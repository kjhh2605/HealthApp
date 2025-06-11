package com.example.healthapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.adapter.CommunityAdapter;
import com.example.healthapp.adapter.PlayListAdapter;
import com.example.healthapp.model.UserTip;
import com.example.healthapp.model.YoutubeLink;
import com.example.healthapp.repository.MyFirebaseDB;
import com.example.healthapp.ui.home.chatbot.ChatBotDialogFragment;
import com.example.healthapp.ui.home.profile.ProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private CircleImageView profile;
    private List<YoutubeLink> playList;
    private RecyclerView recyclerPlayList;
    private RecyclerView recyclerHot;
    private PlayListAdapter playListAdapter;
    private CommunityAdapter communityAdapter;
    private List<UserTip> hotTipList;
    private DatabaseReference communityRef;
    private FloatingActionButton fabChat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        profile = view.findViewById(R.id.profile);
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        // 플레이리스트
        recyclerPlayList = view.findViewById(R.id.card_recycler);
        loadPlayList();
        playListAdapter = new PlayListAdapter(playList, requireContext());
        recyclerPlayList.setAdapter(playListAdapter);
        recyclerPlayList.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );


        // 인기글
        recyclerHot = view.findViewById(R.id.recycler_hot);
        hotTipList = new ArrayList<>();
        communityAdapter = new CommunityAdapter(hotTipList, requireContext(), new CommunityAdapter.OnTipClickListener() {
            // 홈 화면에서는 읽기만 가능
            @Override
            public void onLikeClick(UserTip tip) {
            }
            @Override
            public void onDislikeClick(UserTip tip) {
            }
        });
        recyclerHot.setAdapter(communityAdapter);
        recyclerHot.setLayoutManager(new LinearLayoutManager(requireContext()));
        communityRef = MyFirebaseDB.getDB().child("Community");
        loadHotTips();

        fabChat = view.findViewById(R.id.fab_chat);
        fabChat.setOnClickListener(v -> {
            ChatBotDialogFragment dialog = new ChatBotDialogFragment();
            dialog.show(getChildFragmentManager(), "chatbot");
        });


        return view;
    }
    private void loadHotTips() {
        communityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserTip> allTips = new ArrayList<>();
                for (DataSnapshot workoutSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot tipSnapshot : workoutSnapshot.getChildren()) {
                        UserTip tip = tipSnapshot.getValue(UserTip.class);
                        if (tip != null) {
                            allTips.add(tip);
                        }
                    }
                }
                // 좋아요 순으로 정렬 (내림차순)
                Collections.sort(allTips, (a, b) -> Integer.compare(b.getLikes(), a.getLikes()));
                // 상위 5개
                hotTipList.clear();
                int count = Math.min(allTips.size(), 5);
                hotTipList.addAll(allTips.subList(0, count));
                communityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "인기글을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadPlayList(){
        playList = new ArrayList<>();
        playList.add(new YoutubeLink( "https://img.youtube.com/vi/I2mo7a9XHnM/hqdefault.jpg", "https://www.youtube.com/watch?v=I2mo7a9XHnM"));
        playList.add(new YoutubeLink("https://img.youtube.com/vi/WnrIX9Ak1wA/hqdefault.jpg", "https://www.youtube.com/watch?v=WnrIX9Ak1wA"));
        playList.add(new YoutubeLink("https://img.youtube.com/vi/PN7lP0h68I4/hqdefault.jpg", "https://www.youtube.com/watch?v=PN7lP0h68I4"));
        playList.add(new YoutubeLink("https://img.youtube.com/vi/YuHPU6rvBXA/hqdefault.jpg", "https://www.youtube.com/watch?v=YuHPU6rvBXA"));
        playList.add(new YoutubeLink("https://img.youtube.com/vi/tHCEGL_n7G0/hqdefault.jpg", "https://www.youtube.com/watch?v=tHCEGL_n7G0"));
        playList.add(new YoutubeLink("https://img.youtube.com/vi/e-HMUaxq_HU/hqdefault.jpg", "https://www.youtube.com/watch?v=e-HMUaxq_HU"));

    }
}
