package com.example.ritmofit.services;

import com.example.ritmofit.data.repository.ClasesServiceCallBack;
import com.example.ritmofit.model.Clase;
import java.util.List;

public interface ClasesService {
void getAllClases(ClasesServiceCallBack callBack);
void getClaseByName(String name, ClasesServiceCallBack callBack);
}
