package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 예시: 로그인 상태 체크 (여기서는 무조건 로그인 화면으로 이동)
        boolean isLoggedIn = false; // 실제로는 SharedPreferences 등으로 체크

        if (!isLoggedIn) {
            // 로그인 화면으로 이동
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // MainActivity 종료 (뒤로가기 시 로그인화면 안 나오게)
            return;
        }
    }
}
