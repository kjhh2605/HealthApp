package com.example.healthapp.repository;

import androidx.annotation.NonNull;
import com.example.healthapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {
    private final DatabaseReference userRef;

    public UserRepository() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.userRef = MyFirebaseDB.getDB().child("Users").child(uid);
    }
    public interface NicknameCallback {
        void onNicknameLoaded(String nickname);
        void onError(DatabaseError error);
    }

    public void getNickname(final NicknameCallback callback) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String nickname = user != null ? user.getNickName() : "";
                callback.onNicknameLoaded(nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }


    public interface UserInfoCallback {
        void onUserInfoLoaded(String nickname, String bench, String squat, String deadlift);
        void onError(DatabaseError error);
    }

    public void getUserInfo(final UserInfoCallback callback) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    String nickname = user.getNickName();
                    String bench = String.valueOf(user.getBench());
                    String squat = String.valueOf(user.getSquat());
                    String deadlift = String.valueOf(user.getDeadlift());
                    callback.onUserInfoLoaded(nickname, bench, squat, deadlift);
                } else {
                    callback.onUserInfoLoaded("", "", "", "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }

    public void saveUserInfo(String nickname, String bench, String squat, String deadlift) {
        User user = new User();
        user.setNickName(nickname);
        try {
            user.setBench(Integer.parseInt(bench));
            user.setSquat(Integer.parseInt(squat));
            user.setDeadlift(Integer.parseInt(deadlift));
        } catch (NumberFormatException e) {
            // 숫자 변환 실패 시 기본값(0) 또는 예외 처리
            user.setBench(0);
            user.setSquat(0);
            user.setDeadlift(0);
        }
        userRef.setValue(user);
    }
}
