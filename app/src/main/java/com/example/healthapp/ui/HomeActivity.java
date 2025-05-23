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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabHome = findViewById(R.id.tab_home);
        tabRecord = findViewById(R.id.tab_record);
        tabSearch = findViewById(R.id.tab_search);

        // 처음 화면에 HomeFragment 표시
        loadFragment(new WorkoutFragment());

        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
            }
        });
        tabRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new WorkoutFragment());
            }
        });
        tabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SearchGymFragment());
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}