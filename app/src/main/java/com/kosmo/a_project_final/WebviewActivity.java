package com.kosmo.a_project_final;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.http.cookie.Cookie;

import java.util.HashMap;
import java.util.List;

public class WebviewActivity extends AppCompatActivity {

    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    String url, mode = "";
    String memberURL = "http://192.168.219.200:8282/project_final/android/memberLogin.do?m_id=";
    Handler handler = new Handler();
    String token="";

    private static final int FILECHOOSER_RESULTCODE = 1;
    private final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2;
    private ValueCallback<Uri> mUploadMessage = null;
    private ValueCallback<Uri[]> filePathCallbackLollipop;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        final Intent intent = getIntent();
        url = intent.getStringExtra("url");

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



        mWebView.setWebChromeClient(new WebChromeClient(){
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams){

                if(filePathCallbackLollipop != null){
                    filePathCallbackLollipop.onReceiveValue(null);
                    filePathCallbackLollipop = null;
                }
                filePathCallbackLollipop = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_LOLLIPOP_REQ_CODE);

                return true;
            }
        });


        mode = intent.getStringExtra("mode");
        if(!mode.equals("")){
            String m_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");

            Log.d("ikosmo", "m_id : " + m_id);
            url += "?m_id=" + m_id;
            Log.d("ikosmo", "url : " + url);
            mWebView.loadUrl(url);
        }
        else if(mode.equals("")) {
            mWebView.loadUrl(url);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == FILECHOOSER_LOLLIPOP_REQ_CODE) {
            if (filePathCallbackLollipop == null) {
                return;
            }
            filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
            filePathCallbackLollipop = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ClubActivity.class));
    }
}
