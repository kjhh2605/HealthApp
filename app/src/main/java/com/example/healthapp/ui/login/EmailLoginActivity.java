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
import com.example.healthapp.repository.MyFirebaseDB;
import com.example.healthapp.repository.UserRepository;
import com.example.healthapp.ui.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

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
        mDatabase = MyFirebaseDB.getDB();

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
                            // User 엔티티에서 닉네임 가져오기
                            UserRepository userRepository = new UserRepository();
                            userRepository.getNickname(new UserRepository.NicknameCallback() {
                                @Override
                                public void onNicknameLoaded(String nickname) {
                                    Toast.makeText(EmailLoginActivity.this, nickname + "님 환영합니다", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(EmailLoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(DatabaseError error) {
                                    Toast.makeText(EmailLoginActivity.this, "닉네임 불러오기 실패", Toast.LENGTH_SHORT).show();

                                    // 닉네임 없이도 홈 화면 이동은 가능하게 설정
                                    Intent intent = new Intent(EmailLoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
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