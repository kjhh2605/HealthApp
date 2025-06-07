package com.example.healthapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.model.YoutubeLink;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // ViewPager2와 어댑터 연결 예시
        RecyclerView cardRecycler = view.findViewById(R.id.card_recycler);

        List<YoutubeLink> cardList = new ArrayList<>();
        cardList.add(new YoutubeLink( "https://img.youtube.com/vi/I2mo7a9XHnM/hqdefault.jpg", "https://www.youtube.com/watch?v=I2mo7a9XHnM"));
        cardList.add(new YoutubeLink("https://img.youtube.com/vi/WnrIX9Ak1wA/hqdefault.jpg", "https://www.youtube.com/watch?v=WnrIX9Ak1wA"));
        cardList.add(new YoutubeLink("https://img.youtube.com/vi/PN7lP0h68I4/hqdefault.jpg", "https://www.youtube.com/watch?v=PN7lP0h68I4"));
        cardList.add(new YoutubeLink("https://img.youtube.com/vi/YuHPU6rvBXA/hqdefault.jpg", "https://www.youtube.com/watch?v=YuHPU6rvBXA"));
        cardList.add(new YoutubeLink("https://img.youtube.com/vi/tHCEGL_n7G0/hqdefault.jpg", "https://www.youtube.com/watch?v=tHCEGL_n7G0"));
        cardList.add(new YoutubeLink("https://img.youtube.com/vi/e-HMUaxq_HU/hqdefault.jpg", "https://www.youtube.com/watch?v=e-HMUaxq_HU"));

        CardRecyclerAdapter adapter = new CardRecyclerAdapter(cardList, requireContext());
        cardRecycler.setAdapter(adapter);

        // 가로 스크롤 레이아웃
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(layoutManager);

        return view;
    }
}
