package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClubSearch extends AppCompatActivity {

    String TAG = "iKOSMO";
    EditText search_name;
    Spinner guSpinner,abliSpinner,genderSpinner,ageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_club_search);

        search_name = (EditText)findViewById(R.id.search_name);

        guSpinner = (Spinner)findViewById(R.id.search_gu);
        abliSpinner = (Spinner)findViewById(R.id.search_abil);
        genderSpinner = (Spinner)findViewById(R.id.search_gender);
        ageSpinner = (Spinner)findViewById(R.id.search_age);

        ArrayList<String> guList;
        ArrayList<String> abilList;
        ArrayList<String> genderList;
        ArrayList<String> ageList;

        ArrayAdapter<String> guAdapter;
        ArrayAdapter<String> abilAdapter;
        ArrayAdapter<String> genderAdapter;
        ArrayAdapter<String> ageAdapter;

        guList = new ArrayList<>();
        guList.add("");
        guList.add("강남구");guList.add("강동구");guList.add("강북구");guList.add("강서구");
        guList.add("관악구");guList.add("광진구");guList.add("구로구");guList.add("금천구");
        guList.add("노원구");guList.add("도봉구");guList.add("동대문구");guList.add("동작구");
        guList.add("마포구");guList.add("서대문구");guList.add("서초구");guList.add("성동구");
        guList.add("성북구");guList.add("송파구");guList.add("양천구");guList.add("영등포구");
        guList.add("용산구");guList.add("은평구");guList.add("종로구");guList.add("중구");
        guList.add("중랑구");
        guAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item,guList);
        guSpinner.setAdapter(guAdapter);

        abilList = new ArrayList<>();
        abilList.add("");
        abilList.add("최상");abilList.add("상");abilList.add("중상");abilList.add("중");
        abilList.add("중하");abilList.add("하");
        abilAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item,abilList);
        abliSpinner.setAdapter(abilAdapter);

        genderList = new ArrayList<>();
        genderList.add("");
        genderList.add("남자");genderList.add("여자");genderList.add("혼성");
        genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item,genderList);
        genderSpinner.setAdapter(genderAdapter);

        ageList = new ArrayList<>();
        ageList.add("");
        ageList.add("중년부");ageList.add("청년부");ageList.add("청소년부");ageList.add("유소년부");
        ageAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item,ageList);
        ageSpinner.setAdapter(ageAdapter);

        Button btn_search =(Button)findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"c_name : "+search_name.getText().toString());
                Log.i(TAG,"c_area : "+guSpinner.getSelectedItem().toString());
                Log.i(TAG,"c_ability : "+abliSpinner.getSelectedItem().toString());
                Log.i(TAG,"c_gender : "+genderSpinner.getSelectedItem().toString());
                Log.i(TAG,"c_age : "+ageSpinner.getSelectedItem().toString());
                new AsyncHttpServer().execute(
                        "http://192.168.219.200:8282/project_final/android/clubSearch.do",
                        "c_name="+search_name.getText().toString(),
                        "c_area="+guSpinner.getSelectedItem().toString(),
                        "c_ability="+abliSpinner.getSelectedItem().toString(),
                        "c_gender="+genderSpinner.getSelectedItem().toString(),
                        "c_age="+ageSpinner.getSelectedItem().toString()
                );
            }
        });

    }

    class AsyncHttpServer extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> clubSearchList = null;
        ListView listView = (ListView)findViewById(R.id.club_search_list);

        @Override
        protected List<Map<String, Object>> doInBackground(String... strings) {
            try {
                StringBuffer receiveData = new StringBuffer();
                URL url = new URL(strings[0]);//파라미터1 : 요청URL
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream out= conn.getOutputStream();
                out.write(strings[1].getBytes());
                out.write("&".getBytes());
                out.write(strings[2].getBytes());
                out.write("&".getBytes());
                out.write(strings[3].getBytes());
                out.write("&".getBytes());
                out.write(strings[4].getBytes());
                out.write("&".getBytes());
                out.write(strings[5].getBytes());
                out.flush();
                out.close();

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                    // 스프링 서버에 연결성공한 경우 JSON데이터를 읽어서 저장한다.
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(),"UTF-8")
                    );
                    String data;
                    while((data=reader.readLine())!=null){
                        receiveData.append(data+"\r\n");
                    }
                    reader.close();
                }
                else{
                    Log.i(TAG, "HTTP_OK 안됨. 연결실패.");
                }

                Log.i(TAG,"서버에서 넘어온 값 : "+ receiveData.toString());

                Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                Gson g = new Gson();
                clubSearchList = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return clubSearchList;
        }

        @Override
        protected void onPostExecute(final List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            Log.i(TAG,"Adapter 로 넘어가는 값 : "+ String.valueOf(maps));

            //리스트뷰 띄우기
            ClubSearch_Adapter clubSearch_adapter = new ClubSearch_Adapter(getApplicationContext(), maps,R.layout.activity_club_search__adapter);
            listView.setAdapter(clubSearch_adapter);

        }
    }
}
