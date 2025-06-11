package com.example.healthapp.ui.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.R;
import com.example.healthapp.repository.UserRepository;
import com.example.healthapp.ui.login.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

public class ProfileActivity extends AppCompatActivity {
    private EditText etNickname, etSquat, etBench, etDeadlift;
    private Button btnModify, btnLogout;
    private boolean isEditMode = false;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        etNickname = findViewById(R.id.et_nickname);
        etSquat = findViewById(R.id.et_squat);
        etBench = findViewById(R.id.et_bench);
        etDeadlift = findViewById(R.id.et_deadlift);
        btnModify = findViewById(R.id.btn_modify);
        btnLogout = findViewById(R.id.btn_logout);

        userRepository = new UserRepository();

        loadUserInfo();

        btnModify.setOnClickListener(v -> {
            if (!isEditMode) {
                setEditMode(true);
                btnModify.setText("저장하기");
            } else {
                saveUserInfo();
                setEditMode(false);
                btnModify.setText("수정하기");
            }
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfileActivity.this, StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserInfo() {
        userRepository.getUserInfo(new UserRepository.UserInfoCallback() {
            @Override
            public void onUserInfoLoaded(String nickname, String bench, String squat, String deadlift) {
                etNickname.setText(nickname);
                etBench.setText(bench);
                etSquat.setText(squat);
                etDeadlift.setText(deadlift);
            }
            @Override
            public void onError(DatabaseError error) {
            }
        });
    }

    private void saveUserInfo() {
        String nickname = etNickname.getText().toString();
        String bench = etBench.getText().toString();
        String squat = etSquat.getText().toString();
        String deadlift = etDeadlift.getText().toString();
        userRepository.saveUserInfo(nickname, bench, squat, deadlift);
    }

    private void setEditMode(boolean isEdit) {
        isEditMode = isEdit;
        etNickname.setEnabled(isEdit);
        etSquat.setEnabled(isEdit);
        etBench.setEnabled(isEdit);
        etDeadlift.setEnabled(isEdit);
    }
}
