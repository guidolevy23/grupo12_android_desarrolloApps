package com.example.ritmofit.home.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ritmofit.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main_activity);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.home_nav_host_fragment);
        if (navHostFragment != null) {
            navHostFragment.getNavController();
        }
    }
}