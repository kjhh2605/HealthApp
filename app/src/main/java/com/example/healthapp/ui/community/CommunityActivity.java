package com.example.healthapp.ui.community;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.adapter.CommunityAdapter;
import com.example.healthapp.model.UserTip;
import com.example.healthapp.repository.MyFirebaseDB;
import com.example.healthapp.repository.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunityActivity extends AppCompatActivity {
    private FloatingActionButton writeBtn;
    private String selectedWorkout,selectedMachine,etc;
    private RecyclerView recyclerView;
    private CommunityAdapter adapter;
    private ArrayList<UserTip> tipList;
    private HashMap<UserTip,String> keyMap;
    private DatabaseReference myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community);

        writeBtn = findViewById(R.id.write_btn);
        writeBtn.setOnClickListener(v -> showPostTipDialogWithNickname());

        selectedWorkout = getIntent().getStringExtra("workout");
        selectedMachine = getIntent().getStringExtra("machine");
        etc = getIntent().getStringExtra("etc");

        myDB = MyFirebaseDB.getDB().child("Community").child(selectedWorkout);
        recyclerView = findViewById(R.id.recycler_tip);
        tipList = new ArrayList<>();
        keyMap = new HashMap<>();

        myDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tipList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    UserTip tip = data.getValue(UserTip.class);
                    if (tip != null) {
                        tipList.add(tip);
                        keyMap.put(tip, data.getKey());
                    }
                }
                // 좋아요 순으로 정렬 -> 같으면 싫어요 적은 순
                tipList.sort((a, b) -> {
                    int cmp = Integer.compare(b.getLikes(), a.getLikes());
                    if (cmp != 0) return cmp;
                    return Integer.compare(a.getDislikes(), b.getDislikes());
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommunityActivity.this, "작성된 게시글이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new CommunityAdapter(tipList, this, new CommunityAdapter.OnTipClickListener() {
            // 좋아요
            @Override
            public void onLikeClick(UserTip tip) {
                String tipKey = keyMap.get(tip);
                if (tipKey != null) {
                    myDB.child(tipKey).child("likes").setValue(tip.getLikes() + 1);
                }
            }
            // 싫어요
            @Override
            public void onDislikeClick(UserTip tip) {
                String tipKey = keyMap.get(tip);
                if (tipKey != null) {
                    myDB.child(tipKey).child("dislikes").setValue(tip.getDislikes() + 1);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        // TipDialogListener에 nickname, 선택 운동 정보 전달
        TipDialogListener listener = new TipDialogListener(dialog, etTipContent, this, nickname, selectedWorkout,selectedMachine,etc);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSubmit.setOnClickListener(listener);

        dialog.show();
    }
}
