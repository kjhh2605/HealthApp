package com.example.healthapp.ui.community;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthapp.R;
import com.example.healthapp.model.UserTip;
import com.example.healthapp.repository.MyFirebaseDB;
import com.google.firebase.database.DatabaseReference;


public class TipDialogListener implements View.OnClickListener {
    private final AlertDialog dialog;
    private final EditText etTipContent;
    private final Context context;
    private final String nickname;
    private final String workout, machine, etc;

    public TipDialogListener(AlertDialog dialog, EditText etTipContent, Context context, String nickname,String workout,String machine,String etc) {
        this.dialog = dialog;
        this.etTipContent = etTipContent;
        this.context = context;
        this.nickname = nickname;
        this.workout = workout;
        this.machine = machine;
        this.etc = etc;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel) {
            dialog.dismiss();
        } else if (id == R.id.btn_submit) {
            String tip = etTipContent.getText().toString().trim();
            if (tip.isEmpty()) {
                Toast.makeText(context, "내용을 입력하세요!", Toast.LENGTH_SHORT).show();
                return;
            }else{
                UserTip userTip = new UserTip(nickname, workout, machine, tip, etc);
                DatabaseReference ref = MyFirebaseDB.getDB().child("Community").child(workout);
                ref.push().setValue(userTip)
                        .addOnSuccessListener(aVoid -> Log.d("Firebase", "저장 성공"))
                        .addOnFailureListener(e -> Log.e("Firebase", "저장 실패: " + e.getMessage()));
            }
            dialog.dismiss();
        }
    }
}

