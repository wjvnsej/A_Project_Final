package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ClubActivity extends AppCompatActivity {

    TextView detail_text;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        Intent titleIntent = getIntent();
        detail_text = (TextView)findViewById(R.id.detail_text);
        title = titleIntent.getStringExtra("title");

        detail_text.setText(title);

    }
}
