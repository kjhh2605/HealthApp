package com.example.healthapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.R;
import com.example.healthapp.ui.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth; // 파이어베이스 인증
    private DatabaseReference mDatabase; // 실시간DB
    private EditText etEmail,etPw;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("GYMPT");

        btnLogin = findViewById(R.id.btn_login);
        etEmail = findViewById(R.id.et_email);
        etPw = findViewById(R.id.et_pw);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String password = etPw.getText().toString();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // 로그인 성공
                            Intent intent = new Intent(EmailLoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            String nickname = task.getClass().getName();
                            Toast.makeText(EmailLoginActivity.this, nickname+"님 환영합니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(EmailLoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}