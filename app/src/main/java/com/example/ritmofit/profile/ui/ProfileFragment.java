package com.example.ritmofit.profile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.ritmofit.R;
import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.profile.model.User;
import com.example.ritmofit.profile.service.UserService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    @Inject
    UserService service;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        getUser();
        TextView tvNombre    = v.findViewById(R.id.tvNombre);
        TextView tvTelefono  = v.findViewById(R.id.tvTelefono);
        TextView tvEmail     = v.findViewById(R.id.tvEmail);
        TextView tvDireccion = v.findViewById(R.id.tvDireccion);
        TextView tvFecha     = v.findViewById(R.id.tvFecha);


        SharedPreferences prefs = requireContext().getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        String nombre   = prefs.getString("USER_NAME",  "—");
        String telefono = prefs.getString("USER_PHONE", "—");
        String email    = prefs.getString("USER_EMAIL", "—");
        String dir      = prefs.getString("USER_ADDR",  "—");
        String fecha    = prefs.getString("USER_BDAY",  "—");

        tvNombre.setText(nombre);
        tvTelefono.setText(telefono);
        tvEmail.setText(email);
        tvDireccion.setText(dir);
        tvFecha.setText(fecha);

        return v;
    }

    void getUser() {
        service.fetchCurrentUser(new DomainCallback<>() {
            @Override
            public void onSuccess(User result) {
                String data = "Nombre: " + result.getName() + "\n" +
                        "Email: " + result.getEmail() + "\n";
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }
}