package com.example.healthapp.ui.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.healthapp.R;

public class RecordViewHelper {

    private final Context context;
    private final LinearLayout recordContainer;
    private final PartCountUpdateListener partCountUpdateListener;

    public RecordViewHelper(Context context, LinearLayout recordContainer, PartCountUpdateListener listener) {
        this.context = context;
        this.recordContainer = recordContainer;
        this.partCountUpdateListener = listener;
    }
    public interface PartCountUpdateListener {
        void updatePartCount();
    }

    public View createRecordView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.record_item, recordContainer, false);
    }
    public void setupSpinners(View recordView) {
        Spinner spinner1 = recordView.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(context, R.array.machine_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        Spinner spinner2 = recordView.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context, R.array.part_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        Spinner spinner3 = recordView.findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(context, R.array.etc_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        LinearLayout recordContent = recordView.findViewById(R.id.recordContent);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                int color;
                if (selected.startsWith("등")) {
                    color = context.getResources().getColor(R.color.part_back_back, null);
                } else if (selected.startsWith("가슴")) {
                    color = context.getResources().getColor(R.color.part_back_chest, null);
                } else if (selected.startsWith("어깨")) {
                    color = context.getResources().getColor(R.color.part_back_shoulder, null);
                } else if (selected.equals("이두") || selected.equals("삼두")) {
                    color = context.getResources().getColor(R.color.part_back_arm, null);
                } else if (selected.startsWith("하체")) {
                    color = context.getResources().getColor(R.color.part_back_leg, null);
                } else {
                    color = context.getResources().getColor(R.color.part_back_default, null);
                }
                recordContent.setBackgroundColor(color);

                if (partCountUpdateListener != null) {
                    partCountUpdateListener.updatePartCount();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                int color = context.getResources().getColor(R.color.part_back_default, null);
                recordContent.setBackgroundColor(color);

                if (partCountUpdateListener != null) {
                    partCountUpdateListener.updatePartCount();
                }
            }
        });
    }

    public void animateRecordView(View recordView) {
        recordView.setTranslationY(-200f);
        recordView.setAlpha(0f);
        recordView.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(400)
                .start();
    }

    public void setupSwipeToDelete(View recordView) {
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
                                v.animate().translationX(v.getWidth()).alpha(0f).setDuration(200).withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                recordContainer.removeView(v);
                                                if (partCountUpdateListener != null) {
                                                    partCountUpdateListener.updatePartCount();
                                                }
                                            }
                                        }).start();
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

    public void setupAddSetButton(final View recordView) {
        View btnAddSet = recordView.findViewById(R.id.btnAddSet);
        if (btnAddSet != null) {
            btnAddSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 새 뷰 생성 및 세팅
                    View newRecordView = createRecordView();
                    setupSpinners(newRecordView);
                    setupAddSetButton(newRecordView); // 복제된 뷰에도 +버튼 동작 부여
                    animateRecordView(newRecordView);
                    setupSwipeToDelete(newRecordView);

                    // 값 복사
                    copyValues(recordView, newRecordView);

                    // 현재 뷰의 바로 아래에 추가
                    int index = recordContainer.indexOfChild(recordView);
                    recordContainer.addView(newRecordView, index + 1);

                    if (partCountUpdateListener != null) {
                        partCountUpdateListener.updatePartCount();
                    }
                }
            });
        }
    }

    public void copyValues(View parent, View toView) {
        // EditText 값 복사
        ((android.widget.EditText)toView.findViewById(R.id.etExerciseName))
                .setText(((android.widget.EditText)parent.findViewById(R.id.etExerciseName)).getText().toString());
        ((android.widget.EditText)toView.findViewById(R.id.etWeight))
                .setText(((android.widget.EditText)parent.findViewById(R.id.etWeight)).getText().toString());
        ((android.widget.EditText)toView.findViewById(R.id.etCount))
                .setText(((android.widget.EditText)parent.findViewById(R.id.etCount)).getText().toString());

        // Spinner 선택값 복사
        ((Spinner)toView.findViewById(R.id.spinner1))
                .setSelection(((Spinner)parent.findViewById(R.id.spinner1)).getSelectedItemPosition());
        ((Spinner)toView.findViewById(R.id.spinner2))
                .setSelection(((Spinner)parent.findViewById(R.id.spinner2)).getSelectedItemPosition());
        ((Spinner)toView.findViewById(R.id.spinner3))
                .setSelection(((Spinner)parent.findViewById(R.id.spinner3)).getSelectedItemPosition());
    }


}
