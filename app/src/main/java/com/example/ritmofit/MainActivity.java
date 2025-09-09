package com.example.ritmofit;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerClases;
    private List<String> clasesDisplayList;
    private SimpleAdapter recyclerAdapter;
    @Inject
    ClasesService clasesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerClases = findViewById(R.id.recyclerClases);
        recyclerClases.setLayoutManager(new LinearLayoutManager(this));

        // TODO: traer de backend con Retrofit
        clasesDisplayList = new ArrayList<>();
        clasesDisplayList.add("Funcional - Palermo - 18:00");
        clasesDisplayList.add("Yoga - Belgrano - 19:00");

        recyclerAdapter = new SimpleAdapter(clasesDisplayList);
        recyclerClases.setAdapter(recyclerAdapter);

        loadClases();
    }

    private void loadClases(){
        clasesService.getAllClases(new ClasesServiceCallBack() {
            @Override
            public void onSuccess(List<Clase> clases) {
                clasesDisplayList.clear();
                clasesDisplayList.addAll(clases.stream()
                        .map(clase -> clase.getDisciplina() + " - " + clase.getSede() + " - " + clase.getFechaHora())
                        .collect(Collectors.toList()));
                runOnUiThread(()-> recyclerAdapter.notifyDataSetChanged());
            }

            @Override
            public void onError(Throwable error) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "Error al cargar las Clases: " + error.getMessage(),
                        Toast.LENGTH_LONG).show());
            }
        });
    }
}
