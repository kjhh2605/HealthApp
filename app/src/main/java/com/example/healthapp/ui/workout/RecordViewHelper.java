package com.example.healthapp.ui.workout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.healthapp.R;
import com.example.healthapp.ui.community.CommunityActivity;

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

        Spinner spinner0 = recordView.findViewById(R.id.spinner0);
        ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(context, R.array.workout_array, android.R.layout.simple_spinner_item);
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner0.setAdapter(adapter0);

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

    public void setupTouchAndSwipe(final View recordView, final Context context) {
        recordView.setOnTouchListener(new View.OnTouchListener() {
            float downX = 0;
            float downY = 0;
            boolean swiped = false;
            boolean moved = false;
            final float SWIPE_THRESHOLD = 120; // 스와이프로 간주할 거리(dp)

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        swiped = false;
                        moved = false;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float moveX = event.getX();
                        float deltaX = moveX - downX;
                        if (Math.abs(deltaX) > 10) moved = true; // 10px 이상 움직이면 moved=true

                        if (deltaX > SWIPE_THRESHOLD && !swiped) {
                            // 오른쪽으로 스와이프: 삭제
                            swiped = true;
                            v.animate().translationX(v.getWidth()).alpha(0f).setDuration(200)
                                    .withEndAction(() -> {
                                        ((ViewGroup) v.getParent()).removeView(v);
                                        // partCountUpdateListener 등 콜백 호출
                                    }).start();
                        } else {
                            v.setTranslationX(deltaX); // 스와이프 중 이동 효과
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!swiped) {
                            v.animate().translationX(0).setDuration(150).start();
                            if (!moved) {
                                // 클릭(터치)만 감지 → 게시판 이동
                                Intent intent = new Intent(context, CommunityActivity.class);
                                context.startActivity(intent);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        v.animate().translationX(0).setDuration(150).start();
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
                    setupTouchAndSwipe(newRecordView,context);

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
        ((android.widget.EditText)toView.findViewById(R.id.etWeight))
                .setText(((android.widget.EditText)parent.findViewById(R.id.etWeight)).getText().toString());
        ((android.widget.EditText)toView.findViewById(R.id.etCount))
                .setText(((android.widget.EditText)parent.findViewById(R.id.etCount)).getText().toString());

        // Spinner 선택값 복사
        ((Spinner)toView.findViewById(R.id.spinner0))
                .setSelection(((Spinner)parent.findViewById(R.id.spinner0)).getSelectedItemPosition());
        ((Spinner)toView.findViewById(R.id.spinner1))
                .setSelection(((Spinner)parent.findViewById(R.id.spinner1)).getSelectedItemPosition());
        ((Spinner)toView.findViewById(R.id.spinner2))
                .setSelection(((Spinner)parent.findViewById(R.id.spinner2)).getSelectedItemPosition());
        ((Spinner)toView.findViewById(R.id.spinner3))
                .setSelection(((Spinner)parent.findViewById(R.id.spinner3)).getSelectedItemPosition());
    }


}
