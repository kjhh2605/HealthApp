package com.example.healthapp.ui.workout;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class WorkoutFragment extends Fragment {
    private TextView tvCountBack, tvCountChest, tvCountLeg, tvCountArm, tvCountShoulder;
    private LinearLayout recordContainer;
    private FloatingActionButton btnAddRecord;
    private RecordViewHelper viewHelper;

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
        viewHelper = new RecordViewHelper(getContext(), recordContainer, new RecordViewHelper.PartCountUpdateListener() {
            @Override
            public void updatePartCount() {
                updatePartCounts();
            }
        });

        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkoutRecord();
            }
        });
    }

    private void addWorkoutRecord() {
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

}
