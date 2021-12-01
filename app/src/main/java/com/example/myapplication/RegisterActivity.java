package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private EditText mEtEmail,mEtPwd,mEtPwd2,mEtName,mEtphoneNum;       //회원가입 입력필드
    private Button mBtnRegister;            //회원가입 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth =FirebaseAuth.getInstance();
        nDatabaseRef= FirebaseDatabase.getInstance().getReference("myapplication");//별칭

        mEtEmail=findViewById(R.id.et_email);
        mEtPwd=findViewById(R.id.et_pwd);
        mEtPwd2=findViewById(R.id.et_pwd2);
        mBtnRegister=findViewById(R.id.btn_register);
        mEtName=findViewById(R.id.et_name);
        mEtphoneNum=findViewById(R.id.et_phoneNum);




        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 처리 시작
                String strEmail = mEtEmail.getText().toString(); //문자열로 입력된 걸 가져옴
                String strPwd = mEtPwd.getText().toString(); //변수에 할당
                String strPwd2 = mEtPwd2.getText().toString(); //변수에 할당
                String strName = mEtName.getText().toString(); // 이름 할당
                String strPhoneNum = mEtphoneNum.getText().toString(); //폰번호 할당

                    //firebaseAuth 진행
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) //인증처리가 완료될떄
                        {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); //현재 유저 가져오기
                                UserAccount account = new UserAccount();
                                //데이터베이스에 삽입
                                account.setIdToken(firebaseUser.getUid()); //고윳값
                                account.setEmailId(firebaseUser.getEmail());
                                account.setPassword(strPwd);
                                account.setName(strName);
                                account.setPhoneNum(strPhoneNum);

                                //setValue :database에 insert(삽입)행위
                                nDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                Toast.makeText(RegisterActivity.this, "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show();
                                finish();
                            }else if(!(strEmail.contains("@")&&strEmail.contains("."))){
                            Toast.makeText(RegisterActivity.this, "이메일 형식으로 수정하세요.", Toast.LENGTH_SHORT).show();
                                TextView emailText =findViewById(R.id.et_emailtext);
                                emailText.setTextColor(0xAA1e6de0);//파랑색
                                emailText.setText("이메일 형식으로 수정하세요.");
                            }else if (!(strPwd.equals(strPwd2))) {
                            Toast.makeText(RegisterActivity.this, "비밀번호가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                            TextView pwdText=findViewById(R.id.et_pwdcheck);
                            pwdText.setTextColor(0xAA1e6de0);//파랑색
                            pwdText.setText("비밀번호가 일치 하지 않습니다.");

                            } else if(strPwd.length()<7){
                            Toast.makeText(RegisterActivity.this, "비밀번호 길이가 7보다 작습니다.", Toast.LENGTH_SHORT).show();
                                TextView pwdText=findViewById(R.id.et_pwdcheck);
                                pwdText.setTextColor(0xAAef484a);//빨강색
                                pwdText.setText("비밀번호 길이가 7보다 작습니다.");

                        }else {
                                Toast.makeText(RegisterActivity.this, "이미 있는 이메일입니다.", Toast.LENGTH_SHORT).show();
                                TextView emailText =findViewById(R.id.et_emailtext);
                                emailText.setTextColor(0xAAef484a);//빨강색
                                emailText.setText("이미 있는 이메일입니다.");
                            }
                        }


                    });

            }
        });


    }


}