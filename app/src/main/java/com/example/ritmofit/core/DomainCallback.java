package com.example.ritmofit.core;

public interface DomainCallback<T> {
    void onSuccess(T result);
    void onError(Throwable error);
}
