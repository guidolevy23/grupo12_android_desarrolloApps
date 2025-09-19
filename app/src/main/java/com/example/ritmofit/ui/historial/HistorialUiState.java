package com.example.ritmofit.ui.historial;

import com.example.ritmofit.model.HistorialItem;

import java.util.List;

/**
 * Represents the different UI states for the Historial page.
 * This class encapsulates all possible states the UI can be in.
 */
public class HistorialUiState {
    
    public enum State {
        LOADING,    // Data is being fetched
        SUCCESS,    // Data loaded successfully
        ERROR,      // An error occurred
        EMPTY       // No data available
    }
    
    private final State state;
    private final List<HistorialItem> historialItems;
    private final String errorMessage;
    private final boolean isRefreshing;
    
    private HistorialUiState(State state, List<HistorialItem> historialItems, 
                           String errorMessage, boolean isRefreshing) {
        this.state = state;
        this.historialItems = historialItems;
        this.errorMessage = errorMessage;
        this.isRefreshing = isRefreshing;
    }
    
    // Factory methods for creating different states
    
    /**
     * Creates a loading state
     */
    public static HistorialUiState loading() {
        return new HistorialUiState(State.LOADING, null, null, false);
    }
    
    /**
     * Creates a loading state with refresh indicator
     */
    public static HistorialUiState refreshing() {
        return new HistorialUiState(State.LOADING, null, null, true);
    }
    
    /**
     * Creates a success state with data
     */
    public static HistorialUiState success(List<HistorialItem> historialItems) {
        if (historialItems == null || historialItems.isEmpty()) {
            return empty();
        }
        return new HistorialUiState(State.SUCCESS, historialItems, null, false);
    }
    
    /**
     * Creates an error state with error message
     */
    public static HistorialUiState error(String errorMessage) {
        return new HistorialUiState(State.ERROR, null, errorMessage, false);
    }
    
    /**
     * Creates an empty state (no data available)
     */
    public static HistorialUiState empty() {
        return new HistorialUiState(State.EMPTY, null, null, false);
    }
    
    // Getters
    
    public State getState() {
        return state;
    }
    
    public List<HistorialItem> getHistorialItems() {
        return historialItems;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public boolean isRefreshing() {
        return isRefreshing;
    }
    
    // Convenience methods for checking state
    
    public boolean isLoading() {
        return state == State.LOADING;
    }
    
    public boolean isSuccess() {
        return state == State.SUCCESS;
    }
    
    public boolean isError() {
        return state == State.ERROR;
    }
    
    public boolean isEmpty() {
        return state == State.EMPTY;
    }
    
    public boolean hasData() {
        return historialItems != null && !historialItems.isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        HistorialUiState that = (HistorialUiState) o;
        
        if (isRefreshing != that.isRefreshing) return false;
        if (state != that.state) return false;
        if (historialItems != null ? !historialItems.equals(that.historialItems) : that.historialItems != null)
            return false;
        return errorMessage != null ? errorMessage.equals(that.errorMessage) : that.errorMessage == null;
    }
    
    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (historialItems != null ? historialItems.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (isRefreshing ? 1 : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "HistorialUiState{" +
                "state=" + state +
                ", historialItems=" + (historialItems != null ? historialItems.size() + " items" : "null") +
                ", errorMessage='" + errorMessage + '\'' +
                ", isRefreshing=" + isRefreshing +
                '}';
    }
}