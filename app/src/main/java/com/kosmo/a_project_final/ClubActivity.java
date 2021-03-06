package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ClubActivity extends AppCompatActivity {

    String TAG = "iKOSMO";
    ImageView club_my , club_seach, club_rank;
    ListView listView;
    private TextView logout, log_name;
    String m_id, m_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* 로딩화면 부르기 */
        final Intent intent = new Intent(this, Loading.class);
        startActivity(intent);

        log_name = (TextView)findViewById(R.id.log_name);
        m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        m_name = SharedPreference.getAttribute(getApplicationContext(), "m_name");

        log_name.setText(m_name);

        onBackPressed();

        String id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        Toast.makeText(getApplicationContext(), m_name + " 님 환영합니다!", Toast.LENGTH_SHORT).show();

        club_my = (ImageView)findViewById(R.id.club_my);
        club_seach = (ImageView)findViewById(R.id.club_seachImg);
        club_rank = (ImageView)findViewById(R.id.club_rank);

        club_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
                new AsyncHttpServer().execute(
                        "http://192.168.219.200:8282/project_final/android/listView.do",
                        "m_id="+m_id
                );
            }
        });

        club_seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClubSearch.class);
                startActivity(intent);
            }
        });

        club_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClubRanking.class);
                startActivity(intent);
            }
        });


    }

    class AsyncHttpServer extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> clubList = null;
        ListView listView = (ListView)findViewById(R.id.club_list);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(String... strings) {

            try {
                StringBuffer receiveData = new StringBuffer();
                URL url = new URL(strings[0]);//파라미터1 : 요청URL
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream out= conn.getOutputStream();
                out.write(strings[1].getBytes());//파라미터2 : 사용자아이디
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
                clubList = g.fromJson(receiveData.toString(),listType);



            }
            catch(Exception e){
                e.printStackTrace();
            }
            return clubList;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final List<Map<String, Object>> s) {
            super.onPostExecute(s);
            Log.i(TAG,"Adapter 로 넘어가는 값 : "+ String.valueOf(s));
            ClubList_Adapter clubListAdapter = new ClubList_Adapter(getApplicationContext(),R.layout.activity_club_list, s);
            listView.setAdapter(clubListAdapter);

            //리스트뷰 띄우기
            ClubList_Adapter adapter = new ClubList_Adapter(getApplicationContext(), R.layout.activity_club_list, s);
            listView = (ListView)findViewById(R.id.club_list);
            listView.setAdapter(adapter);

            //리스트뷰 안에 아이템들 눌렀을때
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(),ClubView.class);

                    intent.putExtra("c_idx",s.get(position).get("c_idx").toString());
                    intent.putExtra("c_name",s.get(position).get("c_name").toString());
                    intent.putExtra("c_emb",s.get(position).get("c_emb").toString());
                    intent.putExtra("c_area",s.get(position).get("c_area").toString());
                    intent.putExtra("c_type",s.get(position).get("c_type").toString());
                    intent.putExtra("c_date",s.get(position).get("c_date").toString());
                    intent.putExtra("c_memo",s.get(position).get("c_memo").toString());
                    intent.putExtra("c_ability",s.get(position).get("c_ability").toString());
                    intent.putExtra("c_gender",s.get(position).get("c_gender").toString());
                    intent.putExtra("c_memlimit",s.get(position).get("c_memlimit").toString());
                    intent.putExtra("c_age",s.get(position).get("c_age").toString());
                    //intent.putExtra("c_color",s.get(position).get("c_color").toString());
                    //intent.putExtra("c_cash",s.get(position).get("c_cash").toString());
                    //intent.putExtra("start",s.get(position).get("start").toString());
                    //intent.putExtra("end",s.get(position).get("end").toString());

                    startActivity(intent);
                }
            });

        }
    }

    public void clubCreate(View view){
        Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);

        intent.putExtra("url", "http://192.168.219.200:8282/project_final/android/clubCreate.do");
        intent.putExtra("mode", "clubCreate");
        startActivity(intent);
    }

    //뒤로가기 버튼
    @Override
    public void onBackPressed() {

        /* 로그아웃 */
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertBasic = new AlertDialog.Builder(v.getContext());
                alertBasic.setCancelable(false);
                alertBasic.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                "로그아웃 되었습니다.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(
                                                getApplicationContext(), // 현재 화면의 제어권자
                                                LoginActivity.class); // 다음 넘어갈 클래스 지정
                                        String token = SharedPreference.getAttribute(getApplicationContext(), "token");
                                        SharedPreference.removeAllAttribute(getApplicationContext());
                                        SharedPreference.setAttribute(getApplicationContext(), "token", token);
                                        String save_token = SharedPreference.getAttribute(getApplicationContext(), "token");
                                        Log.d("ikosmo", save_token);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /*Toast.makeText(MainActivity.this,
                                                "취소클릭합니다.",
                                                Toast.LENGTH_SHORT).show();*/
                                        dialog.cancel();
                                    }
                                })
                        .show();
            }
        });
    }


}
