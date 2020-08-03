package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ClubView extends AppCompatActivity {
    String c_emb,c_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_view);

        ImageView imageView = (ImageView)findViewById(R.id.c_emb);
        TextView textView = (TextView)findViewById(R.id.c_name);

        Intent clubIntent = getIntent();
        c_emb = clubIntent.getStringExtra("c_emb");
        c_name = clubIntent.getStringExtra("c_name");

        String img = "http://192.168.219.130:8282/project_final/resources/uploadsFile/"+c_emb;
        Picasso.get().load(img).into(imageView);

        textView.setText(c_name);

    }
}
