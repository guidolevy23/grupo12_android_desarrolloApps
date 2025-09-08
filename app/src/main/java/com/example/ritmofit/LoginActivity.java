package com.example.ritmofit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextOtp;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Referencias a los elementos del layout
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextOtp = findViewById(R.id.editTextOtp);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Acción al presionar el botón
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String otp = editTextOtp.getText().toString().trim();

            if (email.isEmpty() || otp.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Por ahora solo mostramos lo ingresado
                Toast.makeText(this, "Email: " + email + "\nOTP: " + otp, Toast.LENGTH_LONG).show();

                // Más adelante acá iría la llamada al backend con Retrofit
                // y si es correcto -> abrir UserActivity
            }
        });
    }
}
