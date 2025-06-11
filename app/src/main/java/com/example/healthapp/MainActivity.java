package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.ui.HomeActivity;
import com.example.healthapp.ui.login.StartActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 로그인 여부 확인
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // 로그인 상태: HomeActivity로 이동
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            // 로그아웃 상태: StartActivity로 이동
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }

    }
}
