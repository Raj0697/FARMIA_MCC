package com.example.farmia_mcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Locale;

public class Sensor_data extends AppCompatActivity {
    TextView progress, progress2, temptv, humidtv;
    Button b, b2;
    ProgressBar progressBar, progressbar2;
    private DatabaseReference reference;
    private static final int REQUEST_CODE = 143;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);

        b = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        progress = findViewById(R.id.progress_percentage);
        progressBar = findViewById(R.id.progress_bar);
        progress2 = findViewById(R.id.progress_percentage2);
        progressbar2 = findViewById(R.id.progress_bar2);
        temptv = findViewById(R.id.temperature);
        humidtv = findViewById(R.id.humid);

        textToSpeech = new TextToSpeech(Sensor_data.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        // speech code
        Intent intent2 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent2.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent2.putExtra(RecognizerIntent.EXTRA_PROMPT, "speak now");

        try {
            startActivityForResult(intent2,REQUEST_CODE);
        } catch(ActivityNotFoundException exception) {
            Toast.makeText(Sensor_data.this, exception.getMessage(),Toast.LENGTH_LONG).show();
        }
//
//        if (intent2.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent2, 10);
//        } else {
//            Toast.makeText(this, "your device doesn't support speech input", Toast.LENGTH_SHORT).show();
//        }
//        startActivityForResult(intent2, REQUEST_CODE);

        //------------------------------------------------------------------------------------------

        b2.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        checkconnection();

        b.setOnClickListener(v -> {
            firebase_sensordata();
        });
    }

    protected void firebase_sensordata() {
        reference = FirebaseDatabase.getInstance().getReference().child("DHT11");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String humidity = snapshot.child("Humidity").getValue().toString();
                String temperature = snapshot.child("Temperature").getValue().toString();

                String t = temperature.replaceAll("[^\\d.]", "");
                String h = humidity.replaceAll("[^\\d.]", "");

                float tm = Float.parseFloat(t);
                float ht = Float.parseFloat(h);

                progressBar.setProgress((int) tm);
                progress.setText(t + "C");

                progressbar2.setProgress((int) ht);
                progress2.setText(h + "%");
                // 23 - 27 degree celsius is best for ornamental fish tank
                if (tm > 27.00 || tm < 23.00) {
                    getnotification();
                    textToSpeech.speak("Immediately change the fish tank water, because temperature is not stable",TextToSpeech.QUEUE_FLUSH,null);
                }
                // humidity for chicken coop is 50 - 70%
                else if (ht > 70.00 || ht < 50.00) {
                    notification_chickencoop();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE: {
                if(resultCode == RESULT_OK && null!=data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (result.get(0).contains("show temperature and humidity")) {
                        firebase_sensordata();
                    } else if(result.get(0).length() == 0) {
                        Toast.makeText(Sensor_data.this,"no response from the user!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    }

    protected void getnotification() {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify_001");
            builder.setSmallIcon(R.drawable.farmialogo);
            builder.setContentTitle("FARMIA Alert");
            builder.setContentText("Immediately change the fish tank water");
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            String alertmsg = "Your fish tank water temperature is not stable, so immediately change the water";
            Intent intent = new Intent(Sensor_data.this, Notification.class);
            intent.putExtra("message", alertmsg);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager mn = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "notify_001";
                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
                mn.createNotificationChannel(channel);
                ;
                builder.setChannelId(channelId);
            }
            mn.notify(0, builder.build());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void notification_chickencoop() {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify_001");
            builder.setSmallIcon(R.drawable.farmialogo);
            builder.setContentTitle("FARMIA Alert");
            builder.setContentText("Immediately evacuate the chickens and chicks from the coop");
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            String alertmsg = "Your chicken coop humidity level is very low, you run the risk of creating a dry environment..";
            Intent intent = new Intent(Sensor_data.this, Notification.class);
            intent.putExtra("message", alertmsg);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager mn = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "notify_001";
                NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
                mn.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }
            mn.notify(0, builder.build());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void checkconnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (null != networkInfo) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                b.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                progressbar2.setVisibility(View.VISIBLE);
                progress.setVisibility(View.VISIBLE);
                progress2.setVisibility(View.VISIBLE);
                temptv.setVisibility(View.VISIBLE);
                humidtv.setVisibility(View.VISIBLE);
                TastyToast.makeText(getApplicationContext(), "Wifi Enabled !", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                b.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                progressbar2.setVisibility(View.VISIBLE);
                progress.setVisibility(View.VISIBLE);
                progress2.setVisibility(View.VISIBLE);
                temptv.setVisibility(View.VISIBLE);
                humidtv.setVisibility(View.VISIBLE);
                TastyToast.makeText(getApplicationContext(), "Online...", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
            }
        } else {
            b.setVisibility(View.INVISIBLE);
            b2.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            progressbar2.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.INVISIBLE);
            progress2.setVisibility(View.INVISIBLE);
            temptv.setVisibility(View.INVISIBLE);
            humidtv.setVisibility(View.INVISIBLE);
            TastyToast.makeText(getApplicationContext(), "No Internet Connection !", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (textToSpeech != null) {
//            textToSpeech.stop();
//            textToSpeech.shutdown();
//        }
//    }

}