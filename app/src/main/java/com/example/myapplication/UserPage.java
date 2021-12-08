/*
package com.example.myapplication;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.SeekBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserPage extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증
    private DatabaseReference nDatabaseRef; //실시간 데이터베이스

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (user != null)
        {

            //String name = user.getDisplayName();
            String email = user.getEmail();
            //String phoneNum = user.getPhoneNumber();
            // Name, email address, and profile photo Url
            //TextView userName = findViewById(R.id.user_name);
            TextView userEmail = findViewById(R.id.user_email);
            //TextView userphoneNum=findViewById(R.id.user_phoneNum);




            //userName.setText(name);
            userEmail.setText(email);
            //userphoneNum.setText(phoneNum);

        }

    }








}
*/