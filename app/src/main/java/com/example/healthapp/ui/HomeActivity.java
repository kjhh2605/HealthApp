package com.example.healthapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.healthapp.R;
import com.example.healthapp.ui.gym.SearchGymFragment;
import com.example.healthapp.ui.home.HomeFragment;
import com.example.healthapp.ui.login.StartActivity;
import com.example.healthapp.ui.workout.WorkoutFragment;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private TextView tabHome, tabRecord, tabSearch;
    private Fragment homeFragment, workoutFragment, searchGymFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabHome = findViewById(R.id.tab_home);
        tabHome.setBackgroundResource(R.drawable.tab_selected);
        tabRecord = findViewById(R.id.tab_record);
        tabSearch = findViewById(R.id.tab_search);

        homeFragment = new HomeFragment();
        workoutFragment = new WorkoutFragment();
        searchGymFragment = new SearchGymFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment, "HOME")
                .add(R.id.fragment_container, workoutFragment, "WORKOUT").hide(workoutFragment)
                .add(R.id.fragment_container, searchGymFragment, "SEARCH").hide(searchGymFragment)
                .commit();

        activeFragment = homeFragment;

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
        updateTabBackgrounds(fragment);
    }

    private void updateTabBackgrounds(Fragment selectedFragment) {
        if (selectedFragment == homeFragment) {
            tabHome.setBackgroundResource(R.drawable.tab_selected);
            tabRecord.setBackgroundResource(R.drawable.tab_unselected);
            tabSearch.setBackgroundResource(R.drawable.tab_unselected);
        } else if (selectedFragment == workoutFragment) {
            tabHome.setBackgroundResource(R.drawable.tab_unselected);
            tabRecord.setBackgroundResource(R.drawable.tab_selected);
            tabSearch.setBackgroundResource(R.drawable.tab_unselected);
        } else if (selectedFragment == searchGymFragment) {
            tabHome.setBackgroundResource(R.drawable.tab_unselected);
            tabRecord.setBackgroundResource(R.drawable.tab_unselected);
            tabSearch.setBackgroundResource(R.drawable.tab_selected);
        }
    }

    @Override
    public void onBackPressed() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("앱 종료")
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("종료", (dialog, which) -> {finishAffinity();})
                .setNegativeButton("취소", null)
                .show();
    }


}
