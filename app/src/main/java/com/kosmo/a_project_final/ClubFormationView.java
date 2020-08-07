package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ClubFormationView extends AppCompatActivity {
    String formation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_formation_view);

        Intent intent = getIntent();

        formation = intent.getStringExtra("g_formation");

        ImageView g_formation = (ImageView)findViewById(R.id.formation);

        String img = "http://192.168.219.200:8282/project_final/resources/uploadsFile/"+formation;
        Picasso.get().load(img).into(g_formation);

    }
}
