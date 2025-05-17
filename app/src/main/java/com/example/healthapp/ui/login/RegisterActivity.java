package com.example.healthapp.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth; // 파이어베이스 인증
    private DatabaseReference mDatabase; // 실시간DB
    private String email;
    private EditText etNickName,etPw, etPwConfirm;
    private EditText bench, squat, deadlift;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("GYMPT");

        // LoginActivity에서 전달한 이메일 값 받기
        email = getIntent().getStringExtra("email");
        etNickName = findViewById(R.id.et_nickname);
        etPw = findViewById(R.id.et_pw);
        etPwConfirm = findViewById(R.id.et_pw_confirm);
        btnRegister = findViewById(R.id.btn_register);
        bench = findViewById(R.id.et_bench);
        squat = findViewById(R.id.et_squat);
        deadlift = findViewById(R.id.et_deadlift);

        // onCreate() 내부에 추가
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = etPw.getText().toString();
                String pwConfirm = etPwConfirm.getText().toString();
                btnRegister.setEnabled(!pw.isEmpty() && pw.equals(pwConfirm));
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
        etPw.addTextChangedListener(watcher);
        etPwConfirm.addTextChangedListener(watcher);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 처리 시작
                String password = etPw.getText().toString().trim();
                // 계정 생성
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 회원가입 성공
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setUserToken(user.getUid());
                            account.setNickName(etNickName.getText().toString());
                            account.setEmail(user.getEmail());
                            account.setPassword(password);
                            account.setBench(Integer.parseInt(bench.getText().toString()));
                            account.setSquat(Integer.parseInt(squat.getText().toString()));
                            account.setDeadlift(Integer.parseInt(deadlift.getText().toString()));
                            // Users 테이블에 저장
                            mDatabase.child("Users").child(user.getUid()).setValue(account);
                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
