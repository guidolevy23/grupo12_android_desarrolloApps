package com.example.ritmofit.core;

public interface Callback<T> {
    void onSuccess(T result);
    void onError(Throwable error);
}
