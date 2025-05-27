package com.example.healthapp.ui.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class WorkoutFragment extends Fragment {

    private LinearLayout recordContainer;
    private FloatingActionButton btnAddRecord;
    private int recordCount = 1;

    public WorkoutFragment() {
        super(R.layout.fragment_workout); // Fragment에 레이아웃 연결
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordContainer = view.findViewById(R.id.recordContainer);
        btnAddRecord = view.findViewById(R.id.btnAddRecord);

        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWorkoutRecord();
            }
        });
    }

    private void addWorkoutRecord() {
        View recordView = createRecordView();
        setupSpinners(recordView);
        setupSwipeToDelete(recordView);
        animateRecordView(recordView);
        recordContainer.addView(recordView);
    }

    // 1. record_item.xml inflate
    private View createRecordView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return inflater.inflate(R.layout.record_item, recordContainer, false);
    }

    // 2. Spinner 등 뷰에 문자열 배열 연결
    private void setupSpinners(View recordView) {
        Spinner spinner1 = recordView.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(), R.array.machine_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        Spinner spinner2 = recordView.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.part_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        Spinner spinner3 = recordView.findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.etc_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        LinearLayout recordContent = recordView.findViewById(R.id.recordContent);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                int color;
                if (selected.startsWith("등")) {
                    color = getResources().getColor(R.color.part_back_back, null);
                } else if (selected.startsWith("가슴")) {
                    color = getResources().getColor(R.color.part_back_chest, null);
                } else if (selected.startsWith("어깨")) {
                    color = getResources().getColor(R.color.part_back_shoulder, null);
                } else if (selected.equals("이두") || selected.equals("삼두")) {
                    color = getResources().getColor(R.color.part_back_arm, null);
                } else if (selected.startsWith("하체")) {
                    color = getResources().getColor(R.color.part_back_leg, null);
                } else {
                    color = getResources().getColor(R.color.part_back_default, null);
                }
                // 내부 LinearLayout 배경색 변경
                recordContent.setBackgroundColor(color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                int color = getResources().getColor(R.color.part_back_default, null);
                recordContent.setBackgroundColor(color);
            }
        });
    }


    // 애니메이션 적용
    private void animateRecordView(View recordView) {
        recordView.setTranslationY(-200f);
        recordView.setAlpha(0f);
        recordView.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(400)
                .start();
    }

    // 스와이프 삭제 기능
    private void setupSwipeToDelete(View recordView) {
        recordView.setOnTouchListener(new View.OnTouchListener() {
            float downX = 0;
            boolean swiped = false;

            @Override
            public boolean onTouch(View v, android.view.MotionEvent event) {
                switch (event.getAction()) {
                    case android.view.MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        swiped = false;
                        return true;
                    case android.view.MotionEvent.ACTION_MOVE:
                        float moveX = event.getX();
                        float deltaX = moveX - downX;
                        if (deltaX > 0) {
                            v.setTranslationX(deltaX);
                            if (deltaX > 300 && !swiped) {
                                swiped = true;
                                v.animate()
                                        .translationX(v.getWidth())
                                        .alpha(0f)
                                        .setDuration(200)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                recordContainer.removeView(v);
                                            }
                                        })
                                        .start();
                            }
                        }
                        return true;
                    case android.view.MotionEvent.ACTION_UP:
                    case android.view.MotionEvent.ACTION_CANCEL:
                        if (!swiped) {
                            v.animate().translationX(0).setDuration(150).start();
                        }
                        return true;
                }
                return false;
            }
        });
    }


}
