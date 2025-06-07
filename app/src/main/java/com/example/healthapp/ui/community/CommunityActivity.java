package com.example.healthapp.ui.community;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.R;
import com.example.healthapp.repository.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;

public class CommunityActivity extends AppCompatActivity {
    private FloatingActionButton writeBtn;
    private String machineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community);

        writeBtn = findViewById(R.id.write_btn);
        writeBtn.setOnClickListener(v -> showPostTipDialogWithNickname());

        machineName = getIntent().getStringExtra("machine_name");
    }

    // 닉네임을 비동기로 불러와서 다이얼로그에 전달
    private void showPostTipDialogWithNickname() {
        UserRepository userRepository = new UserRepository();
        userRepository.getNickname(new UserRepository.NicknameCallback() {
            @Override
            public void onNicknameLoaded(String nickname) {
                showPostTipDialog(nickname);
            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(CommunityActivity.this, "닉네임을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 닉네임을 파라미터로 받는 다이얼로그 생성 메서드
    private void showPostTipDialog(String nickname) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.write_tip, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).setCancelable(true).create();

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSubmit = dialogView.findViewById(R.id.btn_submit);
        EditText etTipContent = dialogView.findViewById(R.id.et_tip_content);

        // TipDialogListener에 nickname, machineName 전달
        TipDialogListener listener = new TipDialogListener(dialog, etTipContent, this, nickname, machineName);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSubmit.setOnClickListener(listener);

        dialog.show();
    }
}
