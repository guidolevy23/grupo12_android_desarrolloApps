package com.example.ritmofit.data.repository;

import com.example.ritmofit.model.HistorialItem;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing historial (attendance history) data.
 * Defines the contract for historial data operations.
 */
public interface HistorialRepository {

    /**
     * DomainCallback interface for historial data operations
     */
    interface HistorialCallback {
        void onSuccess(List<HistorialItem> historialItems);
        void onError(String errorMessage);
    }

    /**
     * Fetches historial data from the API with optional date range filtering
     * @param fromDate start date for filtering (can be null)
     * @param toDate end date for filtering (can be null)
     * @param callback callback to handle success/error responses
     */
    void getHistorial(LocalDate fromDate, LocalDate toDate, HistorialCallback callback);

    /**
     * Fetches historial data for the current month
     * @param callback callback to handle success/error responses
     */
    void getCurrentMonthHistorial(HistorialCallback callback);

    /**
     * Fetches all historial data without date filtering
     * @param callback callback to handle success/error responses
     */
    void getAllHistorial(HistorialCallback callback);

    /**
     * Validates date range parameters before making API calls
     * @param fromDate start date
     * @param toDate end date
     * @return validation error message or null if valid
     */
    String validateDateRange(LocalDate fromDate, LocalDate toDate);

    /**
     * Checks if the repository can make network requests
     * @return true if network operations are possible
     */
    boolean isNetworkAvailable();
}