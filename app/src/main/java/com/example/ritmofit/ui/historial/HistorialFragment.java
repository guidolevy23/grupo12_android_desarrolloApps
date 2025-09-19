package com.example.ritmofit.ui.historial;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ritmofit.databinding.FragmentHistorialBinding;
import com.example.ritmofit.utils.DateUtils;

import java.time.LocalDate;
import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Fragment for displaying attendance history with date filtering capabilities.
 * Implements user interactions including date picker functionality, pull-to-refresh,
 * and comprehensive error handling with retry mechanisms.
 * 
 * Requirements covered:
 * - 1.1: Display attendance records with fecha, clase, sede
 * - 1.4: Empty state handling
 * - 1.5: Error handling and retry mechanisms
 * - 2.1: Date range filter controls
 * - 2.5: Date filter clearing functionality
 * - 4.4: Loading indicators and user-friendly interface
 */
@AndroidEntryPoint
public class HistorialFragment extends Fragment {
    
    private FragmentHistorialBinding binding;
    private HistorialViewModel viewModel;
    private HistorialAdapter adapter;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistorialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(HistorialViewModel.class);
        
        // Setup UI components
        setupRecyclerView();
        setupDatePickers();
        setupSwipeRefresh();
        setupFilterButtons();
        
        // Observe ViewModel state changes
        observeUiState();
        observeDateRange();
    }
    
    /**
     * Sets up the RecyclerView with adapter and layout manager
     */
    private void setupRecyclerView() {
        adapter = new HistorialAdapter();
        binding.rvHistorial.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvHistorial.setAdapter(adapter);
    }
    
    /**
     * Sets up date picker functionality for filtering
     * Implements requirement 2.1: Date range filter controls
     */
    private void setupDatePickers() {
        // From date picker
        binding.etFromDate.setOnClickListener(v -> showDatePicker(true));
        
        // To date picker
        binding.etToDate.setOnClickListener(v -> showDatePicker(false));
    }
    
    /**
     * Sets up pull-to-refresh functionality
     * Implements requirement 1.5: Refresh mechanism
     */
    private void setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refreshHistorialData();
        });
    }
    
    /**
     * Sets up filter action buttons
     * Implements requirement 2.5: Date filter clearing functionality
     */
    private void setupFilterButtons() {
        // Clear filter button
        binding.btnClearFilter.setOnClickListener(v -> {
            clearDateInputs();
            viewModel.clearDateFilter();
        });
        
        // Apply filter button (optional - filtering happens automatically on date selection)
        binding.btnApplyFilter.setOnClickListener(v -> {
            // Filter is applied automatically when dates are selected
            // This button can be used for manual refresh if needed
            viewModel.loadHistorialData();
        });
    }
    
    /**
     * Shows date picker dialog for from/to date selection
     * @param isFromDate true for from date, false for to date
     */
    private void showDatePicker(boolean isFromDate) {
        Calendar calendar = Calendar.getInstance();
        
        // Set initial date based on current selection or today
        LocalDate currentDate = isFromDate ? 
            viewModel.getCurrentFromDate() : 
            viewModel.getCurrentToDate();
            
        if (currentDate != null) {
            calendar.set(currentDate.getYear(), 
                        currentDate.getMonthValue() - 1, 
                        currentDate.getDayOfMonth());
        }
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            requireContext(),
            (DatePicker view, int year, int month, int dayOfMonth) -> {
                LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                
                if (isFromDate) {
                    handleFromDateSelection(selectedDate);
                } else {
                    handleToDateSelection(selectedDate);
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        // Set maximum date to today
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        
        datePickerDialog.show();
    }
    
    /**
     * Handles from date selection and updates ViewModel
     * @param selectedDate the selected from date
     */
    private void handleFromDateSelection(LocalDate selectedDate) {
        binding.etFromDate.setText(DateUtils.formatForDisplay(selectedDate));
        viewModel.setFromDate(selectedDate);
    }
    
    /**
     * Handles to date selection and updates ViewModel
     * @param selectedDate the selected to date
     */
    private void handleToDateSelection(LocalDate selectedDate) {
        binding.etToDate.setText(DateUtils.formatForDisplay(selectedDate));
        viewModel.setToDate(selectedDate);
    }
    
    /**
     * Clears date input fields
     */
    private void clearDateInputs() {
        binding.etFromDate.setText("");
        binding.etToDate.setText("");
    }
    
    /**
     * Observes UI state changes from ViewModel and updates UI accordingly
     * Implements requirements 1.4, 1.5, 4.4: State handling, error handling, loading indicators
     */
    private void observeUiState() {
        viewModel.uiState.observe(getViewLifecycleOwner(), uiState -> {
            if (uiState == null) return;
            
            // Handle refresh state
            binding.swipeRefreshLayout.setRefreshing(uiState.isRefreshing());
            
            switch (uiState.getState()) {
                case LOADING:
                    showLoadingState();
                    break;
                    
                case SUCCESS:
                    showSuccessState(uiState);
                    break;
                    
                case ERROR:
                    showErrorState(uiState.getErrorMessage());
                    break;
                    
                case EMPTY:
                    showEmptyState();
                    break;
            }
        });
    }
    
    /**
     * Observes date range changes and updates UI
     */
    private void observeDateRange() {
        viewModel.dateRange.observe(getViewLifecycleOwner(), dateRange -> {
            if (dateRange != null) {
                // Update date input fields if they're not already set
                LocalDate fromDate = dateRange.getFromDate();
                LocalDate toDate = dateRange.getToDate();
                
                if (fromDate != null && binding.etFromDate.getText().toString().isEmpty()) {
                    binding.etFromDate.setText(DateUtils.formatForDisplay(fromDate));
                }
                
                if (toDate != null && binding.etToDate.getText().toString().isEmpty()) {
                    binding.etToDate.setText(DateUtils.formatForDisplay(toDate));
                }
            }
        });
    }
    
    /**
     * Shows loading state UI
     */
    private void showLoadingState() {
        binding.rvHistorial.setVisibility(View.GONE);
        binding.layoutEmpty.getRoot().setVisibility(View.GONE);
        binding.layoutError.getRoot().setVisibility(View.GONE);
        binding.layoutLoading.getRoot().setVisibility(View.VISIBLE);
    }
    
    /**
     * Shows success state with data
     * @param uiState the UI state containing data
     */
    private void showSuccessState(HistorialUiState uiState) {
        binding.layoutLoading.getRoot().setVisibility(View.GONE);
        binding.layoutEmpty.getRoot().setVisibility(View.GONE);
        binding.layoutError.getRoot().setVisibility(View.GONE);
        binding.rvHistorial.setVisibility(View.VISIBLE);
        
        // Update adapter with new data
        adapter.updateHistorialList(uiState.getHistorialItems());
    }
    
    /**
     * Shows error state with retry mechanism
     * Implements requirement 1.5: Error handling and retry mechanisms
     * @param errorMessage the error message to display
     */
    private void showErrorState(String errorMessage) {
        binding.rvHistorial.setVisibility(View.GONE);
        binding.layoutEmpty.getRoot().setVisibility(View.GONE);
        binding.layoutLoading.getRoot().setVisibility(View.GONE);
        binding.layoutError.getRoot().setVisibility(View.VISIBLE);
        
        // Setup retry button if error layout has one
        View retryButton = binding.layoutError.getRoot().findViewById(com.example.ritmofit.R.id.btnRetry);
        if (retryButton != null) {
            retryButton.setOnClickListener(v -> viewModel.retry());
        }
    }
    
    /**
     * Shows empty state when no data is available
     * Implements requirement 1.4: Empty state handling
     */
    private void showEmptyState() {
        binding.rvHistorial.setVisibility(View.GONE);
        binding.layoutLoading.getRoot().setVisibility(View.GONE);
        binding.layoutError.getRoot().setVisibility(View.GONE);
        binding.layoutEmpty.getRoot().setVisibility(View.VISIBLE);
        
        // Setup refresh button in empty state
        View refreshButton = binding.layoutEmpty.getRoot().findViewById(com.example.ritmofit.R.id.btnRefreshEmpty);
        if (refreshButton != null) {
            refreshButton.setOnClickListener(v -> viewModel.refreshHistorialData());
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}