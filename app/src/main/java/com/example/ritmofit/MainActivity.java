package com.example.ritmofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ritmofit.SimpleAdapter;

//import com.example.myfirstapplication.data.repository.PokemonServiceCallBack;
import com.example.ritmofit.data.repository.ClasesServiceCallBack;
import com.example.ritmofit.model.Clase;
import com.example.ritmofit.model.User;
import com.example.ritmofit.services.ClasesService;
import com.example.ritmofit.services.UsuarioService;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleAuth();

        // Set up navigation button click listener
        findViewById(R.id.btnNavigateToCourseMain).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CourseMainActivity.class);
            startActivity(intent);
        });
    }

    private void handleAuth() {
        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);

        if (token == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
