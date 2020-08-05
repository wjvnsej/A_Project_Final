package com.kosmo.a_project_final;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.cookie.Cookie;

import java.util.HashMap;
import java.util.List;

public class WebviewActivity extends AppCompatActivity {

    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    String url;
    String memberURL = "http://192.168.219.200:8282/project_final/android/memberLogin.do?m_id=";
    private SharedPreferences mPreferences;

    String token="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        Cookie sessionCookie =null;

        // 웹뷰 시작
        mWebView = (WebView) findViewById(R.id.webView);

        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(true); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(true); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        mWebSettings.setAppCacheEnabled(true);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);

        String cookie = SharedPreference.getAttribute(getApplicationContext(), "cookie");
        String loginURL = SharedPreference.getAttribute(getApplicationContext(), "loginURL");
        String m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
        String m_pw = SharedPreference.getAttribute(getApplicationContext(), "m_pw");

        if(cookie != null){
            String cookieString = cookie + "Domain=" + loginURL;

            CookieManager.getInstance().setCookie(loginURL, cookieString);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String token2= SharedPreference.getAttribute(getApplicationContext(), "auth_token");

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Access-Token", token2);

        mWebView.loadUrl(url);


    }
}
