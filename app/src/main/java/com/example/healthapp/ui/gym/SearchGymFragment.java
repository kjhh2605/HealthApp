package com.example.healthapp.ui.gym;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.adapter.GymAdapter;
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

    private EditText editSearch;
    private TextView resultCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_gym, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_gym);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        myDB = MyFirebaseDB.getDB().child("Gym");
        gymList = new ArrayList<>();
        resultCount = view.findViewById(R.id.result_count);

        // 초기 검색창 힌트 설정
        editSearch = view.findViewById(R.id.edit_search);
        setRandomHint();
        editSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGyms(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        myDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Gym gym = dataSnapshot.getValue(Gym.class);
                    gymList.add(gym);
                }
                adapter.notifyDataSetChanged(); //리스트 저장 & 새로고침
                resultCount.setText("전체 " + String.valueOf(gymList.size()) + "개");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Gym", "Failed to read DB."); //에러문
            }
        });

        adapter = new GymAdapter(gymList, getContext(), new GymAdapter.OnGymClickListener() {
            @Override
            public void onGymClick(Gym gym) {
                // 헬스장 상세 정보 다이어로그
                GymInfoBottomSheet
                        .newInstance(gym)
                        .show(getParentFragmentManager(), "GymInfoBottomSheet");
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public void onResume() {
        super.onResume();
        setRandomHint();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setRandomHint();
        }
    }

    private void setRandomHint() {
        String[][] hints = {
                {"찾는 기구가 있으신가요?", "ex)해머 스트렝스 로우로우"},
                {"내가 원하는 트레이너 경력은?", "ex)스포츠지도사"},
                {"우리 동네 헬스장은?", "ex)성북구"}
        };

        int randomIndex = new java.util.Random().nextInt(hints.length);
        String mainHint = hints[randomIndex][0];
        String example = hints[randomIndex][1];

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(mainHint);
        ssb.append("\n");
        int start = ssb.length();
        ssb.append(example);
        int end = ssb.length();
        ssb.setSpan(new android.text.style.RelativeSizeSpan(0.8f), start, end, 0); // 작은 글씨
        ssb.setSpan(new android.text.style.ForegroundColorSpan(0xFF888888), start, end, 0); // 회색

        if (editSearch != null) {
            editSearch.setHint(ssb);
        }
    }

    private void filterGyms(String query) {
        List<Gym> filteredList = new ArrayList<>();
        query = query.toLowerCase();

        for (Gym gym : gymList) {
            if (gym == null) continue;

            // 이름 검색
            boolean nameMatch = gym.getName() != null && gym.getName().toLowerCase().contains(query);
            // 지역 검색
            boolean regionMatch = gym.getRegion() != null && gym.getRegion().toLowerCase().contains(query);
            // 머신(기구) 검색
            boolean machineMatch = false;
            if (gym.getMachineList() != null) {
                for (String machine : gym.getMachineList()) {
                    if (machine.toLowerCase().contains(query)) {
                        machineMatch = true;
                        break;
                    }
                }
            }
            // 트레이너 경력(자격증) 검색
            boolean trainerCertMatch = false;
            if (gym.getTrainerList() != null) {
                for (ArrayList<String> certs : gym.getTrainerList().values()) {
                    for (String cert : certs) {
                        if (cert.toLowerCase().contains(query)) {
                            trainerCertMatch = true;
                            break;
                        }
                    }
                    if (trainerCertMatch) break;
                }
            }

            if (nameMatch || regionMatch || machineMatch || trainerCertMatch) {
                filteredList.add(gym);
            }
        }

        ((GymAdapter) adapter).updateList(filteredList);
        resultCount.setText("검색 결과 " + filteredList.size() + "개");
    }

}
