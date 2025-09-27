package com.example.ritmofit.profile.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ritmofit.R;
import com.example.ritmofit.auth.repository.TokenRepository;
import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.profile.model.UpdateUserRequest;
import com.example.ritmofit.profile.model.User;
import com.example.ritmofit.profile.service.UserService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private User user;

    private View groupView;
    private TextView tvNombre, tvEmail, tvDireccion, tvTelefono;

    private View groupEdit;
    private EditText etNombre,etEmail ,etDireccion, etTelefono;

    private Button btnEditarGuardar, btnCancelar, btnLogout;

    private boolean editing = false;
    @Inject
    UserService service;

    @Inject
    TokenRepository tokenRepository;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v      = inflater.inflate(R.layout.fragment_profile, container, false);
        groupView   = v.findViewById(R.id.groupView);
        tvNombre    = v.findViewById(R.id.tvNombre);
        tvEmail     = v.findViewById(R.id.tvEmail);
        tvDireccion = v.findViewById(R.id.tvDireccion);
        tvTelefono  = v.findViewById(R.id.tvTelefono);

        groupEdit   = v.findViewById(R.id.groupEdit);
        etNombre    = v.findViewById(R.id.etNombre);
        etEmail     = v.findViewById(R.id.etEmail);
        etDireccion = v.findViewById(R.id.etDireccion);
        etTelefono  = v.findViewById(R.id.etTelefono);

        btnEditarGuardar = v.findViewById(R.id.btnEditarGuardar);
        btnCancelar      = v.findViewById(R.id.btnCancelar);
        btnLogout = v.findViewById(R.id.btnLogout);

        SharedPreferences prefs = requireContext().getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        tvNombre.setText(prefs.getString("USER_NAME",  "—"));
        tvEmail.setText(prefs.getString("USER_EMAIL", "—"));
        tvDireccion.setText(prefs.getString("USER_DIRECCION", "—"));
        tvTelefono.setText(prefs.getString("USER_TELEFONO", "—"));

        getUser();

        btnEditarGuardar.setOnClickListener(view -> {
            if (!editing) enterEditMode();
            else saveChanges();
        });

        btnCancelar.setOnClickListener(view -> exitEditModeWithoutSaving());


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout.setOnClickListener(v -> {
            tokenRepository.clearToken();
            Navigation.findNavController(view)
                    .navigate(R.id.action_profileFragment_to_mainActivity);
        });
    }

    private void enterEditMode() {
        editing = true;

        // Pre-cargar inputs desde los TextView actuales
        etNombre.setText(tvNombre.getText());
        etDireccion.setText(tvDireccion.getText());
        etTelefono.setText(tvTelefono.getText());

        groupView.setVisibility(View.GONE);
        groupEdit.setVisibility(View.VISIBLE);
        btnCancelar.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
        btnEditarGuardar.setText("Guardar");
    }

    private void exitEditModeWithoutSaving() {
        editing = false;
        groupView.setVisibility(View.VISIBLE);
        groupEdit.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);
        btnLogout.setVisibility(View.VISIBLE);
        btnEditarGuardar.setText("Editar");
    }

    private void saveChanges() {

        if (TextUtils.isEmpty(etNombre.getText()))   { etNombre.setError("Requerido"); return; }
        if (TextUtils.isEmpty(etTelefono.getText())) { etTelefono.setError("Requerido"); return; }
        if (TextUtils.isEmpty(etDireccion.getText())) { etDireccion.setError("Requerido"); return; }


        tvNombre.setText(etNombre.getText());
        tvDireccion.setText(etTelefono.getText());
        tvTelefono.setText(etDireccion.getText());

        saveUser(user.getId());


    }

    void getUser() {
        service.fetchCurrentUser(new DomainCallback<>() {
            @Override
            public void onSuccess(User result) {
                if (!isAdded()) return; // el fragment ya no está en pantalla
                user = result;
                Log.d("PROFILE", "name=" + result.getName() +
                        ", direccion=" + result.getDireccion() +
                        ", telefono=" + result.getTelefono());
                tvNombre.setText(result.getName());
                tvEmail.setText(result.getEmail());
                etEmail.setText(result.getEmail());
                tvDireccion.setText(result.getDireccion() != null ? result.getDireccion():"—");
                tvTelefono.setText(result.getTelefono() != null ? result.getTelefono():"—");
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(requireContext(), "Error al cargar perfil: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    void saveUser(Long id){
        UpdateUserRequest UpdateUserRequest = new UpdateUserRequest(etNombre.getText().toString(), etDireccion.getText().toString(), etTelefono.getText().toString(),user.getPhotoUrl(), user.getPassword(), user.getRole(),user.getEmail(), id);
        service.saveUser(id, UpdateUserRequest,new DomainCallback<>() {
            @Override
            public void onSuccess(User result) {
                Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                exitEditModeWithoutSaving();
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(requireContext(), "Error al guardar los cambios: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}