package com.example.healthapp.ui.gym;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.model.Gym;
import com.example.healthapp.repository.MyFirebaseDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchGymFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private DatabaseReference myDB;
    private List<Gym> gymList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_gym, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. RecyclerView 연결
        recyclerView = view.findViewById(R.id.recycler_gym);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        myDB = MyFirebaseDB.getDB().child("Gym");
        gymList = new ArrayList<>();

        myDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Gym gym = dataSnapshot.getValue(Gym.class);
                    gymList.add(gym);
                }
                adapter.notifyDataSetChanged(); //리스트 저장 & 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Gym", "Failed to read DB."); //에러문
            }
        });

        // 2. Adapter 생성 및 연결
        adapter = new GymAdapter(gymList,this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


    }
}
