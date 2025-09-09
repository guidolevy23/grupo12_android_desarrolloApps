package com.example.ritmofit;

import android.content.Intent;
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

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextOtp = findViewById(R.id.editTextOtp);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String otp = editTextOtp.getText().toString().trim();

            if (email.isEmpty() || otp.isEmpty()) {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: llamada real a backend con Retrofit
                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                intent.putExtra("nombre", "Usuario de prueba");
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });
    }
}
