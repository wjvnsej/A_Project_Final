package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    int [] resIds = {
            R.drawable.club, R.drawable.matching, R.drawable.manager, R.drawable.mypage,
            R.drawable.qna, R.drawable.ball, R.drawable.tactics, R.drawable.qr
    };
    String[] titles = {
            "Club", "Matching", "Manager", "Mypage",
            "Q&A", "Charge", "Tacktics", "QR"
    };

    private GridView menuView;
    private TextView logout, log_name;
    String m_id, m_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 상태 바 지우기(전체화면) */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* 로딩화면 부르기 */
        final Intent intent = new Intent(this, Loading.class);
        startActivity(intent);
        setContentView(R.layout.activity_main);

        onBackPressed();


        /* 메뉴 띄우기 */
        MenuAdapter adapter = new MenuAdapter(this, resIds, titles );
        menuView = (GridView)findViewById(R.id.gridview);
        menuView.setAdapter(adapter);
        menuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                //커스텀 대화상자를 띄우기 위해 XML파일을 전개한다.
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View detail_layout = inflater.inflate(R.layout.activity_club, null);

                Intent intent1;

                if(titles[i] == "Club"){
                    intent1 = new Intent(getApplicationContext(), ClubActivity.class);

                    startActivity(intent1);
                }
                else if(titles[i] == "Matching"){
                    intent1 = new Intent(getApplicationContext(), WebviewActivity.class);
                    intent1.putExtra("title", titles[i]);
                    intent1.putExtra("url", "http://192.168.219.200:8282/project_final/match/matchMain.do");
                    startActivity(intent1);
                }
                else if(titles[i] == "Manager"){
                    intent1 = new Intent(getApplicationContext(), WebviewActivity.class);
                    intent1.putExtra("title", titles[i]);
                    intent1.putExtra("url", "http://192.168.219.200:8282/project_final/manager/managerMain.do");
                    startActivity(intent1);
                }
                else if(titles[i] == "Mypage"){
                    intent1 = new Intent(getApplicationContext(), WebviewActivity.class);
                    intent1.putExtra("title", titles[i]);
                    intent1.putExtra("url", "http://192.168.219.200:8282/project_final/member/mypageMain.do");
                    startActivity(intent1);
                }
                else if(titles[i] == "Q&A"){
                    intent1 = new Intent(getApplicationContext(), WebviewActivity.class);
                    intent1.putExtra("title", titles[i]);
                    intent1.putExtra("url", "http://192.168.219.200:8282/project_final/customer/qnaMain.do");
                    startActivity(intent1);
                }
                else if(titles[i] == "Charge"){
                    intent1 = new Intent(getApplicationContext(), WebviewActivity.class);
                    intent1.putExtra("title", titles[i]);
                    intent1.putExtra("url", "http://192.168.219.200:8282/project_final/payment/paymentMain.do");
                    startActivity(intent1);
                }
                else if(titles[i] == "Tacktics"){
                    intent1 = new Intent(getApplicationContext(), WebviewActivity.class);
                    intent1.putExtra("title", titles[i]);
                    intent1.putExtra("url", "http://192.168.219.200:8282/project_final/club/clubTacticBoard.do?g_idx=97");
                    startActivity(intent1);
                }
            }
        });

        log_name = (TextView)findViewById(R.id.log_name);
        m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        m_name = SharedPreference.getAttribute(getApplicationContext(), "m_name");

        log_name.setText(m_name);


        String id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        Toast.makeText(getApplicationContext(), m_name + " 님 환영합니다!", Toast.LENGTH_SHORT).show();

    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
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
                                        Toast.makeText(MainActivity.this,
                                                "로그아웃 되었습니다.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(
                                                getApplicationContext(), // 현재 화면의 제어권자
                                                LoginActivity.class); // 다음 넘어갈 클래스 지정
                                        SharedPreference.removeAllAttribute(getApplicationContext());
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

























