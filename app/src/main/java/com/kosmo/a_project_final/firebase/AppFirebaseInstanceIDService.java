package com.kosmo.a_project_final.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kosmo.a_project_final.SharedPreference;

public class AppFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sentRegistrationToServer(refreshedToken);
    }

    private void sentRegistrationToServer(String token){
        SharedPreference.setAttribute(getApplicationContext(), "token", token);
        Log.d("ikosmo", "서버로 보내는 토큰 : " + token);
    }
}
