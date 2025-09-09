package com.example.ritmofit.data.repository;

import java.util.List;
import com.example.ritmofit.model.Clase;
public interface ClasesServiceCallBack {
    void onSuccess(List<Clase> clases);
    void onError(Throwable error);
}
