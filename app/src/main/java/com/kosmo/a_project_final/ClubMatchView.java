package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import net.daum.mf.map.api.MapView;


public class ClubMatchView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_match_view);

        Intent intent = getIntent();

        TextView g_snameTxt = (TextView)findViewById(R.id.g_snameTxt);
        g_snameTxt.setText(intent.getStringExtra("g_sname"));

//        MapView mapView = new MapView(this);
//
//        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
//        mapViewContainer.addView(mapView);

    }
}
