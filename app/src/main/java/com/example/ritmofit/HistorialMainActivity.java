package com.example.ritmofit;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main activity for hosting the Historial (History) page.
 * This activity uses Navigation Component to host the HistorialFragment.
 * 
 * Requirements covered:
 * - 3.1: Accessible from main navigation
 * - 3.3: Proper back navigation behavior
 * - 3.4: Navigation stack management
 */
@AndroidEntryPoint
public class HistorialMainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_main);

        // Setup navigation
        setupNavigation();
        
        // Setup action bar
        setupActionBar();
    }

    /**
     * Sets up the Navigation Component
     */
    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_historial);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
    }

    /**
     * Sets up the action bar with proper title and back navigation
     * Implements requirement 3.3: Proper back navigation behavior
     */
    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.historial_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Handles back navigation when up button is pressed
     * Implements requirement 3.3: Proper back navigation behavior
     */
    @Override
    public boolean onSupportNavigateUp() {
        if (navController != null && navController.navigateUp()) {
            return true;
        }
        // If navigation component can't handle it, use default behavior
        finish();
        return true;
    }

    /**
     * Handles system back button press
     * Implements requirement 3.4: Navigation stack management
     */
    @Override
    public void onBackPressed() {
        if (navController != null && navController.navigateUp()) {
            return;
        }
        // If navigation component can't handle it, use default behavior
        super.onBackPressed();
    }
}