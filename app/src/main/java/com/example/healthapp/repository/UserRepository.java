package com.example.healthapp.repository;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class UserRepository {
    private final DatabaseReference userRef;

    //
    public UserRepository() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.userRef = MyFirebaseDB.getDB().child("Users").child(uid);;
    }

    // 닉네임 콜백 인터페이스
    public interface NicknameCallback {
        void onNicknameLoaded(String nickname);
        void onError(DatabaseError error);
    }

    // 닉네임 가져오기 메서드
    public void getNickname(final NicknameCallback callback) {
        userRef.child("nickName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nickname = snapshot.getValue(String.class);
                callback.onNicknameLoaded(nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error);
            }
        });
    }
}
