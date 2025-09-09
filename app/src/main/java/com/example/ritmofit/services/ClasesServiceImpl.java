package com.example.ritmofit.services;

import com.example.ritmofit.data.repository.ClasesRepository;
import com.example.ritmofit.data.repository.ClasesServiceCallBack;
import com.example.ritmofit.model.Clase;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
@Singleton
public class ClasesServiceImpl implements ClasesService {
    private final ClasesRepository clasesRepository;

    @Inject
    public ClasesServiceImpl(ClasesRepository clasesRepository) {
        this.clasesRepository = clasesRepository;
    }

    @Override
    public void getAllClases(ClasesServiceCallBack callback) {
        clasesRepository.getAllClases(callback);
    }

    @Override
    public void getClaseByName(String name, ClasesServiceCallBack callback) {
        clasesRepository.getClaseByName(name, callback);
    }
}
