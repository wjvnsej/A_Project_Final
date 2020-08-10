package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Game_QR_Create extends AppCompatActivity {

    private ImageView iv;
    private String text;
    String g_idx1, g_qrcheck;
    String g_num;
    Button btn_history;
    String TAG = "iKOSMO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game__q_r__create);

        Intent intent = getIntent();
        g_idx1 = intent.getStringExtra("g_idx");
        g_num = intent.getStringExtra("g_num");

        Log.i(TAG, "g_num : "+g_num);


        btn_history = (Button)findViewById(R.id.btn_history);

            btn_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AsyncHttpServer().execute(
                            "http://192.168.219.109:8282/project_final/android/select_qrcheck.do",
                            "g_idx="+g_idx1
                    );


                }
            });

        final String[] g_idx=g_idx1.split("\\.");


        iv = (ImageView)findViewById(R.id.qrcode);
        //상대방이 큐알코드 찍으면 넘어가는 페이지
        text = "http://192.168.219.109:8282/project_final/android/your_QR_Check.do?g_idx="+g_idx[0]+"&g_num="+g_num;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);
        }catch (Exception e){}


        }

    class AsyncHttpServer extends AsyncTask<String, Void, List<Map<String, Object>>> {
        List<Map<String, Object>> g_qrCheck = null;

        @Override
        protected List<Map<String, Object>> doInBackground(String... strings) {
            try {
                StringBuffer receiveData = new StringBuffer();
                URL url = new URL(strings[0]);//파라미터1 : 요청URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                out.write(strings[1].getBytes());//파라미터2 : 클럽idx
                out.flush();
                out.close();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    // 스프링 서버에 연결성공한 경우 JSON데이터를 읽어서 저장한다.
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "UTF-8")
                    );
                    String data;
                    while ((data = reader.readLine()) != null) {
                        receiveData.append(data + "\r\n");
                    }
                    reader.close();
                } else {
                    Log.i(TAG, "HTTP_OK 안됨. 연결실패.");
                }

                Log.i(TAG, "서버에서 넘어온 값 : " + receiveData.toString());

                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                Gson g = new Gson();
                g_qrCheck = g.fromJson(receiveData.toString(), listType);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return g_qrCheck;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            g_qrcheck = maps.get(0).get("g_qrcheck").toString();
            g_num = maps.get(0).get("g_num").toString();

            Log.i(TAG, "g_qrcheck : "+g_qrcheck);
            if (g_qrcheck.equals("no")){
                Toast.makeText(getApplicationContext(), "상대방이 QR코드를 스캔하지않아 넘어갈수 없습니다", Toast.LENGTH_LONG).show();
            }else if(g_qrcheck.equals("yes")){
                Toast.makeText(getApplicationContext(), "상대방이 QR코드를 스캔하였습니다.", Toast.LENGTH_LONG).show();

                Intent intent1 = getIntent();

                g_idx1 = intent1.getStringExtra("g_idx");
                Log.i(TAG, "g_idx1 : "+g_idx1);

                g_num = intent1.getStringExtra("g_num");

                Log.i(TAG, "g_num : "+g_num);

                final String[] g_idx=g_idx1.split("\\.");

                Intent intent = new Intent(getApplicationContext(), QR_WebView_Activity.class);
                //내가 들어갈 페이지
                intent.putExtra("url","http://192.168.219.109:8282/project_final/android/my_QR_Check.do?g_idx="+g_idx[0]+"&g_num="+g_num);

                startActivity(intent);
            }
        }
    }



}
