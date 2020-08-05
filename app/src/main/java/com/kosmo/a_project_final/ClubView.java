package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ClubView extends AppCompatActivity {
    String TAG = "iKOSMO";
    String c_emb,c_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_view);

        ImageView imageView = (ImageView)findViewById(R.id.c_emb);
        TextView textView = (TextView)findViewById(R.id.c_name);

        Intent clubIntent = getIntent();
        c_emb = clubIntent.getStringExtra("c_emb");
        c_name = clubIntent.getStringExtra("c_name");

        String img = "http://192.168.219.130:8282/project_final/resources/uploadsFile/"+c_emb;
        Picasso.get().load(img).into(imageView);


        textView.setText(c_name);

    }
    //클럽 맴버 리스트
    public void ClubMemberList(View view){
        Intent clubIntent = getIntent();
        String c_idx = clubIntent.getStringExtra("c_idx");
        Log.i(TAG,"c_idx:"+c_idx);

        new AsyncHttpServer().execute(
                "http://192.168.219.130:8282/project_final/android/clubMember.do",
                "c_idx="+c_idx
        );
    }
    class AsyncHttpServer extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> clubMemberList = null;
        ListView listView = (ListView)findViewById(R.id.club_memberlist);

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
                clubMemberList = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return clubMemberList;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            Log.i(TAG,"Adapter 로 넘어가는 값 : "+ String.valueOf(maps));

            //상단바 변경
            TextView text1 = (TextView)findViewById(R.id.text1);
            TextView text2 = (TextView)findViewById(R.id.text2);
            TextView text3 = (TextView)findViewById(R.id.text3);
            TextView text4 = (TextView)findViewById(R.id.text4);
            text1.setText("이름");
            text2.setText("핸드폰");
            text3.setText("등급");
            text4.setVisibility(View.GONE);

            //리스트뷰 띄우기
            ClubMember_Adapter clubMemberAdapter = new ClubMember_Adapter(getApplicationContext(),R.layout.activity_club_member__adapter, maps);
            listView.setAdapter(clubMemberAdapter);

        }
    }

    //클럽 모든 경기 리스트
    public void ClubMatch(View view){

        Intent clubIntent1 = getIntent();
        //int c_idx = Integer.parseInt(clubIntent1.getStringExtra("c_idx"));
        String c_idx = clubIntent1.getStringExtra("c_idx");
        Log.i(TAG,"c_idx:"+c_idx);

        new AsyncHttpServer2().execute(
                "http://192.168.219.130:8282/project_final/android/clubViewMatch.do",
                "c_idx="+c_idx
        );
    }
    class AsyncHttpServer2 extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> clubMatch = null;
        ListView listView = (ListView)findViewById(R.id.club_memberlist);

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
                clubMatch = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return clubMatch;
        }

        @Override
        protected void onPostExecute(final List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            Log.i(TAG,"Adapter 로 넘어가는 값 : "+ String.valueOf(maps));
            //상단바 변경
            TextView text1 = (TextView)findViewById(R.id.text1);
            TextView text2 = (TextView)findViewById(R.id.text2);
            TextView text3 = (TextView)findViewById(R.id.text3);
            TextView text4 = (TextView)findViewById(R.id.text4);
            text1.setText("날짜");
            text2.setText("시간");
            text3.setText("장소");
            text4.setVisibility(View.VISIBLE);
            text4.setText("상대팀");

            //리스트뷰 띄우기
            ClubMatch_Adapter clubMatchAdapter = new ClubMatch_Adapter(getApplicationContext(),R.layout.activity_club_member__adapter, maps);
            listView.setAdapter(clubMatchAdapter);

            //리스트뷰 안에 아이템들 눌렀을때
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), ClubMatchView.class);

                    intent.putExtra("g_sname",maps.get(position).get("g_sname").toString());

                    startActivity(intent);
                }
            });

        }
    }

}