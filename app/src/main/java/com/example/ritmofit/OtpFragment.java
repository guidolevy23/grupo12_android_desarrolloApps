package com.example.ritmofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OtpFragment extends Fragment {

    static final String ARG_EMAIL = "email";
    private String email;

    TextView tvOtpInstruction;

    private EditText etOtp;
    private Button btnVerifyOtp;
    private ProgressBar progressBar;
//    private ApiService apiService;
    private SharedPreferences preferences;

    public static OtpFragment newInstance(String username) {
        OtpFragment fragment = new OtpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        tvOtpInstruction = view.findViewById(R.id.tvOtpInstruction);
        etOtp = view.findViewById(R.id.etOtp);
        btnVerifyOtp = view.findViewById(R.id.btnVerifyOtp);
        progressBar = view.findViewById(R.id.progressBar);

        preferences = requireContext().getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);

        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
            instructUser(email);
        }

        btnVerifyOtp.setOnClickListener(v -> verifyOtp());

        return view;
    }

    private void instructUser(String email) {
        tvOtpInstruction.setText(getString(R.string.otp_instruction, email));
    }

    private void verifyOtp() {
        String otp = etOtp.getText().toString().trim();

        if (TextUtils.isEmpty(otp)) {
            Toast.makeText(getContext(), getString(R.string.otp_help), Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        handleOtpSuccess("123");
    }

    private void handleOtpSuccess(String token) {
        // Save token
        preferences.edit().putString("TOKEN", token).apply();

        Toast.makeText(getContext(), getString(R.string.otp_success), Toast.LENGTH_SHORT).show();

        // Navigate to MainActivity (the real app)
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);

        // Close LoginActivity so user cannot go back to login
        requireActivity().finish();
    }
}
