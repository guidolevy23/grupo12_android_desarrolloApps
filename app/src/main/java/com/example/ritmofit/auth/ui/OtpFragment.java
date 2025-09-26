package com.example.ritmofit.auth.ui;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ritmofit.R;
import com.example.ritmofit.auth.service.AuthService;
import com.example.ritmofit.core.DomainCallback;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OtpFragment extends Fragment {

    private String email;

    TextView tvOtpInstruction;

    private EditText etOtp;
    private Button btnVerifyOtp;

    @Inject
    AuthService service;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.auth_fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvOtpInstruction = view.findViewById(R.id.tvOtpInstruction);
        etOtp = view.findViewById(R.id.etOtp);
        btnVerifyOtp = view.findViewById(R.id.btnVerifyOtp);

        if (getArguments() != null) {
            email = getArguments().getString("email");
            requestOtp(email);
        }
        btnVerifyOtp.setOnClickListener(this::validateOtp);
    }

    private void instructUser(String email) {
        tvOtpInstruction.setText(getString(R.string.otp_instruction, email));
    }

    private void requestOtp(String email) {
        service.otpRequetst(email, new DomainCallback<>() {
            @Override
            public void onSuccess(String result) {
                instructUser(email);
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validateOtp(View view) {
        String code = etOtp.getText().toString().trim();
        if (code.isEmpty()) {
            etOtp.setError("OTP is required");
            etOtp.requestFocus();
            return;
        }
        service.validateOtp(email, code, new DomainCallback<>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getContext(), "User validated successfully", Toast.LENGTH_LONG).show();
                Navigation.findNavController(view)
                        .navigate(R.id.action_otpFragment_to_mainActivity);
            }

            @Override
            public void onError(Throwable error) {
                etOtp.setError("Otp Error");
                etOtp.requestFocus();
                Toast.makeText(getContext(), "OTP validation failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
