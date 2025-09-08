package com.example.ritmofit;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    private TextView textNombre;
    private TextView textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        textNombre = findViewById(R.id.textNombre);
        textEmail = findViewById(R.id.textEmail);

        String nombre = getIntent().getStringExtra("nombre");
        String email = getIntent().getStringExtra("email");

        textNombre.setText("Bienvenido, " + nombre);
        textEmail.setText(email);
    }
}
