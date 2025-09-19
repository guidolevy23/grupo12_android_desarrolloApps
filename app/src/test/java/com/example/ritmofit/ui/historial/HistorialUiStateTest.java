package com.example.ritmofit.ui.historial;

import com.example.ritmofit.model.HistorialItem;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for HistorialUiState class.
 * Tests state creation, transitions, and convenience methods.
 */
public class HistorialUiStateTest {

    @Test
    public void loading_createsLoadingState() {
        // When
        HistorialUiState state = HistorialUiState.loading();
        
        // Then
        assertEquals(HistorialUiState.State.LOADING, state.getState());
        assertTrue(state.isLoading());
        assertFalse(state.isRefreshing());
        assertNull(state.getHistorialItems());
        assertNull(state.getErrorMessage());
        assertFalse(state.hasData());
    }

    @Test
    public void refreshing_createsRefreshingState() {
        // When
        HistorialUiState state = HistorialUiState.refreshing();
        
        // Then
        assertEquals(HistorialUiState.State.LOADING, state.getState());
        assertTrue(state.isLoading());
        assertTrue(state.isRefreshing());
        assertNull(state.getHistorialItems());
        assertNull(state.getErrorMessage());
        assertFalse(state.hasData());
    }

    @Test
    public void success_withData_createsSuccessState() {
        // Given
        List<HistorialItem> items = createSampleHistorialItems();
        
        // When
        HistorialUiState state = HistorialUiState.success(items);
        
        // Then
        assertEquals(HistorialUiState.State.SUCCESS, state.getState());
        assertTrue(state.isSuccess());
        assertFalse(state.isLoading());
        assertFalse(state.isRefreshing());
        assertEquals(items, state.getHistorialItems());
        assertNull(state.getErrorMessage());
        assertTrue(state.hasData());
    }

    @Test
    public void success_withEmptyList_createsEmptyState() {
        // Given
        List<HistorialItem> emptyList = new ArrayList<>();
        
        // When
        HistorialUiState state = HistorialUiState.success(emptyList);
        
        // Then
        assertEquals(HistorialUiState.State.EMPTY, state.getState());
        assertTrue(state.isEmpty());
        assertFalse(state.isSuccess());
        assertNull(state.getHistorialItems());
        assertNull(state.getErrorMessage());
        assertFalse(state.hasData());
    }

    @Test
    public void success_withNullList_createsEmptyState() {
        // When
        HistorialUiState state = HistorialUiState.success(null);
        
        // Then
        assertEquals(HistorialUiState.State.EMPTY, state.getState());
        assertTrue(state.isEmpty());
        assertFalse(state.isSuccess());
        assertNull(state.getHistorialItems());
        assertNull(state.getErrorMessage());
        assertFalse(state.hasData());
    }

    @Test
    public void error_createsErrorState() {
        // Given
        String errorMessage = "Network error occurred";
        
        // When
        HistorialUiState state = HistorialUiState.error(errorMessage);
        
        // Then
        assertEquals(HistorialUiState.State.ERROR, state.getState());
        assertTrue(state.isError());
        assertFalse(state.isLoading());
        assertFalse(state.isRefreshing());
        assertNull(state.getHistorialItems());
        assertEquals(errorMessage, state.getErrorMessage());
        assertFalse(state.hasData());
    }

    @Test
    public void empty_createsEmptyState() {
        // When
        HistorialUiState state = HistorialUiState.empty();
        
        // Then
        assertEquals(HistorialUiState.State.EMPTY, state.getState());
        assertTrue(state.isEmpty());
        assertFalse(state.isLoading());
        assertFalse(state.isRefreshing());
        assertNull(state.getHistorialItems());
        assertNull(state.getErrorMessage());
        assertFalse(state.hasData());
    }

    @Test
    public void equals_sameStates_returnsTrue() {
        // Given
        HistorialUiState state1 = HistorialUiState.loading();
        HistorialUiState state2 = HistorialUiState.loading();
        
        // When & Then
        assertEquals(state1, state2);
    }

    @Test
    public void equals_differentStates_returnsFalse() {
        // Given
        HistorialUiState loadingState = HistorialUiState.loading();
        HistorialUiState errorState = HistorialUiState.error("Error");
        
        // When & Then
        assertNotEquals(loadingState, errorState);
    }

