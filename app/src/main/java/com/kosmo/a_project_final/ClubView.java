package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_club_view);

        ImageView imageView = (ImageView)findViewById(R.id.c_emb);
        TextView textView = (TextView)findViewById(R.id.c_name);

        Intent clubIntent = getIntent();
        c_emb = clubIntent.getStringExtra("c_emb");
        c_name = clubIntent.getStringExtra("c_name");

        String img = "http://192.168.219.200:8282/project_final/resources/uploadsFile/"+c_emb;
        Picasso.get().load(img).into(imageView);


        textView.setText(c_name);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.rankinglay);
        linearLayout.setVisibility(View.GONE);

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    //클럽 맴버 리스트
    public void ClubMemberList(View view){
        Intent clubIntent = getIntent();
        String c_idx = clubIntent.getStringExtra("c_idx");
        Log.i(TAG,"c_idx:"+c_idx);

        new AsyncHttpServer().execute(
                "http://192.168.219.200:8282/project_final/android/clubMember.do",
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
        protected void onPostExecute(final List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            Log.i(TAG,"Adapter 로 넘어가는 값 : "+ String.valueOf(maps));

            //상단바 변경
            TextView text1 = (TextView)findViewById(R.id.text1);
            TextView text2 = (TextView)findViewById(R.id.text2);
            TextView text3 = (TextView)findViewById(R.id.text3);
            TextView text4 = (TextView)findViewById(R.id.text4);
            TextView text5 = (TextView)findViewById(R.id.text5);
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.rankinglay);
            linearLayout.setVisibility(View.GONE);
            LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
            linearLayout2.setVisibility(View.VISIBLE);
            ListView listView1 =(ListView)findViewById(R.id.club_memberlist);
            listView1.setVisibility(View.VISIBLE);
            ListView listView2 =(ListView)findViewById(R.id.club_Ranklist);
            listView2.setVisibility(View.GONE);


            text1.setText("이름");
            text2.setText("핸드폰");
            text3.setText("등급");
            text4.setVisibility(View.GONE);
            text5.setVisibility(View.GONE);

            //리스트뷰 띄우기
            ClubMember_Adapter clubMemberAdapter = new ClubMember_Adapter(getApplicationContext(),R.layout.activity_club_member__adapter, maps);
            listView.setAdapter(clubMemberAdapter);

            //리스트뷰 안에 아이템들 눌렀을때
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String m_pic = maps.get(position).get("m_pic").toString();
                    String m_name = maps.get(position).get("m_name").toString();
                    String m_birth = maps.get(position).get("m_birth").toString();
                    String m_phone = maps.get(position).get("m_phone").toString();
                    String m_position = maps.get(position).get("m_position").toString();
                    String m_sex = maps.get(position).get("m_sex").toString();
                    String m_foot = maps.get(position).get("m_foot").toString();
                    String m_date = maps.get(position).get("m_date").toString();
                    String m_abil = maps.get(position).get("m_abil").toString();
                    Log.i(TAG,"m_pic ="+m_pic);
                    Log.i(TAG,"m_name ="+m_name);
                    Log.i(TAG,"m_birth ="+m_birth);
                    Log.i(TAG,"m_phone ="+m_phone);
                    Log.i(TAG,"m_position ="+m_position);
                    Log.i(TAG,"m_sex ="+m_sex);
                    Log.i(TAG,"m_foot ="+m_foot);
                    Log.i(TAG,"m_date ="+m_date);
                    Log.i(TAG,"m_abil ="+m_abil);

                    ClubMemberDialog clubMemberDialog = new ClubMemberDialog(ClubView.this);
                    clubMemberDialog.callFunction(
                            m_pic,
                            m_name,
                            m_birth,
                            m_phone,
                            m_position,
                            m_sex,
                            m_foot,
                            m_date,
                            m_abil
                    );



                }
            });

        }
    }

    //클럽 모든 경기 리스트
    public void ClubMatch(View view){

        Intent clubIntent1 = getIntent();
        //int c_idx = Integer.parseInt(clubIntent1.getStringExtra("c_idx"));
        String c_idx = clubIntent1.getStringExtra("c_idx");
        SharedPreference.setAttribute(getApplicationContext(), "c_idx", c_idx);
        String m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        Log.i(TAG,"c_idx:"+c_idx);

        new AsyncHttpServer2().execute(
                "http://192.168.219.200:8282/project_final/android/clubViewMatch.do",
                "c_idx="+c_idx,
                "m_id="+m_id
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
                out.write("&".getBytes());//&를 사용하여 쿼리스트링 형태로 만들어준다.
                out.write(strings[2].getBytes());//파라미터2 : 사용자아이디
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
            TextView text5 = (TextView)findViewById(R.id.text5);
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.rankinglay);
            linearLayout.setVisibility(View.GONE);
            LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
            linearLayout2.setVisibility(View.VISIBLE);
            ListView listView1 =(ListView)findViewById(R.id.club_memberlist);
            listView1.setVisibility(View.VISIBLE);
            ListView listView2 =(ListView)findViewById(R.id.club_Ranklist);
            listView2.setVisibility(View.GONE);

            text1.setText("날짜");
            text2.setText("시간");
            text3.setText("장소");
            text4.setVisibility(View.VISIBLE);
            text4.setText("상대팀");
            text5.setVisibility(View.GONE);

            //리스트뷰 띄우기
            ClubMatch_Adapter clubMatchAdapter = new ClubMatch_Adapter(getApplicationContext(),R.layout.activity_club_member__adapter, maps);
            listView.setAdapter(clubMatchAdapter);

            //리스트뷰 안에 아이템들 눌렀을때
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), ClubMatchView.class);

                    intent.putExtra("g_sname",maps.get(position).get("g_sname").toString());
                    intent.putExtra("g_memo",maps.get(position).get("g_memo").toString());
                    intent.putExtra("g_lat",maps.get(position).get("g_lat").toString());
                    intent.putExtra("g_lng",maps.get(position).get("g_lng").toString());
                    intent.putExtra("g_idx",maps.get(position).get("g_idx").toString());
                    intent.putExtra("gm_check",maps.get(position).get("gm_check").toString());
                    intent.putExtra("g_formation",maps.get(position).get("g_formation").toString());

                    startActivity(intent);
                }
            });

        }
    }

    //클럽 모든 경기 리스트 전술판
    public void ClubTactic(View view){

        Intent clubIntent1 = getIntent();
        //int c_idx = Integer.parseInt(clubIntent1.getStringExtra("c_idx"));
        String c_idx = clubIntent1.getStringExtra("c_idx");
        SharedPreference.setAttribute(getApplicationContext(), "c_idx", c_idx);
        String m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        Log.i(TAG,"c_idx:"+c_idx);
        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
        linearLayout2.setVisibility(View.VISIBLE);
        new AsyncHttpServer3().execute(
                "http://192.168.219.200:8282/project_final/android/clubViewMatch.do",
                "c_idx="+c_idx,
                "m_id="+m_id
        );
    }
    class AsyncHttpServer3 extends AsyncTask<String, Void, List<Map<String, Object>>>
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
                out.write("&".getBytes());//&를 사용하여 쿼리스트링 형태로 만들어준다.
                out.write(strings[2].getBytes());//파라미터2 : 사용자아이디
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
            TextView text5 = (TextView)findViewById(R.id.text5);
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.rankinglay);
            linearLayout.setVisibility(View.GONE);
            LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
            linearLayout2.setVisibility(View.VISIBLE);
            ListView listView1 =(ListView)findViewById(R.id.club_memberlist);
            listView1.setVisibility(View.VISIBLE);
            ListView listView2 =(ListView)findViewById(R.id.club_Ranklist);
            listView2.setVisibility(View.GONE);

            text1.setText("날짜");
            text2.setText("시간");
            text3.setText("장소");
            text4.setVisibility(View.VISIBLE);
            text4.setText("전술판");
            text5.setVisibility(View.VISIBLE);
            text5.setText("기록");

            //리스트뷰 띄우기
            ClubTactic_Adapter clubTacticAdapter = new ClubTactic_Adapter(getApplicationContext(),R.layout.activity_club_tactic__adapter, maps, c_name);
            listView.setAdapter(clubTacticAdapter);

        }
    }

    public void ClubView(View view){
        LinearLayout linearLayout1 = (LinearLayout)findViewById(R.id.rankinglay);
        linearLayout1.setVisibility(View.VISIBLE);
        ListView listView =(ListView)findViewById(R.id.club_memberlist);
        listView.setVisibility(View.GONE);
        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
        linearLayout2.setVisibility(View.GONE);
    }

    public void btn_goal(View view){
        Intent clubIntent11 = getIntent();
        String c_idx = clubIntent11.getStringExtra("c_idx");
        Log.i(TAG,"c_idx:"+c_idx);
        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
        linearLayout2.setVisibility(View.GONE);

        new AsyncHttpServer4().execute(
                "http://192.168.219.200:8282/project_final/android/clubMemberGoal.do",
                "c_idx="+c_idx
        );
    }
    class AsyncHttpServer4 extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> goalRanking = null;
        ListView listView = (ListView)findViewById(R.id.club_Ranklist);

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
                goalRanking = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return goalRanking;
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
            TextView text5 = (TextView)findViewById(R.id.text5);
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.rankinglay);
            linearLayout.setVisibility(View.VISIBLE);
            LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
            linearLayout2.setVisibility(View.VISIBLE);
            ListView listView1 =(ListView)findViewById(R.id.club_memberlist);
            listView1.setVisibility(View.GONE);
            ListView listView2 =(ListView)findViewById(R.id.club_Ranklist);
            listView2.setVisibility(View.VISIBLE);

            text1.setText("순위");
            text2.setText("사진");
            text3.setText("선수명");
            text4.setVisibility(View.VISIBLE);
            text4.setText("경기 수");
            text5.setVisibility(View.VISIBLE);
            text5.setText("득점");

            //리스트뷰 띄우기
            ClubMemberGoalRank_Adapter clubMemberGoalRank = new ClubMemberGoalRank_Adapter(getApplicationContext(), maps,R.layout.activity_club_member_goal_rank);
            listView.setAdapter(clubMemberGoalRank);
        }
    }

    //팀원 어시 랭킹
    public void btn_Assist(View view){
        Intent clubIntent11 = getIntent();
        String c_idx = clubIntent11.getStringExtra("c_idx");
        Log.i(TAG,"c_idx:"+c_idx);
        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
        linearLayout2.setVisibility(View.GONE);

        new AsyncHttpServer5().execute(
                "http://192.168.219.200:8282/project_final/android/clubMemberAssist.do",
                "c_idx="+c_idx
        );
    }

    class AsyncHttpServer5 extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> assist = null;
        ListView listView = (ListView)findViewById(R.id.club_Ranklist);

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
                assist = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return assist;
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
            TextView text5 = (TextView)findViewById(R.id.text5);
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.rankinglay);
            linearLayout.setVisibility(View.VISIBLE);
            LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
            linearLayout2.setVisibility(View.VISIBLE);
            ListView listView1 =(ListView)findViewById(R.id.club_memberlist);
            listView1.setVisibility(View.GONE);
            ListView listView2 =(ListView)findViewById(R.id.club_Ranklist);
            listView2.setVisibility(View.VISIBLE);

            text1.setText("순위");
            text2.setText("사진");
            text3.setText("선수명");
            text4.setVisibility(View.VISIBLE);
            text4.setText("경기 수");
            text5.setVisibility(View.VISIBLE);
            text5.setText("어시스트");

            //리스트뷰 띄우기
            ClubMemberAssist_Adapter clubMemberAssistAdapter =
                    new ClubMemberAssist_Adapter(getApplicationContext(), maps, R.layout.activity_club_member_assist__adapter);
            listView.setAdapter(clubMemberAssistAdapter);
        }
    }
    //팀원 공포 랭킹
    public void btn_attack(View view){
        Intent clubIntent11 = getIntent();
        String c_idx = clubIntent11.getStringExtra("c_idx");
        Log.i(TAG,"c_idx:"+c_idx);
        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
        linearLayout2.setVisibility(View.GONE);

        new AsyncHttpServer6().execute(
                "http://192.168.219.200:8282/project_final/android/clubMemberPoint.do",
                "c_idx="+c_idx
        );
    }

    class AsyncHttpServer6 extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> pointRank = null;
        ListView listView = (ListView)findViewById(R.id.club_Ranklist);

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
                pointRank = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return pointRank;
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
            TextView text5 = (TextView)findViewById(R.id.text5);
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.rankinglay);
            linearLayout.setVisibility(View.VISIBLE);
            LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
            linearLayout2.setVisibility(View.VISIBLE);
            ListView listView1 =(ListView)findViewById(R.id.club_memberlist);
            listView1.setVisibility(View.GONE);
            ListView listView2 =(ListView)findViewById(R.id.club_Ranklist);
            listView2.setVisibility(View.VISIBLE);

            text1.setText("순위");
            text2.setText("사진");
            text3.setText("선수명");
            text4.setVisibility(View.VISIBLE);
            text4.setText("경기 수");
            text5.setVisibility(View.VISIBLE);
            text5.setText("공격포인트");

            //리스트뷰 띄우기
            ClubMemberPoint_Adapter clubMemberPointAdapter =
                    new ClubMemberPoint_Adapter(getApplicationContext(), maps,R.layout.activity_club_member_point__adapter);
            listView.setAdapter(clubMemberPointAdapter);
        }
    }

    //팀원 경기 랭킹
    public void btn_participation(View view){
        Intent clubIntent11 = getIntent();
        String c_idx = clubIntent11.getStringExtra("c_idx");
        Log.i(TAG,"c_idx:"+c_idx);
        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
        linearLayout2.setVisibility(View.GONE);

        new AsyncHttpServer7().execute(
                "http://192.168.219.200:8282/project_final/android/clubMemberAppearance.do",
                "c_idx="+c_idx
        );
    }

    class AsyncHttpServer7 extends AsyncTask<String, Void, List<Map<String, Object>>>
    {
        List<Map<String, Object>> appearanceRank = null;
        ListView listView = (ListView)findViewById(R.id.club_Ranklist);

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
                appearanceRank = g.fromJson(receiveData.toString(),listType);

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return appearanceRank;
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
            TextView text5 = (TextView)findViewById(R.id.text5);
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.rankinglay);
            linearLayout.setVisibility(View.VISIBLE);
            LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.topbar);
            linearLayout2.setVisibility(View.VISIBLE);
            ListView listView1 =(ListView)findViewById(R.id.club_memberlist);
            listView1.setVisibility(View.GONE);
            ListView listView2 =(ListView)findViewById(R.id.club_Ranklist);
            listView2.setVisibility(View.VISIBLE);

            text1.setText("순위");
            text2.setText("사진");
            text3.setText("선수명");
            text4.setVisibility(View.VISIBLE);
            text4.setText("경기 수");
            text5.setVisibility(View.GONE);

            //리스트뷰 띄우기
            ClubMemberAppearance_Adapter clubMemberAppearance =
                    new ClubMemberAppearance_Adapter(getApplicationContext(), maps, R.layout.activity_club_member_appearance__adapter);
            listView.setAdapter(clubMemberAppearance);
        }
    }

}
