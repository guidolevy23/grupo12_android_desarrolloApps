package com.example.ritmofit;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        EditText nombre = view.findViewById(R.id.editTextText);
        EditText telefono = view.findViewById(R.id.editTextPhone);
        EditText email = view.findViewById(R.id.editTextTextEmailAddress2);
        Button botonEditar = view.findViewById(R.id.button);

        return view;
    }
}