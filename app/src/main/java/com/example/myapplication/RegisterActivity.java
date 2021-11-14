package com.example.myapplication;

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

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증
    private DatabaseReference nDatabaseRef; //실시간 데이터베이스
    private EditText mEtEmail,mEtPwd,mEtName,mEtPhoneNum;       //회원가입 입력필드
    private Button mBtnRegister;            //회원가입 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth =FirebaseAuth.getInstance();
        nDatabaseRef= FirebaseDatabase.getInstance().getReference("myapplication");//별칭

        mEtName=findViewById(R.id.et_name);
        mEtPhoneNum=findViewById(R.id.et_phoneNum);
        mEtEmail=findViewById(R.id.et_email);
        mEtPwd=findViewById(R.id.et_pwd);


        mBtnRegister=findViewById(R.id.btn_register);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 처리 시작
                String strEmail =mEtEmail.getText().toString(); //문자열로 입력된 걸 가져옴
                String strPwd=mEtPwd.getText().toString();
                String strName=mEtName.getText().toString();
                String strPhoneNum=mEtPhoneNum.getText().toString();

                //firebaseAuth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account =new UserAccount();
                            account.setIdToken(firebaseUser.getUid()); //고윳값
                            account.setEmailId(firebaseUser.getEmail());
                      //     account.setName(firebaseUser.getDisplayName());
                        //    account.setPhoneNum(firebaseUser.getPhoneNumber());
                            account.setPassword(strPwd);

                            //setValue :database에 insert(삽입)행위
                            nDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText (RegisterActivity.this,"회원가입에 성공했습니다",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText (RegisterActivity.this,"회원가입에 실패했습니다",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });


    }


}