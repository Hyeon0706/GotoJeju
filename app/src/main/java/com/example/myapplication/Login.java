package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText idInput = (EditText) findViewById(R.id.emailInput);
        EditText passInput = (EditText) findViewById(R.id.passwordInput);
        Button btn = (Button) findViewById(R.id.loginButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idInput.getText().equals("kth")&&passInput.getText().equals("9876")){
                    Toast.makeText(Login.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Login.this, "로그인 실패...!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
