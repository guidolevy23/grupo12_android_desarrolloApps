package com.example.ritmofit;

import android.os.Bundle;
import com.example.ritmofit.SimpleAdapter;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerClases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerClases = findViewById(R.id.recyclerClases);
        recyclerClases.setLayoutManager(new LinearLayoutManager(this));

        // TODO: traer de backend con Retrofit
        List<String> clases = new ArrayList<>();
        clases.add("Funcional - Palermo - 18:00");
        clases.add("Yoga - Belgrano - 19:00");

        recyclerClases.setAdapter(new SimpleAdapter(clases));
    }
}
