package com.example.capture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultlayout);

        TextView Body1 = findViewById(R.id.Bodyname);
        TextView Body2 = findViewById(R.id.Bodytype);

        Body1.setText("나의 체형 : ");
        Body2.setText("체형 특징 : ");
    }
}
