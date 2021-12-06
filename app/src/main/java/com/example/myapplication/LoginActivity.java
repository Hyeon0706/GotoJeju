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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증
    private DatabaseReference nDatabaseRef; //실시간 데이터베이스
    private EditText mEtEmail,mEtPwd;       //회원가입 입력필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
       nDatabaseRef= FirebaseDatabase.getInstance().getReference("myapplication");//별칭


        mEtEmail=findViewById(R.id.et_email);
        mEtPwd=findViewById(R.id.et_pwd);


        Button btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인 요청
                String strEmail =mEtEmail.getText().toString(); //문자열로 입력된 걸 가져옴
                String strPwd=mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //로그인 성공
                            Intent intent1 =new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent1);
                //            finish(); //현재엑티비티 파괴
                        }else{
                            Toast.makeText(LoginActivity.this, "로그인실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button btn_findPwd;
        btn_findPwd=findViewById(R.id.btn_findPwd);
        btn_findPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,resetPwdActivity.class);
                startActivity(intent);

            }
        });


        Button btn_register;
        btn_register = findViewById(R.id.btn_resetPwd);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 버튼클릭시 화면으로 이동
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);

                startActivity(intent);
            }
        });
    }
}