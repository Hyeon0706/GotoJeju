
package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.SeekBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//아이디가져오기
import static com.example.myapplication.LoginActivity.id;
import static com.example.myapplication.LoginActivity.pw;

import java.util.ArrayList;


public class UserPage extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     //파이어베이스 인증
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //파이어베이스 로그인 유저정보

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserAccount> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference; //실시간 데이터베이스




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage); //연결 중요

        recyclerView=findViewById(R.id.recyclerview);//아디 연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this); //자동입력
        recyclerView.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>(); //UserAccount 객체를 담을 어레이 리스트

        database=FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스  연동

        databaseReference=database.getReference("UserAccount");//DB 테이블 연결

        databaseReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {


                    Log.d("firebase", String.valueOf(task.getResult().getValue()));



                }
            }
        });


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존배열리스가 존재하지 않게 초기화
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){ //반복문으로 데이터 list 추출한다.
                    UserAccount userAccount = snapshot.getValue(UserAccount.class); //만들었던 User 객체에 담는다.
                    arrayList.add(userAccount); //담은데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); //리스트정장및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비를 가져오던중에러발생시
                Log.e("UserPage",String.valueOf(error.toException()));
            }
        });

        adapter= new CustomAdapter(arrayList,this); //연결
        recyclerView.setAdapter(adapter);//리사이클러뷰에 어댑터 연결;

        mFirebaseAuth = FirebaseAuth.getInstance();
        /*
        TextView userEmail = (TextView)findViewById(R.id.user_email);
        TextView userName = (TextView)findViewById(R.id.user_name);
        TextView userPhoneNum = (TextView)findViewById(R.id.user_phoneNum);


        String email =(String) user.getEmail();
        String name=(String)user.getDisplayName();
        userEmail.setTextColor(0xAAef484a);//빨강색
        userName.setTextColor(0xAAef484a);//빨강색
        userPhoneNum.setTextColor(0xAAef484a);//빨강색

        userEmail.setText(email);
        userName.setText(name);
        userPhoneNum.setText(email);
    */
    }
}
