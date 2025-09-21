package com.example.ritmofit.auth.http;

import androidx.annotation.NonNull;

import com.example.ritmofit.auth.model.UnAuthenticationEvent;
import com.example.ritmofit.auth.repository.TokenRepository;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final TokenRepository tokenRepository;

    public AuthInterceptor(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String token = tokenRepository.getToken();
        Request.Builder requestBuilder = chain.request().newBuilder();

        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }
        Response response = chain.proceed(requestBuilder.build());
        if (response.code() == 401) {
            tokenRepository.clearToken();
            EventBus.getDefault().post(new UnAuthenticationEvent("Token expired or invalid"));
        }
        return response;
    }
}
