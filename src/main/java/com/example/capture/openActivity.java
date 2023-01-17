package com.example.capture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class openActivity extends AppCompatActivity {
    EditText AgeEdit;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub3activity_main);
        AgeEdit = (EditText) findViewById(R.id.EditAge);
        Button next = findViewById(R.id.mybody);


    next.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick (View v){
        try {
            text = AgeEdit.getText().toString().trim();

            int age = Integer.parseInt(text);
            Intent intent = new Intent(getApplicationContext(), Sub2Activity.class);
            startActivity(intent);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(openActivity.this, "숫자만 입력하세요", Toast.LENGTH_LONG).show();
        }
    }
    });
}
}
