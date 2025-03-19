package com.example.loginmvp.data.repository;

public interface CartCallback<T> {
    void onResult(T result);
    void onError(Throwable t);
}
