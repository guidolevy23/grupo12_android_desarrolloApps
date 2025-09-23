package com.example.ritmofit.auth.ui;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ritmofit.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OtpFragment extends Fragment {

    private String email;

    TextView tvOtpInstruction;
    private Button btnVerifyOtp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment_otp, container, false);

        tvOtpInstruction = view.findViewById(R.id.tvOtpInstruction);
        btnVerifyOtp = view.findViewById(R.id.btnVerifyOtp);

        if (getArguments() != null) {
            email = getArguments().getString("email");
            instructUser(email);
        }

        btnVerifyOtp.setOnClickListener(this::validateOtp);

        return view;
    }

    private void instructUser(String email) {
        tvOtpInstruction.setText(getString(R.string.otp_instruction, email));
    }

    private void validateOtp(View view) {
        Navigation.findNavController(view)
                .navigate(R.id.action_otpFragment_to_loginFragment);
    }
}
