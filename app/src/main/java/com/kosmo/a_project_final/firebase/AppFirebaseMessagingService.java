package com.kosmo.a_project_final.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.kosmo.a_project_final.LoginActivity;
import com.kosmo.a_project_final.R;
import com.kosmo.a_project_final.SharedPreference;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Map;

public class AppFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    int badge_count;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreference.setAttribute(getApplicationContext(), "token", token);
        Log.d("token", token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> pushDataMap = remoteMessage.getData();

        try {
            sendNotification(pushDataMap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        set_alarm_badge();
    }

    private void sendNotification(Map<String, String> data) throws UnsupportedEncodingException {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notificationBuilder;

        //Android8.0(Oreo, API26) 이상인 경우 채널을 생성이 필요함.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //아래는 채널 생성을 위한 패턴이라 생각하면 됨.
            NotificationChannel notificationChannel =
                    new NotificationChannel("kosmo_id", "kosmo_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("코스모채널입니다.");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);

            //Notification.Builder를 사용하여 알림을 설정한다.

            //채널 생성시 사용된 채널 ID를 인자로 빌더를 생성함.
            notificationBuilder = new Notification.Builder(this, "kosmo_id");
        }
        else {
            // Android7.0(Nougat, API25) 이하일때는 채널생성 필요없음
            notificationBuilder = new Notification.Builder(this);
        }

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        String title = URLDecoder.decode(data.get("title"), "UTF-8");
        String msg = URLDecoder.decode(data.get("msg"), "UTF-8");

        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(msg);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(contentIntent);

        notificationManager.notify(0, notificationBuilder.build());

    }

    public void set_alarm_badge(){

        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        badge_count = Integer.parseInt(SharedPreference.getAttribute(getApplicationContext(),"badge_count"));
        badge_count++;
        intent.putExtra("badge_count", badge_count);

        intent.putExtra("badge_count_package_name", getApplicationContext().getPackageName());
        intent.putExtra("badge_count_class_name", LoginActivity.class.getName());

        SharedPreference.setAttribute(getApplicationContext(), "badge_count", String.valueOf(badge_count));
        sendBroadcast(intent);
    }
}
















