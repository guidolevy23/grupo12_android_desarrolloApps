package com.example.ritmofit.ui.historial;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ritmofit.data.repository.HistorialRepository;
import com.example.ritmofit.model.DateRange;
import com.example.ritmofit.model.HistorialItem;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the Historial page that manages UI state and business logic.
 * Handles date filtering, data loading, and error states.
 */
@HiltViewModel
public class HistorialViewModel extends ViewModel {
    
    private final HistorialRepository historialRepository;
    
    // LiveData for UI state observation
    private final MutableLiveData<HistorialUiState> _uiState = new MutableLiveData<>();
    public final LiveData<HistorialUiState> uiState = _uiState;
    
    // Current date range filter
    private final MutableLiveData<DateRange> _dateRange = new MutableLiveData<>();
    public final LiveData<DateRange> dateRange = _dateRange;
    
    // All loaded historial items (before filtering)
    private List<HistorialItem> allHistorialItems;
    
    @Inject
    public HistorialViewModel(HistorialRepository historialRepository) {
        this.historialRepository = historialRepository;
        
        // Initialize with current month date range
        _dateRange.setValue(DateRange.currentMonth());
        
        // Load initial data
        loadHistorialData();
    }
    
    /**
     * Loads historial data based on current date range filter
     */
    public void loadHistorialData() {
        // Check network availability before loading
        if (!isNetworkAvailable()) {
            _uiState.setValue(HistorialUiState.error("Sin conexión a internet. Verifica tu conexión y vuelve a intentar."));
            return;
        }
        
        _uiState.setValue(HistorialUiState.loading());
        
        DateRange currentRange = _dateRange.getValue();
        LocalDate fromDate = currentRange != null ? currentRange.getFromDate() : null;
        LocalDate toDate = currentRange != null ? currentRange.getToDate() : null;
        
        historialRepository.getHistorial(fromDate, toDate, new HistorialRepository.HistorialCallback() {
            @Override
            public void onSuccess(List<HistorialItem> historialItems) {
                allHistorialItems = historialItems;
                applyDateFilter();
            }
            
            @Override
            public void onError(String errorMessage) {
                _uiState.setValue(HistorialUiState.error(errorMessage));
            }
        });
    }
    
    /**
     * Refreshes the historial data (for pull-to-refresh)
     */
    public void refreshHistorialData() {
        _uiState.setValue(HistorialUiState.refreshing());
        
        DateRange currentRange = _dateRange.getValue();
        LocalDate fromDate = currentRange != null ? currentRange.getFromDate() : null;
        LocalDate toDate = currentRange != null ? currentRange.getToDate() : null;
        
        historialRepository.getHistorial(fromDate, toDate, new HistorialRepository.HistorialCallback() {
            @Override
            public void onSuccess(List<HistorialItem> historialItems) {
                allHistorialItems = historialItems;
                applyDateFilter();
            }
            
            @Override
            public void onError(String errorMessage) {
                _uiState.setValue(HistorialUiState.error(errorMessage));
            }
        });
    }
    
    /**
     * Sets the from date for filtering
     */
    public void setFromDate(LocalDate fromDate) {
        DateRange currentRange = _dateRange.getValue();
        LocalDate toDate = currentRange != null ? currentRange.getToDate() : null;
        
        DateRange newRange = new DateRange(fromDate, toDate);
        String validationError = validateDateRange(newRange);
        
        if (validationError != null) {
            _uiState.setValue(HistorialUiState.error(validationError));
            return;
        }
        
        _dateRange.setValue(newRange);
        applyDateFilter();
    }
    
    /**
     * Sets the to date for filtering
     */
    public void setToDate(LocalDate toDate) {
        DateRange currentRange = _dateRange.getValue();
        LocalDate fromDate = currentRange != null ? currentRange.getFromDate() : null;
        
        DateRange newRange = new DateRange(fromDate, toDate);
        String validationError = validateDateRange(newRange);
        
        if (validationError != null) {
            _uiState.setValue(HistorialUiState.error(validationError));
            return;
        }
        
        _dateRange.setValue(newRange);
        applyDateFilter();
    }
    
    /**
     * Sets both from and to dates for filtering
     */
    public void setDateRange(LocalDate fromDate, LocalDate toDate) {
        DateRange newRange = new DateRange(fromDate, toDate);
        String validationError = validateDateRange(newRange);
        
        if (validationError != null) {
            _uiState.setValue(HistorialUiState.error(validationError));
            return;
        }
        
        _dateRange.setValue(newRange);
        applyDateFilter();
    }
    
    /**
     * Clears the date filter and shows current month data
     */
    public void clearDateFilter() {
        _dateRange.setValue(DateRange.currentMonth());
        loadHistorialData();
    }
    
    /**
     * Applies the current date filter to the loaded data
     */
    private void applyDateFilter() {
        if (allHistorialItems == null) {
            _uiState.setValue(HistorialUiState.empty());
            return;
        }
        
        DateRange currentRange = _dateRange.getValue();
        if (currentRange == null || !currentRange.hasFilter()) {
            _uiState.setValue(HistorialUiState.success(allHistorialItems));
            return;
        }
        
        List<HistorialItem> filteredItems = allHistorialItems.stream()
                .filter(item -> currentRange.contains(item.getFecha()))
                .collect(Collectors.toList());
        
        _uiState.setValue(HistorialUiState.success(filteredItems));
    }
    
    /**
     * Validates the date range
     */
    private String validateDateRange(DateRange dateRange) {
        if (dateRange == null) {
            return null;
        }
        
        String repositoryValidation = historialRepository.validateDateRange(
                dateRange.getFromDate(), 
                dateRange.getToDate()
        );
        
        if (repositoryValidation != null) {
            return repositoryValidation;
        }
        
        return dateRange.getValidationError();
    }
    
    /**
     * Retries the last failed operation
     */
    public void retry() {
        loadHistorialData();
    }
    
    /**
     * Checks if network is available for operations
     */
    public boolean isNetworkAvailable() {
        return historialRepository.isNetworkAvailable();
    }
    
    /**
     * Gets the current from date
     */
    public LocalDate getCurrentFromDate() {
        DateRange currentRange = _dateRange.getValue();
        return currentRange != null ? currentRange.getFromDate() : null;
    }
    
    /**
     * Gets the current to date
     */
    public LocalDate getCurrentToDate() {
        DateRange currentRange = _dateRange.getValue();
        return currentRange != null ? currentRange.getToDate() : null;
    }
    
    /**
     * Checks if any date filter is currently applied
     */
    public boolean hasDateFilter() {
        DateRange currentRange = _dateRange.getValue();
        return currentRange != null && currentRange.hasFilter();
    }
    
    /**
     * Gets a user-friendly description of the current filter
     */
    public String getFilterDescription() {
        DateRange currentRange = _dateRange.getValue();
        if (currentRange == null || !currentRange.hasFilter()) {
            return "Mes actual";
        }
        
        LocalDate fromDate = currentRange.getFromDate();
        LocalDate toDate = currentRange.getToDate();
        
        if (fromDate != null && toDate != null) {
            return "Desde " + fromDate + " hasta " + toDate;
        } else if (fromDate != null) {
            return "Desde " + fromDate;
        } else if (toDate != null) {
            return "Hasta " + toDate;
        }
        
        return "Sin filtro";
    }
}