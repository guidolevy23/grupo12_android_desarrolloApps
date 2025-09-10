package com.example.ritmofit.data.api.model;

import com.google.gson.annotations.SerializedName;

public class PageResponse<T> {

    @SerializedName("_embedded")
    private T data;

    public T getData() {
        return data;
    }
}
