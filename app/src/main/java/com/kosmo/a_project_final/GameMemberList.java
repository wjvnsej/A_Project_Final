package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

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

public class GameMemberList extends AppCompatActivity {

    String TAG = "iKOSMO";
    String g_idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_member_list);

        Intent intent = getIntent();
        g_idx = intent.getStringExtra("g_idx");
        GameMemberList(g_idx);
    }


    //클럽 맴버 리스트
    public void GameMemberList(String g_idx){

        new AsyncHttpServer3().execute(
                "http://192.168.219.200:8282/project_final/android/gameMemberList.do",
                "g_idx="+g_idx
        );
    }
    class AsyncHttpServer3 extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> gameMemberList = null;
        ListView listView = (ListView)findViewById(R.id.game_memberlist);

        @Override
        protected List<Map<String, Object>> doInBackground(String... strings) {
            try {
                StringBuffer receiveData = new StringBuffer();
                URL url = new URL(strings[0]);//파라미터1 : 요청URL
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream out= conn.getOutputStream();
                out.write(strings[1].getBytes());//파라미터2 : 클럽idx
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
                gameMemberList = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return gameMemberList;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            Log.i(TAG,"Adapter 로 넘어가는 값 : "+ String.valueOf(maps));

            //상단바 변경
            TextView text1 = (TextView)findViewById(R.id.text1);
            TextView text2 = (TextView)findViewById(R.id.text2);
            TextView text3 = (TextView)findViewById(R.id.text3);
            text1.setText("사진");
            text2.setText("이름");
            text3.setText("연락처");

            //리스트뷰 띄우기
            GameMember_Adapter gameMemberAdapter = new GameMember_Adapter(getApplicationContext(),R.layout.activity_game_member__adapter, maps);
            listView.setAdapter(gameMemberAdapter);

        }
    }



}
