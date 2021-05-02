package com.example.farmia_mcc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;

import static androidx.core.content.ContextCompat.getSystemService;

public class Signuptab_fragment extends Fragment {

    EditText user, mobile, email, password;
    Button signup;
    TextView forgot;
    ImageView eye_icon;
    float v = 0;
    boolean isallcheckfields = false;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_signuptab_fragment, container, false);

        user = viewGroup.findViewById(R.id.username);
        mobile = viewGroup.findViewById(R.id.signup_mobileno);
        email = viewGroup.findViewById(R.id.email);
        password = viewGroup.findViewById(R.id.signup_password);
        signup = viewGroup.findViewById(R.id.signupbutton);
        forgot = viewGroup.findViewById(R.id.forgot_password);
        eye_icon = viewGroup.findViewById(R.id.eye_icon);

        eye_icon.setOnClickListener(v -> {
            if(password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            else {
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(email.getText().toString().matches(emailPattern) && s.length() > 0) {
                }
                else {
                    email.setError("Invalid email address");
                }
            }
        });

        signup.setOnClickListener(v -> {

            isallcheckfields = checkallfields();
            if(isallcheckfields) {
                FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
                DatabaseReference reference = rootnode.getReference("Users");

                Userdata userdata = new Userdata(user.getText().toString(), email.getText().toString(), mobile.getText().toString(), password.getText().toString());
                reference.child(mobile.getText().toString()).setValue(userdata);
                TastyToast.makeText(getContext(), "Success", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                Intent intent = new Intent(getContext(), Loading_screen.class);
                startActivity(intent);
            }
        });

        user.setTranslationX(800);
        mobile.setTranslationX(800);
        email.setTranslationX(800);
        password.setTranslationX(800);
        signup.setTranslationX(800);
        eye_icon.setTranslationX(800);

        user.setAlpha(v);
        mobile.setAlpha(v);
        email.setAlpha(v);
        password.setAlpha(v);
        signup.setAlpha(v);
        eye_icon.setAlpha(v);

        user.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        mobile.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(800).start();
        eye_icon.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1000).start();

        return viewGroup;
    }

    private boolean checkallfields() {

        if (user.getText().toString().length() == 0) {
            user.setError("Username should not be empty");
            return false;
        } else if (!user.getText().toString().matches("[a-zA-Z ]+")) {
            user.setError("Enter only characters");
            return false;
        }

        if (mobile.getText().toString().length() == 0) {
            mobile.setError("Mobile number should not be empty");
            return false;
        } else if (mobile.getText().toString().matches("[a-zA-Z ]+")) {
            mobile.setError("Only numbers are allowed");
            return false;
        }
//
//        if(email.getText().toString().length() == 0) {
//            email.setError("Email should not be empty");
//            return false;
//        } else if(email.getText().toString().trim().matches(emailPattern)) {
//            email.setError("Invalid email address");
//            return false;
//        }

        if(password.getText().toString().length() == 0) {
            password.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

}