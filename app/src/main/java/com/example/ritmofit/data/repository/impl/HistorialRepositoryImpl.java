package com.example.ritmofit.data.repository.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.ritmofit.data.api.model.HistorialResponse;
import com.example.ritmofit.data.api.model.HistorialService;
import com.example.ritmofit.data.repository.HistorialRepository;
import com.example.ritmofit.model.HistorialItem;
import com.example.ritmofit.utils.DateUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of HistorialRepository for managing historial (attendance history) data.
 * Handles API integration using HistorialService and provides data transformation
 * from HistorialResponse to HistorialItem domain models.
 */
@Singleton
public class HistorialRepositoryImpl implements HistorialRepository {

    private final HistorialService historialService;
    private final Context context;

    @Inject
    public HistorialRepositoryImpl(HistorialService historialService, Context context) {
        this.historialService = historialService;
        this.context = context;
    }

    @Override
    public void getHistorial(LocalDate fromDate, LocalDate toDate, HistorialCallback callback) {
        // Check network connectivity before making the request
        if (!isNetworkAvailable()) {
            callback.onError("Sin conexión a internet. Por favor, verifica tu conexión y vuelve a intentar.");
            return;
        }

        // Validate date range before making API call
        String validationError = validateDateRange(fromDate, toDate);
        if (validationError != null) {
            callback.onError(validationError);
            return;
        }

        String fromDateStr = DateUtils.formatForApi(fromDate);
        String toDateStr = DateUtils.formatForApi(toDate);

        Call<List<HistorialResponse>> call = historialService.getHistorial(fromDateStr, toDateStr);
        
        call.enqueue(new Callback<List<HistorialResponse>>() {
            @Override
            public void onResponse(Call<List<HistorialResponse>> call, Response<List<HistorialResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<HistorialItem> historialItems = transformResponseToItems(response.body());
                        callback.onSuccess(historialItems);
                    } else {
                        // Empty response body - treat as empty result
                        callback.onSuccess(new ArrayList<>());
                    }
                } else {
                    String errorMessage = getErrorMessage(response.code(), response.message());
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<HistorialResponse>> call, Throwable t) {
                String errorMessage = getNetworkErrorMessage(t);
                callback.onError(errorMessage);
            }
        });
    }

    @Override
    public void getCurrentMonthHistorial(HistorialCallback callback) {
        LocalDate firstDayOfMonth = DateUtils.getFirstDayOfCurrentMonth();
        LocalDate lastDayOfMonth = DateUtils.getLastDayOfCurrentMonth();
        getHistorial(firstDayOfMonth, lastDayOfMonth, callback);
    }

    @Override
    public void getAllHistorial(HistorialCallback callback) {
        getHistorial(null, null, callback);
    }

    @Override
    public String validateDateRange(LocalDate fromDate, LocalDate toDate) {
        // Basic range validation
        if (!DateUtils.isValidDateRange(fromDate, toDate)) {
            return "La fecha 'desde' no puede ser posterior a la fecha 'hasta'";
        }
        
        LocalDate today = LocalDate.now();
        
        // Check if dates are in the future
        if (fromDate != null && fromDate.isAfter(today)) {
            return "La fecha 'desde' no puede ser una fecha futura";
        }
        
        if (toDate != null && toDate.isAfter(today)) {
            return "La fecha 'hasta' no puede ser una fecha futura";
        }
        
        // Check if date range is too far in the past (more than 5 years)
        LocalDate fiveYearsAgo = today.minusYears(5);
        if (fromDate != null && fromDate.isBefore(fiveYearsAgo)) {
            return "La fecha 'desde' no puede ser anterior a " + DateUtils.formatForDisplay(fiveYearsAgo);
        }
        
        if (toDate != null && toDate.isBefore(fiveYearsAgo)) {
            return "La fecha 'hasta' no puede ser anterior a " + DateUtils.formatForDisplay(fiveYearsAgo);
        }
        
        // Check if date range is too wide (more than 2 years)
        if (fromDate != null && toDate != null) {
            long daysBetween = DateUtils.daysBetween(fromDate, toDate);
            if (daysBetween > 730) { // 2 years approximately
                return "El rango de fechas no puede ser mayor a 2 años";
            }
        }
        
        return null; // Valid range
    }

    @Override
    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = 
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
            
            return false;
        } catch (Exception e) {
            // If we can't check network status, assume it's available
            return true;
        }
    }

    /**
     * Transforms a list of HistorialResponse objects to HistorialItem domain models
     * @param responses list of API response objects
     * @return list of domain model objects
     */
    private List<HistorialItem> transformResponseToItems(List<HistorialResponse> responses) {
        List<HistorialItem> items = new ArrayList<>();
        
        for (HistorialResponse response : responses) {
            HistorialItem item = transformResponseToItem(response);
            if (item != null) {
                items.add(item);
            }
        }
        
        return items;
    }

    /**
     * Transforms a single HistorialResponse to HistorialItem domain model
     * @param response API response object
     * @return domain model object or null if transformation fails
     */
    private HistorialItem transformResponseToItem(HistorialResponse response) {
        if (response == null) {
            return null;
        }

        try {
            HistorialItem item = new HistorialItem();
            
            // Set basic fields
            item.setId(response.id);
            item.setClase(response.clase != null ? response.clase : "");
            item.setSede(response.sede != null ? response.sede : "");
            item.setDuracion(response.duracion);

            // Parse and set date
            LocalDate fecha = DateUtils.parseApiDate(response.fecha);
            item.setFecha(fecha);

            // Parse and set time
            LocalTime hora = DateUtils.parseTime(response.hora);
            item.setHora(hora);

            return item;
        } catch (Exception e) {
            // Log the error in a real application
            // For now, return null to skip invalid items
            return null;
        }
    }

    /**
     * Generates user-friendly error messages based on HTTP response codes
     * @param responseCode HTTP response code
     * @param responseMessage HTTP response message
     * @return localized error message in Spanish
     */
    private String getErrorMessage(int responseCode, String responseMessage) {
        switch (responseCode) {
            case 400:
                return "Solicitud inválida. Verifica que las fechas sean correctas y vuelve a intentar.";
            case 401:
                return "Tu sesión ha expirado. Por favor, inicia sesión nuevamente.";
            case 403:
                return "No tienes permisos para acceder al historial de clases.";
            case 404:
                return "El servicio de historial no está disponible en este momento.";
            case 408:
                return "La solicitud tardó demasiado tiempo. Verifica tu conexión e intenta nuevamente.";
            case 429:
                return "Demasiadas solicitudes. Por favor, espera un momento antes de intentar nuevamente.";
            case 500:
                return "Error interno del servidor. Nuestro equipo ha sido notificado. Intenta más tarde.";
            case 502:
                return "El servidor no está disponible temporalmente. Intenta más tarde.";
            case 503:
                return "El servicio está en mantenimiento. Por favor, intenta más tarde.";
            case 504:
                return "El servidor tardó demasiado en responder. Intenta nuevamente.";
            default:
                if (responseCode >= 500) {
                    return "Error del servidor (" + responseCode + "). Por favor, intenta más tarde.";
                } else if (responseCode >= 400) {
                    return "Error en la solicitud (" + responseCode + "). Verifica los datos e intenta nuevamente.";
                } else {
                    return "Error inesperado (" + responseCode + "). Por favor, intenta nuevamente.";
                }
        }
    }

    /**
     * Generates user-friendly error messages for network failures
     * @param throwable the exception that occurred
     * @return localized error message in Spanish
     */
    private String getNetworkErrorMessage(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            return "Sin conexión a internet. Verifica tu conexión WiFi o datos móviles e intenta nuevamente.";
        } else if (throwable instanceof SocketTimeoutException) {
            return "La conexión tardó demasiado tiempo. Verifica tu conexión e intenta nuevamente.";
        } else if (throwable instanceof ConnectException) {
            return "No se pudo conectar al servidor. Verifica tu conexión e intenta más tarde.";
        } else if (throwable instanceof IOException) {
            return "Error de conexión. Verifica tu conexión a internet e intenta nuevamente.";
        } else {
            String message = throwable.getMessage();
            if (message != null && message.toLowerCase().contains("timeout")) {
                return "La conexión tardó demasiado tiempo. Intenta nuevamente.";
            } else if (message != null && message.toLowerCase().contains("network")) {
                return "Error de red. Verifica tu conexión e intenta nuevamente.";
            } else {
                return "Error de conexión inesperado. Por favor, intenta nuevamente.";
            }
        }
    }
}