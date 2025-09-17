package com.example.ritmofit;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    private EditText etEmail;
    private Button btnRequestOtp;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etEmail = view.findViewById(R.id.etEmail);
        btnRequestOtp = view.findViewById(R.id.btnLogin);
        progressBar = view.findViewById(R.id.progressBar);

        btnRequestOtp.setOnClickListener(this::requestOtp);

        return view;
    }

    private void requestOtp(View v) {
        String username = etEmail.getText()
                .toString()
                .trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), getString(R.string.email_help), Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Bundle args = new Bundle();
        args.putString(OtpFragment.ARG_EMAIL, username);
        Navigation.findNavController(v)
                .navigate(R.id.action_loginFragment_to_otpFragment, args);
    }
}
