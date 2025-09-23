package com.example.ritmofit.auth.ui;

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
import com.example.ritmofit.auth.model.Register;
import com.example.ritmofit.auth.service.AuthService;
import com.example.ritmofit.core.DomainCallback;
import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends Fragment {

    private EditText emailEditText;
    private EditText nameEditText;
    private EditText passwordEditText;

    private EditText passwordAgainEditText;

    @Inject
    AuthService authService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.auth_fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEditText = view.findViewById(R.id.registerEmailEditText);
        nameEditText = view.findViewById(R.id.registerNameEditText);
        passwordEditText = view.findViewById(R.id.registerPasswordEditText);
        passwordAgainEditText = view.findViewById(R.id.registerPasswordAgainEditText);
        Button registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(this::register);
    }

    private void register(View view) {
        if (!validPassword()) {
            return;
        }
        Register request = new Register(
                emailEditText.getText().toString().trim(),
                passwordEditText.getText().toString().trim(),
                nameEditText.getText().toString().trim()
        );
        authService.register(request, new DomainCallback<>() {
            @Override
            public void onSuccess(String result) {
                Bundle bundle = new Bundle();
                bundle.putString("email", request.username());
                Navigation.findNavController(view)
                        .navigate(R.id.action_registerFragment_to_otpFragment, bundle);
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(requireContext(), "Registering failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private boolean validPassword() {
        String password = passwordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.empty_password));
            passwordEditText.requestFocus();
            return false;
        }
        String passwordAgain = passwordAgainEditText.getText().toString().trim();
        if (!password.equals(passwordAgain)) {
            passwordAgainEditText.setError(getString(R.string.password_does_not_match));
            passwordAgainEditText.requestFocus();
            return false;
        }
        return true;
    }


}
