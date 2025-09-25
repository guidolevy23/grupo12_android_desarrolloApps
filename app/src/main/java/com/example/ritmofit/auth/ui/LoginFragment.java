package com.example.ritmofit.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ritmofit.R;
import com.example.ritmofit.auth.model.Login;
import com.example.ritmofit.auth.repository.TokenRepository;
import com.example.ritmofit.auth.service.AuthService;
import com.example.ritmofit.core.DomainCallback;
import com.example.ritmofit.security.service.SecurityService;
import com.example.ritmofit.security.ui.SecurityActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    @Inject
    TokenRepository tokenRepository;
    @Inject
    AuthService authService;
    @Inject
    SecurityService securityService;

    private EditText userEditText;
    private EditText passwordEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.auth_fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userEditText = view.findViewById(R.id.userEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        Button loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);

        loginButton.setOnClickListener(this::login);

        registerButton.setOnClickListener(this::register);
    }

    private void login(View view) {
        Login login = new Login(
                userEditText.getText().toString().trim(),
                passwordEditText.getText().toString().trim()
        );
        authService.login(login, new DomainCallback<>() {
            @Override
            public void onSuccess(String token) {
                tokenRepository.saveToken(token);
                
                // Después del login exitoso, verificar si se requiere autenticación de seguridad
                if (securityService.shouldRequestAuthentication()) {
                    // Redirigir a SecurityActivity para autenticación biométrica
                    Intent intent = new Intent(requireContext(), SecurityActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    // Si no se requiere seguridad, ir directamente a MainActivity
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeActivity);
                }
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(requireContext(), "Login failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void register(View view) {
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
    }
}
