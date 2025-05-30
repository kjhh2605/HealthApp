package com.example.healthapp.ui.workout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthapp.R;
import com.example.healthapp.model.WorkoutRecord;
import com.example.healthapp.repository.MyFirebaseDB;
import com.example.healthapp.repository.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class WorkoutFragment extends Fragment {
    private UserRepository userRepository;
    private TextView tvCountBack, tvCountChest, tvCountLeg, tvCountArm, tvCountShoulder;
    private LinearLayout recordContainer;
    private FloatingActionButton btnAddRecord;
    private RecordViewHelper viewHelper;

    private Button btnSaveRecords;
    public WorkoutFragment() {
        super(R.layout.fragment_workout); // Fragment에 레이아웃 연결
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCountBack = view.findViewById(R.id.tvCountBack);
        tvCountChest = view.findViewById(R.id.tvCountChest);
        tvCountLeg = view.findViewById(R.id.tvCountLeg);
        tvCountArm = view.findViewById(R.id.tvCountArm);
        tvCountArm = view.findViewById(R.id.tvCountShoulder);

        recordContainer = view.findViewById(R.id.recordContainer);
        btnAddRecord = view.findViewById(R.id.btnAddRecord);

        btnSaveRecords = view.findViewById(R.id.btnSaveRecords);

        viewHelper = new RecordViewHelper(getContext(), recordContainer, new RecordViewHelper.PartCountUpdateListener() {
            @Override
            public void updatePartCount() {
                updatePartCounts();
            }
        });

        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkoutRecordView();
            }
        });

        btnSaveRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("운동 기록 저장")
                        .setMessage("정말로 운동 기록을 저장하시겠습니까?\n저장 후에는 수정할 수 없습니다.")
                        .setPositiveButton("확인", (dialog, which) -> {
                            btnSaveRecords.setEnabled(false); // 버튼 비활성화
                            btnSaveRecords.setBackgroundColor(Color.GRAY);
                            saveAllWorkoutRecords();
                            removeAllWorkoutRecordView();
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });

    }

    private void addWorkoutRecordView() {
        View recordView = viewHelper.createRecordView();
        viewHelper.setupSpinners(recordView);
        viewHelper.setupSwipeToDelete(recordView);
        viewHelper.animateRecordView(recordView);
        viewHelper.setupAddSetButton(recordView);
        recordContainer.addView(recordView);
    }

    private void updatePartCounts() {
        int countBack = 0, countChest = 0, countLeg = 0, countArm = 0 , countShoulder=0;
        for (int i = 0; i < recordContainer.getChildCount(); i++) {
            View recordView = recordContainer.getChildAt(i);
            Spinner spinner2 = recordView.findViewById(R.id.spinner2);
            if (spinner2 != null && spinner2.getSelectedItem() != null) {
                String selected = spinner2.getSelectedItem().toString();
                if (selected.startsWith("등")) {
                    countBack++;
                } else if (selected.startsWith("가슴")) {
                    countChest++;
                } else if (selected.startsWith("하체")) {
                    countLeg++;
                } else if (selected.equals("이두") || selected.equals("삼두")) {
                    countArm++;
                } else if (selected.startsWith("어깨")){
                    countShoulder++;
                }
            }
        }
        tvCountBack.setText("등: " + countBack);
        tvCountChest.setText("가슴: " + countChest);
        tvCountLeg.setText("하체: " + countLeg);
        tvCountArm.setText("팔: " + countArm);
        tvCountArm.setText("어깨: " + countShoulder);
    }

    private void saveAllWorkoutRecords() {
        List<WorkoutRecord> recordList = new ArrayList<>();

        for (int i = 0; i < recordContainer.getChildCount(); i++) {
            View recordView = recordContainer.getChildAt(i);

            Spinner spinner0 = recordView.findViewById(R.id.spinner0);
            Spinner spinner1 = recordView.findViewById(R.id.spinner1);
            Spinner spinner2 = recordView.findViewById(R.id.spinner2);
            Spinner spinner3 = recordView.findViewById(R.id.spinner3);
            EditText etWeight = recordView.findViewById(R.id.etWeight);
            EditText etCount = recordView.findViewById(R.id.etCount);

            if (spinner0 == null || spinner1 == null || spinner2 == null || etWeight == null || etCount == null)
                continue;

            String workout = spinner0.getSelectedItem() != null ? spinner0.getSelectedItem().toString() : "";
            String machine = spinner1.getSelectedItem() != null ? spinner1.getSelectedItem().toString() : "";
            String part = spinner2.getSelectedItem() != null ? spinner2.getSelectedItem().toString() : "";
            String etc = spinner3.getSelectedItem() != null ? spinner3.getSelectedItem().toString() : "";

            if(workout.equals("선택안함"))
                continue;

            double weight = 0.0;
            int count = 0;

            try {
                weight = Double.parseDouble(etWeight.getText().toString());
            } catch (Exception ignored) {}

            try {
                count = Integer.parseInt(etCount.getText().toString());
            } catch (Exception ignored) {}

            long timestamp = System.currentTimeMillis();

            WorkoutRecord record = new WorkoutRecord(workout,machine,part,etc, weight, count, timestamp);
            recordList.add(record);
        }

        saveRecordsToFirebase(recordList);
    }

    private void saveRecordsToFirebase(List<WorkoutRecord> recordList) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String dateKey = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        DatabaseReference dbRef = MyFirebaseDB.getDB()
                .child("Users")
                .child(uid)
                .child("workoutRecords")
                .child(dateKey);

        dbRef.setValue(recordList)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "운동 기록 저장 완료!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void removeAllWorkoutRecordView(){
        recordContainer.removeAllViews();
        updatePartCounts();
    }
}