    @Test
    public void equals_sameSuccessStates_returnsTrue() {
        // Given
        List<HistorialItem> items = createSampleHistorialItems();
        HistorialUiState state1 = HistorialUiState.success(items);
        HistorialUiState state2 = HistorialUiState.success(items);
        
        // When & Then
        assertEquals(state1, state2);
    }

    @Test
    public void hashCode_sameStates_returnsSameHashCode() {
        // Given
        HistorialUiState state1 = HistorialUiState.loading();
        HistorialUiState state2 = HistorialUiState.loading();
        
        // When & Then
        assertEquals(state1.hashCode(), state2.hashCode());
    }

    @Test
    public void toString_containsStateInformation() {
        // Given
        List<HistorialItem> items = createSampleHistorialItems();
        HistorialUiState state = HistorialUiState.success(items);
        
        // When
        String result = state.toString();
        
        // Then
        assertTrue(result.contains("SUCCESS"));
        assertTrue(result.contains("2 items"));
    }

    @Test
    public void toString_errorState_containsErrorMessage() {
        // Given
        String errorMessage = "Network error";
        HistorialUiState state = HistorialUiState.error(errorMessage);
        
        // When
        String result = state.toString();
        
        // Then
        assertTrue(result.contains("ERROR"));
        assertTrue(result.contains(errorMessage));
    }

    @Test
    public void stateTransitions_loadingToSuccess() {
        // Given
        HistorialUiState loadingState = HistorialUiState.loading();
        List<HistorialItem> items = createSampleHistorialItems();
        
        // When
        HistorialUiState successState = HistorialUiState.success(items);
        
        // Then
        assertTrue(loadingState.isLoading());
        assertTrue(successState.isSuccess());
        assertNotEquals(loadingState, successState);
    }

    @Test
    public void stateTransitions_loadingToError() {
        // Given
        HistorialUiState loadingState = HistorialUiState.loading();
        String errorMessage = "API error";
        
        // When
        HistorialUiState errorState = HistorialUiState.error(errorMessage);
        
        // Then
        assertTrue(loadingState.isLoading());
        assertTrue(errorState.isError());
        assertEquals(errorMessage, errorState.getErrorMessage());
        assertNotEquals(loadingState, errorState);
    }

    @Test
    public void stateTransitions_successToRefreshing() {
        // Given
        List<HistorialItem> items = createSampleHistorialItems();
        HistorialUiState successState = HistorialUiState.success(items);
        
        // When
        HistorialUiState refreshingState = HistorialUiState.refreshing();
        
        // Then
        assertTrue(successState.isSuccess());
        assertTrue(refreshingState.isLoading());
        assertTrue(refreshingState.isRefreshing());
        assertNotEquals(successState, refreshingState);
    }

    @Test
    public void convenienceMethods_allStatesWork() {
        // Given
        HistorialUiState loadingState = HistorialUiState.loading();
        HistorialUiState successState = HistorialUiState.success(createSampleHistorialItems());
        HistorialUiState errorState = HistorialUiState.error("Error");
        HistorialUiState emptyState = HistorialUiState.empty();
        
        // Then - Loading state
        assertTrue(loadingState.isLoading());
        assertFalse(loadingState.isSuccess());
        assertFalse(loadingState.isError());
        assertFalse(loadingState.isEmpty());
        
        // Then - Success state
        assertFalse(successState.isLoading());
        assertTrue(successState.isSuccess());
        assertFalse(successState.isError());
        assertFalse(successState.isEmpty());
        
        // Then - Error state
        assertFalse(errorState.isLoading());
        assertFalse(errorState.isSuccess());
        assertTrue(errorState.isError());
        assertFalse(errorState.isEmpty());
        
        // Then - Empty state
        assertFalse(emptyState.isLoading());
        assertFalse(emptyState.isSuccess());
        assertFalse(emptyState.isError());
        assertTrue(emptyState.isEmpty());
    }

    /**
     * Helper method to create sample HistorialItem objects for testing
     */
    private List<HistorialItem> createSampleHistorialItems() {
        HistorialItem item1 = new HistorialItem(
            1L, 
            "Yoga", 
            "Sede Centro", 
            LocalDate.of(2024, 12, 15), 
            LocalTime.of(10, 0), 
            60
        );
        
        HistorialItem item2 = new HistorialItem(
            2L, 
            "Pilates", 
            "Sede Norte", 
            LocalDate.of(2024, 12, 14), 
            LocalTime.of(18, 30), 
            45
        );
        
        return Arrays.asList(item1, item2);
    }
}