package com.example.farmia_mcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class Loading_screen extends AppCompatActivity {
    ProgressBar progressBar;
    private int progress_status = 0;
    private Handler handler = new Handler();
    TextView tv;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        progressBar = findViewById(R.id.progress_bar);
        tv = findViewById(R.id.progress);

        textToSpeech = new TextToSpeech(Loading_screen.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        new Thread(() -> {
            while (progress_status <= 100) {
                handler.post(() -> {
                    progressBar.setProgress(progress_status);
                    tv.setText(progress_status + " %");
                    if(progress_status == 100) {
                        Intent intent = new Intent(this,Sensor_data.class);
                        startActivity(intent);
                    }
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progress_status++;
            }
        }).start();
    }
}