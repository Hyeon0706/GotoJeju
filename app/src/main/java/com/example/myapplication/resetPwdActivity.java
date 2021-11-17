package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class resetPwdActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증
    private DatabaseReference nDatabaseRef; //실시간 데이터베이스
    private EditText mEtEmail;       //회원가입 입력필드




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd);

        mFirebaseAuth = FirebaseAuth.getInstance();



        mEtEmail=findViewById(R.id.et_email);


        Button btn_restPwd=findViewById(R.id.btn_resetPwd);
        btn_restPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인 요청
                String strEmail =mEtEmail.getText().toString(); //문자열로 입력된 걸 가져옴
                mFirebaseAuth.sendPasswordResetEmail(strEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(resetPwdActivity.this, "이메일이 전송되었습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    }
                });






            }
        });
    }
    }

