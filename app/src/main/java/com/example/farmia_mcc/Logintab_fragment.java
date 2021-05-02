package com.example.farmia_mcc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

public class Logintab_fragment extends Fragment {
    EditText mobile,password;
    Button login;
    TextView forgot;
    ImageView eye_icon;
    float v=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_logintab_fragment, container, false);

        mobile = viewGroup.findViewById(R.id.mobileno);
        password = viewGroup.findViewById(R.id.password);
        login = viewGroup.findViewById(R.id.loginbutton);
        forgot = viewGroup.findViewById(R.id.forgot_password);
        eye_icon = viewGroup.findViewById(R.id.eye_icon);

        mobile.setTranslationX(800);
        password.setTranslationX(800);
        login.setTranslationX(800);
        eye_icon.setTranslationX(800);

        mobile.setAlpha(v);
        password.setAlpha(v);
        login.setAlpha(v);
        eye_icon.setAlpha(v);

        mobile.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        eye_icon.animate().translationX(0).alpha(1).setStartDelay(1000).setStartDelay(900).start();

        eye_icon.setOnClickListener(v -> {
            if(password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            else {
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        login.setOnClickListener(v -> {
            Query checkuser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileno").equalTo(mobile.getText().toString());
            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        mobile.setError(null);

                        String systempassword = snapshot.child(mobile.getText().toString()).child("_password").getValue(String.class);
                        if(systempassword.equals(password.getText().toString())) {
                            password.setError(null);

                            Intent intent = new Intent(getContext(),Sensor_data.class);
                            startActivity(intent);
                            String mobileno = snapshot.child(mobile.getText().toString()).child("mobile").getValue(String.class);
                            String pass = snapshot.child(password.getText().toString()).child("_password").getValue(String.class);
                        }
                        else {
                            TastyToast.makeText(getActivity(), "Password doesn't match!", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                        }
                    } else {
                        TastyToast.makeText(getActivity(), "No such User exists !", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    TastyToast.makeText(getActivity(), error.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
            });
        });

        return viewGroup;
    }
}