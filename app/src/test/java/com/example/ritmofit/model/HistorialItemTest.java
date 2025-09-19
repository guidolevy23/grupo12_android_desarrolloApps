package com.example.ritmofit.model;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Unit tests for HistorialItem model class.
 * Tests date formatting, duration formatting, and utility methods.
 */
public class HistorialItemTest {

    @Test
    public void constructor_noArgs_createsEmptyItem() {
        // When
        HistorialItem item = new HistorialItem();
        
        // Then
        assertNull(item.getId());
        assertNull(item.getClase());
        assertNull(item.getSede());
        assertNull(item.getFecha());
        assertNull(item.getHora());
        assertEquals(0, item.getDuracion());
    }

    @Test
    public void constructor_withArgs_createsProperlyInitializedItem() {
        // Given
        Long id = 1L;
        String clase = "Yoga";
        String sede = "Sede Centro";
        LocalDate fecha = LocalDate.of(2024, 12, 15);
        LocalTime hora = LocalTime.of(10, 30);
        int duracion = 60;
        
        // When
        HistorialItem item = new HistorialItem(id, clase, sede, fecha, hora, duracion);
        
        // Then
        assertEquals(id, item.getId());
        assertEquals(clase, item.getClase());
        assertEquals(sede, item.getSede());
        assertEquals(fecha, item.getFecha());
        assertEquals(hora, item.getHora());
        assertEquals(duracion, item.getDuracion());
    }

