package com.example.healthapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.R;
import com.google.android.gms.common.SignInButton;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        EditText etEmail = findViewById(R.id.et_email); // EditText id 확인!
        Button btnContinue = findViewById(R.id.btn_register);
        Button btnEmailLogin = findViewById(R.id.btn_email_login);
        SignInButton btnGoogleLogin = findViewById(R.id.btn_google_login);


        // 계속하기
        btnContinue.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) { // 입력칸 비어있으면
                Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            // 이메일을 회원가입 화면으로 전달
            Intent intent = new Intent(StartActivity.this, com.example.healthapp.ui.login.RegisterActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // 이메일 계정 로그인
        btnEmailLogin.setOnClickListener(v->{
            Intent intent = new Intent(StartActivity.this, EmailLoginActivity.class);
            startActivity(intent);
        });
    }
}