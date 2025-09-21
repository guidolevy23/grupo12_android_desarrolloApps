package com.example.ritmofit.data.repository;

import com.example.ritmofit.data.api.model.HistorialResponse;
import com.example.ritmofit.data.api.model.HistorialService;
import com.example.ritmofit.data.repository.impl.HistorialRepositoryImpl;
import com.example.ritmofit.model.HistorialItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HistorialRepositoryImpl
 */
public class HistorialRepositoryTest {

    @Mock
    private HistorialService mockHistorialService;

    @Mock
    private Call<List<HistorialResponse>> mockCall;

    private HistorialRepositoryImpl historialRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        historialRepository = new HistorialRepositoryImpl(mockHistorialService);
    }

    @Test
    public void testValidateDateRange_ValidRange_ReturnsNull() {
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 1, 31);

        String result = historialRepository.validateDateRange(fromDate, toDate);

        assertNull("Valid date range should return null", result);
    }

    @Test
    public void testValidateDateRange_InvalidRange_ReturnsErrorMessage() {
        LocalDate fromDate = LocalDate.of(2024, 1, 31);
        LocalDate toDate = LocalDate.of(2024, 1, 1);

        String result = historialRepository.validateDateRange(fromDate, toDate);

        assertNotNull("Invalid date range should return error message", result);
        assertTrue("Error message should be in Spanish", 
                   result.contains("fecha 'desde' no puede ser posterior"));
    }

    @Test
    public void testValidateDateRange_NullDates_ReturnsNull() {
        String result = historialRepository.validateDateRange(null, null);
        assertNull("Null dates should be valid", result);

        result = historialRepository.validateDateRange(LocalDate.now(), null);
        assertNull("Single null date should be valid", result);

        result = historialRepository.validateDateRange(null, LocalDate.now());
        assertNull("Single null date should be valid", result);
    }

    @Test
    public void testValidateDateRange_FutureDateTooFar_ReturnsErrorMessage() {
        LocalDate farFutureDate = LocalDate.now().plusYears(2);
        
        String result = historialRepository.validateDateRange(farFutureDate, null);
        
        assertNotNull("Far future date should return error message", result);
        assertTrue("Error message should mention future date limit", 
                   result.contains("más de un año en el futuro"));
    }

    @Test
    public void testIsNetworkAvailable_ReturnsTrue() {
        boolean result = historialRepository.isNetworkAvailable();
        assertTrue("Network should be available by default", result);
    }

    @Test
    public void testGetHistorial_SuccessfulResponse_CallsOnSuccess() {
        // Arrange
        List<HistorialResponse> mockResponses = createMockHistorialResponses();
        Response<List<HistorialResponse>> mockResponse = Response.success(mockResponses);
        
        when(mockHistorialService.getHistorial(anyString(), anyString())).thenReturn(mockCall);
        
        HistorialRepository.HistorialCallback mockCallback = mock(HistorialRepository.HistorialCallback.class);

        // Simulate successful response
        doAnswer(invocation -> {
            Callback<List<HistorialResponse>> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, mockResponse);
            return null;
        }).when(mockCall).enqueue(any(Callback.class));

        // Act
        historialRepository.getHistorial(LocalDate.now(), LocalDate.now(), mockCallback);

        // Assert
        verify(mockCallback).onSuccess(any(List.class));
        verify(mockCallback, never()).onError(anyString());
    }

    @Test
    public void testGetHistorial_FailedResponse_CallsOnError() {
        // Arrange
        Response<List<HistorialResponse>> mockResponse = Response.error(404, null);
        
        when(mockHistorialService.getHistorial(anyString(), anyString())).thenReturn(mockCall);
        
        HistorialRepository.HistorialCallback mockCallback = mock(HistorialRepository.HistorialCallback.class);

        // Simulate failed response
        doAnswer(invocation -> {
            Callback<List<HistorialResponse>> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, mockResponse);
            return null;
        }).when(mockCall).enqueue(any(Callback.class));

        // Act
        historialRepository.getHistorial(LocalDate.now(), LocalDate.now(), mockCallback);

        // Assert
        verify(mockCallback).onError(anyString());
        verify(mockCallback, never()).onSuccess(any(List.class));
    }

    @Test
    public void testGetHistorial_NetworkFailure_CallsOnError() {
        // Arrange
        when(mockHistorialService.getHistorial(anyString(), anyString())).thenReturn(mockCall);
        
        HistorialRepository.HistorialCallback mockCallback = mock(HistorialRepository.HistorialCallback.class);

        // Simulate network failure
        doAnswer(invocation -> {
            Callback<List<HistorialResponse>> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new java.net.UnknownHostException("Network error"));
            return null;
        }).when(mockCall).enqueue(any(Callback.class));

        // Act
        historialRepository.getHistorial(LocalDate.now(), LocalDate.now(), mockCallback);

        // Assert
        verify(mockCallback).onError(anyString());
        verify(mockCallback, never()).onSuccess(any(List.class));
    }

    @Test
    public void testGetCurrentMonthHistorial_CallsGetHistorialWithCurrentMonthDates() {
        // Arrange
        when(mockHistorialService.getHistorial(anyString(), anyString())).thenReturn(mockCall);
        
        HistorialRepository.HistorialCallback mockCallback = mock(HistorialRepository.HistorialCallback.class);

        // Act
        historialRepository.getCurrentMonthHistorial(mockCallback);

        // Assert
        verify(mockHistorialService).getHistorial(anyString(), anyString());
    }

    @Test
    public void testGetAllHistorial_CallsGetHistorialWithNullDates() {
        // Arrange
        when(mockHistorialService.getHistorial(null, null)).thenReturn(mockCall);
        
        HistorialRepository.HistorialCallback mockCallback = mock(HistorialRepository.HistorialCallback.class);

        // Act
        historialRepository.getAllHistorial(mockCallback);

        // Assert
        verify(mockHistorialService).getHistorial(null, null);
    }

    private List<HistorialResponse> createMockHistorialResponses() {
        List<HistorialResponse> responses = new ArrayList<>();
        
        HistorialResponse response1 = new HistorialResponse();
        response1.id = 1L;
        response1.clase = "Yoga";
        response1.sede = "Sede Centro";
        response1.fecha = "2024-01-15";
        response1.hora = "10:00";
        response1.duracion = 60;
        
        HistorialResponse response2 = new HistorialResponse();
        response2.id = 2L;
        response2.clase = "Pilates";
        response2.sede = "Sede Norte";
        response2.fecha = "2024-01-16";
        response2.hora = "14:30";
        response2.duracion = 45;
        
        responses.add(response1);
        responses.add(response2);
        
        return responses;
    }
}