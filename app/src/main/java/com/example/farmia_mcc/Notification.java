package com.example.farmia_mcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Notification extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        tv = findViewById(R.id.notification);
        String msg = getIntent().getStringExtra("message");
        tv.setText(msg);
    }
}