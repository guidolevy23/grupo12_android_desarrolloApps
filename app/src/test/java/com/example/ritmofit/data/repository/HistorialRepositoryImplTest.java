package com.example.ritmofit.data.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.ritmofit.data.api.model.HistorialResponse;
import com.example.ritmofit.data.api.model.HistorialService;
import com.example.ritmofit.data.repository.impl.HistorialRepositoryImpl;
import com.example.ritmofit.model.HistorialItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HistorialRepositoryImpl class.
 * Tests API integration, error handling, and data transformation.
 */
@RunWith(RobolectricTestRunner.class)
public class HistorialRepositoryImplTest {

    @Mock
    private HistorialService mockHistorialService;

    @Mock
    private Context mockContext;

    @Mock
    private ConnectivityManager mockConnectivityManager;

    @Mock
    private NetworkInfo mockNetworkInfo;

    @Mock
    private Call<List<HistorialResponse>> mockCall;

    @Mock
    private HistorialRepository.HistorialCallback mockCallback;

    private HistorialRepositoryImpl repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Mock context and connectivity manager
        when(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(mockConnectivityManager);
        when(mockConnectivityManager.getActiveNetworkInfo())
            .thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(true);
        
        repository = new HistorialRepositoryImpl(mockHistorialService, mockContext);
    }

