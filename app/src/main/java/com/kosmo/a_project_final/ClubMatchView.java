package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class ClubMatchView extends AppCompatActivity {

    String TAG = "iKOSMO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_match_view);

        Intent intent = getIntent();

        TextView g_snameTxt = (TextView)findViewById(R.id.g_snameTxt);
        g_snameTxt.setText(intent.getStringExtra("g_sname"));

        TextView g_memoTxt = (TextView)findViewById(R.id.g_memoTxt);
        g_memoTxt.setText(intent.getStringExtra("g_memo"));

        String g_lat1 = intent.getStringExtra("g_lat");
        String g_lng1 = intent.getStringExtra("g_lng");

        double g_lat = Double.parseDouble(g_lat1);
        double g_lng = Double.parseDouble(g_lng1);

        Log.i(TAG,"g_lat : "+g_lat);
        Log.i(TAG,"g_lng : "+g_lng);

        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(g_lat, g_lng), true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(intent.getStringExtra("g_sname"));
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(g_lat, g_lng));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

    }

    public void btn_request(View view){
        Intent intent = getIntent();
        String m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        String g_idx = intent.getStringExtra("g_idx");
        Log.i(TAG, "g_idx : "+ g_idx);
        Log.i(TAG, "m_id : "+ m_id);

        new AsyncHttpServer().execute(
                "http://192.168.219.200:8282/project_final/android/gameMemberApply.do",
                "g_idx"+g_idx,
                "m_id"+m_id
        );
    }
    class AsyncHttpServer extends AsyncTask<String, Void , String>
    {

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer receiveData = new StringBuffer();
            try {
                URL url = new URL(strings[0]);//파라미터1 : 요청URL
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream out= conn.getOutputStream();
                out.write(strings[1].getBytes());
                out.write("&".getBytes());//&를 사용하여 쿼리스트링 형태로 만들어준다.
                out.write(strings[2].getBytes());
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

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return receiveData.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent intent = new Intent(getApplicationContext(), ClubView.class);
            startActivity(intent);
        }
    }
    public void btn_refusal(View view){

    }
}
