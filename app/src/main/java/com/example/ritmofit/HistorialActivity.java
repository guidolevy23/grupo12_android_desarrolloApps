package com.example.ritmofit;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @deprecated Use HistorialMainActivity instead.
 *             This class is kept for backward compatibility but should not be
 *             used.
 */
@Deprecated
public class HistorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Redirect to HistorialMainActivity
        finish();
    }
}
