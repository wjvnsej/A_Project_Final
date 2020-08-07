package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
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
    String g_idx;
    String formation;
    Button apply_button;
    Button reject_button;
    Button kakao;
    MapView kakaoMapView;
    double g_lat, g_lng;
    double myLat, myLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_match_view);

        Intent intent = getIntent();

        g_idx = intent.getStringExtra("g_idx");
        formation = intent.getStringExtra("g_formation");

        TextView g_snameTxt = (TextView)findViewById(R.id.g_snameTxt);
        g_snameTxt.setText(intent.getStringExtra("g_sname"));

        TextView g_memoTxt = (TextView)findViewById(R.id.g_memoTxt);
        g_memoTxt.setText(intent.getStringExtra("g_memo"));

        String gm_check = intent.getStringExtra("gm_check");
        apply_button = (Button)findViewById(R.id.btn_request);
        if(gm_check.equals("0")){
            apply_button.setEnabled(true);
            apply_button.setText("참가 신청");
        }
        else if(gm_check.equals("1")){
            apply_button.setEnabled(false);
            apply_button.setText("참가 완료");
        }

        String g_lat1 = intent.getStringExtra("g_lat");
        String g_lng1 = intent.getStringExtra("g_lng");

        g_lat = Double.parseDouble(g_lat1);
        g_lng = Double.parseDouble(g_lng1);

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


        //권한체크 후 사용자에 의해 취소되었다면 다시 요청함.
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);
        }

    }

    public void kakao(View view){

        //위치관리자에 대한 참조값을 구한다.
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //위치가 업데이트 되면 호출되는 리스너를 정의한다.
        LocationListener locationListener = new LocationListener() {

            //새로운 위치가 발견되면 위치제공자에 의해 호출되는 콜백메소드
            @Override
            public void onLocationChanged(Location location) {
                myLat = location.getLatitude();
                myLng = location.getLongitude();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        //위치를 업데이트 받기위해 리스너를 위치관리자에 등록한다.
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //네트워크를 통해서 위치를 알 수 있다.
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);

            //GPS 를 통해서 위치를 알 수 있다.
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }

        String url = "kakaomap://route?sp=" + myLat + "," + myLng + "&ep=" + g_lat  + "," + g_lng + "&by=CAR";
        final Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent3);

    }

    public void btn_request(View view){

        apply_button = (Button)findViewById(R.id.btn_request);
        if(apply_button.getText().equals("참가 완료")){
            Toast.makeText(getApplicationContext(), "이미 참가 완료한 경기입니다.", Toast.LENGTH_SHORT).show();
            return;
    }

        Intent intent = getIntent();
        String m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        String g_idx = intent.getStringExtra("g_idx");
        Log.i(TAG, "g_idx : "+ g_idx);
        Log.i(TAG, "m_id : "+ m_id);

        new AsyncHttpServer().execute(
                "http://192.168.219.200:8282/project_final/android/gameMemberApply.do",
                "g_idx="+g_idx,
                "m_id="+m_id
        );
    }

    class AsyncHttpServer extends AsyncTask<String, Void , String> {

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

                Log.d("kosmo1", strings[1]);
                Log.d("kosmo1", strings[2]);

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

            if(s.equals(0)){
                Toast.makeText(getApplicationContext(), "참가 신청에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                apply_button.setEnabled(false);
                apply_button.setText("참가 완료");
                Toast.makeText(getApplicationContext(), "참가 신청에 성공했습니다.", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
    public void btn_reject(View view){

        reject_button = (Button)findViewById(R.id.btn_reject);
        apply_button = (Button)findViewById(R.id.btn_request);

        Intent intent = getIntent();
        String m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        String g_idx = intent.getStringExtra("g_idx");
        Log.i(TAG, "g_idx : "+ g_idx);
        Log.i(TAG, "m_id : "+ m_id);

        new AsyncHttpServer2().execute(
                "http://192.168.219.200:8282/project_final/android/gameMemberReject.do",
                "g_idx="+g_idx,
                "m_id="+m_id
        );
    }
    class AsyncHttpServer2 extends AsyncTask<String, Void , String> {

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

                Log.d("kosmo1", strings[1]);
                Log.d("kosmo1", strings[2]);

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

            if(s.equals(0)){
                Toast.makeText(getApplicationContext(), "참가 거절에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                apply_button.setEnabled(true);
                apply_button.setText("참가 신청");
                Toast.makeText(getApplicationContext(), "참가 거절에 성공했습니다.", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }


    //클럽 맴버 리스트
    public void GameMemberList(View view){

        Intent intent = new Intent(getApplicationContext(), GameMemberList.class);
        intent.putExtra("g_idx", g_idx);
        startActivity(intent);

    }

    public void Gameformation(View view){

        Intent intent =new Intent(getApplicationContext(),ClubFormationView.class);

        intent.putExtra("g_formation",formation);

        startActivity(intent);
    }


}
