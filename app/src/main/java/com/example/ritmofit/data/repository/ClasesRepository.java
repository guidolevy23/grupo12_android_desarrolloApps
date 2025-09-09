package com.example.ritmofit.data.repository;

public interface ClasesRepository {
    void getAllClases(ClasesServiceCallBack callback);
    void getClaseByName(String name, ClasesServiceCallBack callback);
}