    @Test
    public void getHistorial_networkUnavailable_callsOnError() {
        // Given
        when(mockNetworkInfo.isConnected()).thenReturn(false);
        
        // When
        repository.getHistorial(null, null, mockCallback);
        
        // Then
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockCallback).onError(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("Sin conexión"));
        verify(mockHistorialService, never()).getHistorial(any(), any());
    }

    @Test
    public void getHistorial_validationError_callsOnError() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 31);
        LocalDate toDate = LocalDate.of(2024, 12, 1);
        
        // When
        repository.getHistorial(fromDate, toDate, mockCallback);
        
        // Then
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockCallback).onError(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("fecha 'desde' no puede ser posterior"));
        verify(mockHistorialService, never()).getHistorial(any(), any());
    }

    @Test
    public void getHistorial_successfulResponse_callsOnSuccess() {
        // Given
        List<HistorialResponse> mockResponses = createSampleHistorialResponses();
        Response<List<HistorialResponse>> successResponse = Response.success(mockResponses);
        
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getHistorial(null, null, mockCallback);
        
        // Capture and trigger the callback
        ArgumentCaptor<Callback<List<HistorialResponse>>> callbackCaptor = 
            ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(mockCall, successResponse);
        
        // Then
        ArgumentCaptor<List<HistorialItem>> itemsCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockCallback).onSuccess(itemsCaptor.capture());
        
        List<HistorialItem> items = itemsCaptor.getValue();
        assertEquals(2, items.size());
        assertEquals("Yoga", items.get(0).getClase());
        assertEquals("Pilates", items.get(1).getClase());
    }

    @Test
    public void getHistorial_emptyResponse_callsOnSuccessWithEmptyList() {
        // Given
        Response<List<HistorialResponse>> emptyResponse = Response.success(null);
        
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getHistorial(null, null, mockCallback);
        
        // Capture and trigger the callback
        ArgumentCaptor<Callback<List<HistorialResponse>>> callbackCaptor = 
            ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(mockCall, emptyResponse);
        
        // Then
        ArgumentCaptor<List<HistorialItem>> itemsCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockCallback).onSuccess(itemsCaptor.capture());
        
        List<HistorialItem> items = itemsCaptor.getValue();
        assertTrue(items.isEmpty());
    }

    @Test
    public void getHistorial_errorResponse_callsOnError() {
        // Given
        Response<List<HistorialResponse>> errorResponse = Response.error(404, 
            okhttp3.ResponseBody.create(null, "Not Found"));
        
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getHistorial(null, null, mockCallback);
        
        // Capture and trigger the callback
        ArgumentCaptor<Callback<List<HistorialResponse>>> callbackCaptor = 
            ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(mockCall, errorResponse);
        
        // Then
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockCallback).onError(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("servicio de historial no está disponible"));
    }

    @Test
    public void getHistorial_networkFailure_callsOnError() {
        // Given
        UnknownHostException networkException = new UnknownHostException("No internet");
        
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getHistorial(null, null, mockCallback);
        
        // Capture and trigger the callback
        ArgumentCaptor<Callback<List<HistorialResponse>>> callbackCaptor = 
            ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(mockCall, networkException);
        
        // Then
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockCallback).onError(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("Sin conexión a internet"));
    }

    @Test
    public void getCurrentMonthHistorial_callsGetHistorialWithCurrentMonthDates() {
        // Given
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getCurrentMonthHistorial(mockCallback);
        
        // Then
        ArgumentCaptor<String> fromDateCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> toDateCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockHistorialService).getHistorial(fromDateCaptor.capture(), toDateCaptor.capture());
        
        // Verify dates are for current month
        LocalDate now = LocalDate.now();
        String expectedFromDate = now.withDayOfMonth(1).toString();
        String expectedToDate = now.withDayOfMonth(now.lengthOfMonth()).toString();
        
        assertEquals(expectedFromDate, fromDateCaptor.getValue());
        assertEquals(expectedToDate, toDateCaptor.getValue());
    }

    @Test
    public void getAllHistorial_callsGetHistorialWithNullDates() {
        // Given
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getAllHistorial(mockCallback);
        
        // Then
        verify(mockHistorialService).getHistorial(null, null);
    }

    @Test
    public void validateDateRange_validRange_returnsNull() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        
        // When
        String result = repository.validateDateRange(fromDate, toDate);
        
        // Then
        assertNull(result);
    }

    @Test
    public void validateDateRange_invalidRange_returnsErrorMessage() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 31);
        LocalDate toDate = LocalDate.of(2024, 12, 1);
        
        // When
        String result = repository.validateDateRange(fromDate, toDate);
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("fecha 'desde' no puede ser posterior"));
    }

    @Test
    public void validateDateRange_futureFromDate_returnsErrorMessage() {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalDate toDate = LocalDate.now();
        
        // When
        String result = repository.validateDateRange(futureDate, toDate);
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("fecha 'desde' no puede ser una fecha futura"));
    }

    @Test
    public void validateDateRange_futureToDate_returnsErrorMessage() {
        // Given
        LocalDate fromDate = LocalDate.now();
        LocalDate futureDate = LocalDate.now().plusDays(1);
        
        // When
        String result = repository.validateDateRange(fromDate, futureDate);
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("fecha 'hasta' no puede ser una fecha futura"));
    }

    @Test
    public void validateDateRange_tooOldFromDate_returnsErrorMessage() {
        // Given
        LocalDate oldDate = LocalDate.now().minusYears(6);
        LocalDate toDate = LocalDate.now();
        
        // When
        String result = repository.validateDateRange(oldDate, toDate);
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("no puede ser anterior a"));
    }

    @Test
    public void validateDateRange_tooWideRange_returnsErrorMessage() {
        // Given
        LocalDate fromDate = LocalDate.now().minusYears(3);
        LocalDate toDate = LocalDate.now();
        
        // When
        String result = repository.validateDateRange(fromDate, toDate);
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("rango de fechas no puede ser mayor a 2 años"));
    }

    @Test
    public void isNetworkAvailable_connectedNetwork_returnsTrue() {
        // Given
        when(mockNetworkInfo.isConnected()).thenReturn(true);
        
        // When
        boolean result = repository.isNetworkAvailable();
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isNetworkAvailable_disconnectedNetwork_returnsFalse() {
        // Given
        when(mockNetworkInfo.isConnected()).thenReturn(false);
        
        // When
        boolean result = repository.isNetworkAvailable();
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isNetworkAvailable_nullNetworkInfo_returnsFalse() {
        // Given
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(null);
        
        // When
        boolean result = repository.isNetworkAvailable();
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isNetworkAvailable_nullConnectivityManager_returnsFalse() {
        // Given
        when(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(null);
        
        // When
        boolean result = repository.isNetworkAvailable();
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isNetworkAvailable_exception_returnsTrue() {
        // Given
        when(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenThrow(new RuntimeException("Test exception"));
        
        // When
        boolean result = repository.isNetworkAvailable();
        
        // Then
        assertTrue(result); // Should return true when can't check network status
    }

    @Test
    public void getHistorial_withDateRange_passesCorrectDatesToService() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getHistorial(fromDate, toDate, mockCallback);
        
        // Then
        verify(mockHistorialService).getHistorial("2024-12-01", "2024-12-31");
    }

    @Test
    public void networkErrorHandling_socketTimeoutException_returnsCorrectMessage() {
        // Given
        SocketTimeoutException timeoutException = new SocketTimeoutException("Timeout");
        
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getHistorial(null, null, mockCallback);
        
        ArgumentCaptor<Callback<List<HistorialResponse>>> callbackCaptor = 
            ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(mockCall, timeoutException);
        
        // Then
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockCallback).onError(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("conexión tardó demasiado"));
    }

    @Test
    public void networkErrorHandling_connectException_returnsCorrectMessage() {
        // Given
        ConnectException connectException = new ConnectException("Connection refused");
        
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getHistorial(null, null, mockCallback);
        
        ArgumentCaptor<Callback<List<HistorialResponse>>> callbackCaptor = 
            ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(mockCall, connectException);
        
        // Then
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockCallback).onError(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("No se pudo conectar al servidor"));
    }

    @Test
    public void httpErrorHandling_401Unauthorized_returnsCorrectMessage() {
        // Given
        Response<List<HistorialResponse>> unauthorizedResponse = Response.error(401, 
            okhttp3.ResponseBody.create(null, "Unauthorized"));
        
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getHistorial(null, null, mockCallback);
        
        ArgumentCaptor<Callback<List<HistorialResponse>>> callbackCaptor = 
            ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(mockCall, unauthorizedResponse);
        
        // Then
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockCallback).onError(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("sesión ha expirado"));
    }

    @Test
    public void httpErrorHandling_500ServerError_returnsCorrectMessage() {
        // Given
        Response<List<HistorialResponse>> serverErrorResponse = Response.error(500, 
            okhttp3.ResponseBody.create(null, "Internal Server Error"));
        
        when(mockHistorialService.getHistorial(any(), any())).thenReturn(mockCall);
        
        // When
        repository.getHistorial(null, null, mockCallback);
        
        ArgumentCaptor<Callback<List<HistorialResponse>>> callbackCaptor = 
            ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(mockCall, serverErrorResponse);
        
        // Then
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockCallback).onError(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("Error interno del servidor"));
    }

    /**
     * Helper method to create sample HistorialResponse objects for testing
     */
    private List<HistorialResponse> createSampleHistorialResponses() {
        HistorialResponse response1 = new HistorialResponse();
        response1.id = 1L;
        response1.clase = "Yoga";
        response1.sede = "Sede Centro";
        response1.fecha = "2024-12-15";
        response1.hora = "10:00";
        response1.duracion = 60;
        
        HistorialResponse response2 = new HistorialResponse();
        response2.id = 2L;
        response2.clase = "Pilates";
        response2.sede = "Sede Norte";
        response2.fecha = "2024-12-14";
        response2.hora = "18:30";
        response2.duracion = 45;
        
        return Arrays.asList(response1, response2);
    }
}