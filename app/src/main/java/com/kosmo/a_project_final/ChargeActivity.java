package com.kosmo.a_project_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ChargeActivity extends AppCompatActivity {

    TextView title_text;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        Intent titleIntent = getIntent();
        title_text = (TextView)findViewById(R.id.title);
        title = titleIntent.getStringExtra("title");
        title_text.setText(title);


    }
}
