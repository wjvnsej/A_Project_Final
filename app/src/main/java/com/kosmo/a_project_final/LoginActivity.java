package com.kosmo.a_project_final;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    String TAG = "iKOSMO";
    private VideoView videoView;
    EditText m_id, m_pw;
    ProgressDialog dialog;
    TextView join, login_search;
    String loginURL = "http://192.168.219.200:8282/project_final/android/memberLogin.do";
    String sessionURL = "http://192.168.219.200:8282/project_final/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* 로딩화면 부르기 */
        final Intent intent = new Intent(this, Loading.class);
        startActivity(intent);

        /* 동영상 재생 */
        videoView = (VideoView) findViewById(R.id.loginvideo);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        final Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login);
        videoView.setMediaController(null);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                mp.setLooping(true);
            }
        });


        /* 회원가입, 아이디비번찾기 */
        join = (TextView)findViewById(R.id.join);
        login_search = (TextView)findViewById(R.id.login_search);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                intent.putExtra("url", "http://192.168.219.200:8282/project_final/member/memberSelect.do");
                startActivity(intent);
            }
        });

        login_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                intent.putExtra("url", "http://192.168.219.200:8282/project_final/member/id_pw.do");
                startActivity(intent);
            }
        });

        /* 로그인 */
        //위젯얻어오기
        m_id = (EditText)findViewById(R.id.m_id);
        m_pw = (EditText)findViewById(R.id.m_pw);

        Button btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //http://본인컴퓨터IP:8080/경로명
                /*
                execute()메소드를 통해 doInBackground()메소드를 호출한다.
                이때 전달하는 파라미터는 총 3가지로
                첫번째는 요청URL, 두번째와 세번째는 서버로 전송할 파라미터임.
                 */
                new AsyncHttpServer().execute(
                        loginURL,
                        "m_id="+m_id.getText().toString(),
                        "m_pw="+m_pw.getText().toString()
                );
            }
        });

        //진행대화창을 띄울 준비를 함.
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);//Back버튼을 누를때 창이 닫히게 설정
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIcon(android.R.drawable.ic_dialog_email);
        dialog.setTitle("로그인 처리중");
        dialog.setMessage("서버로부터 응답을 기다리고 있습니다.");
    }

    class AsyncHttpServer extends AsyncTask<String, Void, String>
    {

        /*
         AsyncTask로 백그라운드 작업을 실행하기 전에 onPreExcuted( )실행됩니다.
         이 부분에는 이미지 로딩 작업이라면 로딩 중 이미지를 띄워 놓기 등,
         스레드 작업 이전에 수행할 동작을 구현합니다.
        */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //응답대기 대화창을 띄워줌
            if(!dialog.isShowing()) dialog.show();
        }

        /*
        execute()를 통해 전달한 3개의 파라미터를 가변인자를 통해 전달받는다.
        해당 값은 배열형태로 사용하게된다.
         */
        @Override
        protected String doInBackground(String... strings) {

            StringBuffer receiveData = new StringBuffer();

            try {
                URL url = new URL(strings[0]);//파라미터1 : 요청URL
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();


                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream out= conn.getOutputStream();

                out.write(strings[1].getBytes());//파라미터2 : 사용자아이디
                out.write("&".getBytes());//&를 사용하여 쿼리스트링 형태로 만들어준다.
                out.write(strings[2].getBytes());//파라미터3 : 사용자패스워드


                String COOKIES_HEADER = "Set-Cookie";
                conn.connect();
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                if(cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        String cookieName = HttpCookie.parse(cookie).get(0).getName();
                        String cookieValue = HttpCookie.parse(cookie).get(1).getValue();

                        String cookieString = cookieName + "=" + cookieValue + "Domain=" + loginURL;

                        CookieManager.getInstance().setCookie(loginURL, cookieString);
                        String getCookie = CookieManager.getInstance().getCookie(loginURL);
                        SharedPreference.setAttribute(getApplicationContext(), "cookie", getCookie);
                        SharedPreference.setAttribute(getApplicationContext(), "loginURL", loginURL);
                    }
                }


                out.flush();
                out.close();


                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

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
            }
            catch(Exception e){
                e.printStackTrace();
            }

            //로그출력
            Log.i(TAG, receiveData.toString());

            //서버에서 내려준 JSON정보를 저장후 반환
            return receiveData.toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /*
        doInBackground()에서 반환된 값은 해당 함수로 전달된다.
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            StringBuffer sb = new StringBuffer();

            try{
                JSONObject jsonObject = new JSONObject(s);
                int success = Integer.parseInt(jsonObject.getString("isLogin"));
                JSONObject memberInfo = jsonObject.getJSONObject("memberInfo");
                String m_id = memberInfo.getString("m_id");
                String m_name = memberInfo.getString("m_name");

                SharedPreference.setAttribute(getApplicationContext(), "m_id", m_id);
                SharedPreference.setAttribute(getApplicationContext(), "m_name", m_name);

                //파싱후 로그인 성공인 경우
                if(success==1){
                    //메인으로 이동
                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);

                }
                else{
                    sb.append("로그인실패ㅜㅜ");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            //결과출력
            dialog.dismiss();
            Toast.makeText(getApplicationContext(),
                    sb.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

}
