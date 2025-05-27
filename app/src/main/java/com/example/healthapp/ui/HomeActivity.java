package com.example.healthapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.healthapp.R;
import com.example.healthapp.ui.gym.SearchGymFragment;
import com.example.healthapp.ui.home.HomeFragment;
import com.example.healthapp.ui.workout.WorkoutFragment;

public class HomeActivity extends AppCompatActivity {
    private TextView tabHome, tabRecord, tabSearch;
    private Fragment homeFragment, workoutFragment, searchGymFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabHome = findViewById(R.id.tab_home);
        tabRecord = findViewById(R.id.tab_record);
        tabSearch = findViewById(R.id.tab_search);

        homeFragment = new HomeFragment();
        workoutFragment = new WorkoutFragment();
        searchGymFragment = new SearchGymFragment();

        // 처음에 모든 프래그먼트 add, workoutFragment만 show
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment, "HOME").hide(homeFragment)
                .add(R.id.fragment_container, workoutFragment, "WORKOUT")
                .add(R.id.fragment_container, searchGymFragment, "SEARCH").hide(searchGymFragment)
                .commit();

        activeFragment = workoutFragment;

        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(homeFragment);
            }
        });
        tabRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(workoutFragment);
            }
        });
        tabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(searchGymFragment);
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        if (activeFragment == fragment) return;
        getSupportFragmentManager().beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit();
        activeFragment = fragment;
    }
}
