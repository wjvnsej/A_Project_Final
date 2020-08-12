package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ClubRanking extends AppCompatActivity {

    String TAG = "iKOSMO";
    Spinner areaSpinner;
    Button btn_totalRanking, btn_rank_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_club_ranking);

        areaSpinner = (Spinner)findViewById(R.id.search_area);

        ArrayList<String> areaList;
        ArrayAdapter<String> areaAdapter;

        areaList = new ArrayList<>();
        areaList.add("지역 선택");
        areaList.add("강남구");areaList.add("강동구");areaList.add("강북구");areaList.add("강서구");
        areaList.add("관악구");areaList.add("광진구");areaList.add("구로구");areaList.add("금천구");
        areaList.add("노원구");areaList.add("도봉구");areaList.add("동대문구");areaList.add("동작구");
        areaList.add("마포구");areaList.add("서대문구");areaList.add("서초구");areaList.add("성동구");
        areaList.add("성북구");areaList.add("송파구");areaList.add("양천구");areaList.add("영등포구");
        areaList.add("용산구");areaList.add("은평구");areaList.add("종로구");areaList.add("중구");
        areaList.add("중랑구");
        areaAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item,areaList);
        areaSpinner.setAdapter(areaAdapter);

        btn_totalRanking = (Button)findViewById(R.id.btn_totalRanking);
        btn_totalRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncHttpServer().execute(
                        "http://192.168.219.200:8282/project_final/android/clubTotalRanking.do"
                );
            }
        });


        btn_rank_search = (Button)findViewById(R.id.btn_rank_search);
        btn_rank_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"areaSpinner.getSelectedItem().toString() : "+ areaSpinner.getSelectedItem().toString());
                new AsyncHttpServer2().execute(
                        "http://192.168.219.200:8282/project_final/android/clubAreaRanking.do",
                        "c_area="+areaSpinner.getSelectedItem().toString()
                );
            }
        });



    }
    class AsyncHttpServer extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> clubTotalRankList = null;
        ListView listView = (ListView)findViewById(R.id.club_ranking_list);

        @Override
        protected List<Map<String, Object>> doInBackground(String... strings) {
            try {
                StringBuffer receiveData = new StringBuffer();
                URL url = new URL(strings[0]);//파라미터1 : 요청URL
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

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
                clubTotalRankList = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return clubTotalRankList;
        }

        @Override
        protected void onPostExecute(final List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            Log.i(TAG,"Adapter 로 넘어가는 값 : "+ String.valueOf(maps));

            //리스트뷰 띄우기
            ClubTotalRank_Adapter clubTotalRankAdapter = new ClubTotalRank_Adapter(getApplicationContext(), maps,R.layout.activity_club_total_rank__adapter);
            listView.setAdapter(clubTotalRankAdapter);

        }
    }

    class AsyncHttpServer2 extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> clubTotalRankList = null;
        ListView listView = (ListView)findViewById(R.id.club_ranking_list);

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
                clubTotalRankList = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return clubTotalRankList;
        }

        @Override
        protected void onPostExecute(final List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            Log.i(TAG,"Adapter 로 넘어가는 값 : "+ String.valueOf(maps));

            //리스트뷰 띄우기
            ClubTotalRank_Adapter clubTotalRankAdapter = new ClubTotalRank_Adapter(getApplicationContext(), maps,R.layout.activity_club_total_rank__adapter);
            listView.setAdapter(clubTotalRankAdapter);

        }
    }



}