    @Test
    public void getFormattedDate_validDate_returnsSpanishFormat() {
        // Given
        LocalDate fecha = LocalDate.of(2024, 12, 15);
        HistorialItem item = new HistorialItem();
        item.setFecha(fecha);
        
        // When
        String result = item.getFormattedDate();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("15"));
        assertTrue(result.contains("diciembre"));
        assertTrue(result.contains("2024"));
    }

    @Test
    public void getFormattedDate_nullDate_returnsEmptyString() {
        // Given
        HistorialItem item = new HistorialItem();
        item.setFecha(null);
        
        // When
        String result = item.getFormattedDate();
        
        // Then
        assertEquals("", result);
    }

    @Test
    public void getFormattedTime_validTime_returnsFormattedTime() {
        // Given
        LocalTime hora = LocalTime.of(14, 30);
        HistorialItem item = new HistorialItem();
        item.setHora(hora);
        
        // When
        String result = item.getFormattedTime();
        
        // Then
        assertEquals("14:30", result);
    }

    @Test
    public void getFormattedTime_nullTime_returnsEmptyString() {
        // Given
        HistorialItem item = new HistorialItem();
        item.setHora(null);
        
        // When
        String result = item.getFormattedTime();
        
        // Then
        assertEquals("", result);
    }

    @Test
    public void getApiFormattedDate_validDate_returnsIsoFormat() {
        // Given
        LocalDate fecha = LocalDate.of(2024, 12, 15);
        HistorialItem item = new HistorialItem();
        item.setFecha(fecha);
        
        // When
        String result = item.getApiFormattedDate();
        
        // Then
        assertEquals("2024-12-15", result);
    }

    @Test
    public void getApiFormattedDate_nullDate_returnsEmptyString() {
        // Given
        HistorialItem item = new HistorialItem();
        item.setFecha(null);
        
        // When
        String result = item.getApiFormattedDate();
        
        // Then
        assertEquals("", result);
    }

    @Test
    public void getFormattedDuration_hoursAndMinutes_returnsCorrectFormat() {
        // Given
        HistorialItem item = new HistorialItem();
        item.setDuracion(90); // 1h 30min
        
        // When
        String result = item.getFormattedDuration();
        
        // Then
        assertEquals("1h 30min", result);
    }

    @Test
    public void getFormattedDuration_onlyHours_returnsCorrectFormat() {
        // Given
        HistorialItem item = new HistorialItem();
        item.setDuracion(120); // 2h
        
        // When
        String result = item.getFormattedDuration();
        
        // Then
        assertEquals("2h", result);
    }

    @Test
    public void getFormattedDuration_onlyMinutes_returnsCorrectFormat() {
        // Given
        HistorialItem item = new HistorialItem();
        item.setDuracion(45); // 45min
        
        // When
        String result = item.getFormattedDuration();
        
        // Then
        assertEquals("45min", result);
    }

    @Test
    public void getFormattedDuration_zeroDuration_returnsZeroMin() {
        // Given
        HistorialItem item = new HistorialItem();
        item.setDuracion(0);
        
        // When
        String result = item.getFormattedDuration();
        
        // Then
        assertEquals("0min", result);
    }

    @Test
    public void getFormattedDuration_negativeDuration_returnsZeroMin() {
        // Given
        HistorialItem item = new HistorialItem();
        item.setDuracion(-30);
        
        // When
        String result = item.getFormattedDuration();
        
        // Then
        assertEquals("0min", result);
    }

    @Test
    public void isWithinDateRange_dateInRange_returnsTrue() {
        // Given
        LocalDate fecha = LocalDate.of(2024, 12, 15);
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        
        HistorialItem item = new HistorialItem();
        item.setFecha(fecha);
        
        // When
        boolean result = item.isWithinDateRange(fromDate, toDate);
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isWithinDateRange_dateOutOfRange_returnsFalse() {
        // Given
        LocalDate fecha = LocalDate.of(2025, 1, 15);
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        
        HistorialItem item = new HistorialItem();
        item.setFecha(fecha);
        
        // When
        boolean result = item.isWithinDateRange(fromDate, toDate);
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isWithinDateRange_dateOnBoundary_returnsTrue() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        
        HistorialItem item1 = new HistorialItem();
        item1.setFecha(fromDate);
        
        HistorialItem item2 = new HistorialItem();
        item2.setFecha(toDate);
        
        // When & Then
        assertTrue(item1.isWithinDateRange(fromDate, toDate));
        assertTrue(item2.isWithinDateRange(fromDate, toDate));
    }

    @Test
    public void isWithinDateRange_nullItemDate_returnsFalse() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        
        HistorialItem item = new HistorialItem();
        item.setFecha(null);
        
        // When
        boolean result = item.isWithinDateRange(fromDate, toDate);
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isWithinDateRange_nullRangeDates_returnsTrue() {
        // Given
        LocalDate fecha = LocalDate.of(2024, 12, 15);
        HistorialItem item = new HistorialItem();
        item.setFecha(fecha);
        
        // When & Then
        assertTrue(item.isWithinDateRange(null, null));
        assertTrue(item.isWithinDateRange(null, LocalDate.of(2024, 12, 31)));
        assertTrue(item.isWithinDateRange(LocalDate.of(2024, 12, 1), null));
    }

    @Test
    public void isWithinDateRange_openEndedRange_worksCorrectly() {
        // Given
        LocalDate fecha = LocalDate.of(2024, 12, 15);
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        
        HistorialItem item = new HistorialItem();
        item.setFecha(fecha);
        
        // When & Then
        assertTrue(item.isWithinDateRange(fromDate, null)); // From date only
        assertTrue(item.isWithinDateRange(null, toDate));   // To date only
        assertFalse(item.isWithinDateRange(LocalDate.of(2024, 12, 20), null)); // After from date
        assertFalse(item.isWithinDateRange(null, LocalDate.of(2024, 12, 10))); // Before to date
    }

    @Test
    public void equals_sameId_returnsTrue() {
        // Given
        Long id = 1L;
        HistorialItem item1 = new HistorialItem();
        item1.setId(id);
        item1.setClase("Yoga");
        
        HistorialItem item2 = new HistorialItem();
        item2.setId(id);
        item2.setClase("Pilates"); // Different class, same ID
        
        // When & Then
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    public void equals_differentId_returnsFalse() {
        // Given
        HistorialItem item1 = new HistorialItem();
        item1.setId(1L);
        
        HistorialItem item2 = new HistorialItem();
        item2.setId(2L);
        
        // When & Then
        assertNotEquals(item1, item2);
    }

    @Test
    public void equals_nullIds_returnsTrue() {
        // Given
        HistorialItem item1 = new HistorialItem();
        item1.setId(null);
        
        HistorialItem item2 = new HistorialItem();
        item2.setId(null);
        
        // When & Then
        assertEquals(item1, item2);
    }

    @Test
    public void equals_oneNullId_returnsFalse() {
        // Given
        HistorialItem item1 = new HistorialItem();
        item1.setId(1L);
        
        HistorialItem item2 = new HistorialItem();
        item2.setId(null);
        
        // When & Then
        assertNotEquals(item1, item2);
    }

    @Test
    public void toString_containsAllFields() {
        // Given
        HistorialItem item = new HistorialItem(
            1L, 
            "Yoga", 
            "Sede Centro", 
            LocalDate.of(2024, 12, 15), 
            LocalTime.of(10, 30), 
            60
        );
        
        // When
        String result = item.toString();
        
        // Then
        assertTrue(result.contains("1"));
        assertTrue(result.contains("Yoga"));
        assertTrue(result.contains("Sede Centro"));
        assertTrue(result.contains("2024-12-15"));
        assertTrue(result.contains("10:30"));
        assertTrue(result.contains("60"));
        assertTrue(result.contains("HistorialItem"));
    }

    @Test
    public void settersAndGetters_workCorrectly() {
        // Given
        HistorialItem item = new HistorialItem();
        Long id = 1L;
        String clase = "Pilates";
        String sede = "Sede Norte";
        LocalDate fecha = LocalDate.of(2024, 12, 20);
        LocalTime hora = LocalTime.of(18, 0);
        int duracion = 45;
        
        // When
        item.setId(id);
        item.setClase(clase);
        item.setSede(sede);
        item.setFecha(fecha);
        item.setHora(hora);
        item.setDuracion(duracion);
        
        // Then
        assertEquals(id, item.getId());
        assertEquals(clase, item.getClase());
        assertEquals(sede, item.getSede());
        assertEquals(fecha, item.getFecha());
        assertEquals(hora, item.getHora());
        assertEquals(duracion, item.getDuracion());
    }

    @Test
    public void formattingMethods_handleEdgeCases() {
        // Given
        HistorialItem item = new HistorialItem();
        
        // Test with midnight time
        item.setHora(LocalTime.of(0, 0));
        assertEquals("00:00", item.getFormattedTime());
        
        // Test with end of day time
        item.setHora(LocalTime.of(23, 59));
        assertEquals("23:59", item.getFormattedTime());
        
        // Test with single digit minutes
        item.setHora(LocalTime.of(9, 5));
        assertEquals("09:05", item.getFormattedTime());
        
        // Test with leap year date
        item.setFecha(LocalDate.of(2024, 2, 29));
        assertEquals("2024-02-29", item.getApiFormattedDate());
    }

    @Test
    public void durationFormatting_edgeCases() {
        // Given
        HistorialItem item = new HistorialItem();
        
        // Test 1 minute
        item.setDuracion(1);
        assertEquals("1min", item.getFormattedDuration());
        
        // Test 59 minutes
        item.setDuracion(59);
        assertEquals("59min", item.getFormattedDuration());
        
        // Test exactly 1 hour
        item.setDuracion(60);
        assertEquals("1h", item.getFormattedDuration());
        
        // Test 1 hour 1 minute
        item.setDuracion(61);
        assertEquals("1h 1min", item.getFormattedDuration());
        
        // Test large duration
        item.setDuracion(300); // 5 hours
        assertEquals("5h", item.getFormattedDuration());
        
        // Test large duration with minutes
        item.setDuracion(315); // 5 hours 15 minutes
        assertEquals("5h 15min", item.getFormattedDuration());
    }
}