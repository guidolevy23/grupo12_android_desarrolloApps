package com.example.ritmofit.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Unit tests for DateUtils class.
 * Tests date parsing, formatting, validation, and utility methods.
 */
@RunWith(RobolectricTestRunner.class)
public class DateUtilsTest {

    // Test parsing methods

    @Test
    public void parseApiDate_validDate_returnsLocalDate() {
        // Given
        String dateString = "2024-12-15";
        
        // When
        LocalDate result = DateUtils.parseApiDate(dateString);
        
        // Then
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void parseApiDate_nullInput_returnsNull() {
        // When
        LocalDate result = DateUtils.parseApiDate(null);
        
        // Then
        assertNull(result);
    }

    @Test
    public void parseApiDate_emptyString_returnsNull() {
        // When
        LocalDate result = DateUtils.parseApiDate("");
        
        // Then
        assertNull(result);
    }

    @Test
    public void parseApiDate_invalidFormat_returnsNull() {
        // When
        LocalDate result = DateUtils.parseApiDate("15/12/2024");
        
        // Then
        assertNull(result);
    }

    @Test
    public void parseApiDateTime_validDateTime_returnsLocalDateTime() {
        // Given
        String dateTimeString = "2024-12-15T14:30:00";
        
        // When
        LocalDateTime result = DateUtils.parseApiDateTime(dateTimeString);
        
        // Then
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseTime_validTime_returnsLocalTime() {
        // Given
        String timeString = "14:30";
        
        // When
        LocalTime result = DateUtils.parseTime(timeString);
        
        // Then
        assertNotNull(result);
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseTime_invalidFormat_returnsNull() {
        // When
        LocalTime result = DateUtils.parseTime("2:30 PM");
        
        // Then
        assertNull(result);
    }

    // Test formatting methods

    @Test
    public void formatForApi_validDate_returnsIsoFormat() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 15);
        
        // When
        String result = DateUtils.formatForApi(date);
        
        // Then
        assertEquals("2024-12-15", result);
    }

    @Test
    public void formatForApi_nullDate_returnsNull() {
        // When
        String result = DateUtils.formatForApi((LocalDate) null);
        
        // Then
        assertNull(result);
    }

    @Test
    public void formatForDisplay_validDate_returnsSpanishFormat() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 15);
        
        // When
        String result = DateUtils.formatForDisplay(date);
        
        // Then
        assertTrue(result.contains("diciembre"));
        assertTrue(result.contains("2024"));
        assertTrue(result.contains("15"));
    }

    @Test
    public void formatShort_validDate_returnsShortFormat() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 15);
        
        // When
        String result = DateUtils.formatShort(date);
        
        // Then
        assertEquals("15/12/2024", result);
    }

    @Test
    public void formatTime_validTime_returnsFormattedTime() {
        // Given
        LocalTime time = LocalTime.of(14, 30);
        
        // When
        String result = DateUtils.formatTime(time);
        
        // Then
        assertEquals("14:30", result);
    }

    // Test validation methods

    @Test
    public void isValidDateRange_validRange_returnsTrue() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 15);
        
        // When
        boolean result = DateUtils.isValidDateRange(fromDate, toDate);
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isValidDateRange_invalidRange_returnsFalse() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 15);
        LocalDate toDate = LocalDate.of(2024, 12, 1);
        
        // When
        boolean result = DateUtils.isValidDateRange(fromDate, toDate);
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isValidDateRange_nullDates_returnsTrue() {
        // When
        boolean result = DateUtils.isValidDateRange(null, null);
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isDateInRange_dateInRange_returnsTrue() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 10);
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 15);
        
        // When
        boolean result = DateUtils.isDateInRange(date, fromDate, toDate);
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isDateInRange_dateOutOfRange_returnsFalse() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 20);
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 15);
        
        // When
        boolean result = DateUtils.isDateInRange(date, fromDate, toDate);
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isDateInRange_nullDate_returnsFalse() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 15);
        
        // When
        boolean result = DateUtils.isDateInRange(null, fromDate, toDate);
        
        // Then
        assertFalse(result);
    }

    // Test utility methods

    @Test
    public void getFirstDayOfCurrentMonth_returnsCorrectDate() {
        // When
        LocalDate result = DateUtils.getFirstDayOfCurrentMonth();
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getDayOfMonth());
    }

    @Test
    public void getLastDayOfCurrentMonth_returnsCorrectDate() {
        // When
        LocalDate result = DateUtils.getLastDayOfCurrentMonth();
        
        // Then
        assertNotNull(result);
        LocalDate now = LocalDate.now();
        assertEquals(now.lengthOfMonth(), result.getDayOfMonth());
    }

    @Test
    public void getFirstDayOfMonth_returnsCorrectDate() {
        // When
        LocalDate result = DateUtils.getFirstDayOfMonth(2024, 12);
        
        // Then
        assertEquals(LocalDate.of(2024, 12, 1), result);
    }

    @Test
    public void getLastDayOfMonth_returnsCorrectDate() {
        // When
        LocalDate result = DateUtils.getLastDayOfMonth(2024, 12);
        
        // Then
        assertEquals(LocalDate.of(2024, 12, 31), result);
    }

    @Test
    public void daysBetween_validDates_returnsCorrectDays() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 15);
        
        // When
        long result = DateUtils.daysBetween(startDate, endDate);
        
        // Then
        assertEquals(14, result);
    }

    @Test
    public void daysBetween_nullDates_returnsZero() {
        // When
        long result = DateUtils.daysBetween(null, null);
        
        // Then
        assertEquals(0, result);
    }

    @Test
    public void isToday_todayDate_returnsTrue() {
        // Given
        LocalDate today = LocalDate.now();
        
        // When
        boolean result = DateUtils.isToday(today);
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isToday_pastDate_returnsFalse() {
        // Given
        LocalDate pastDate = LocalDate.now().minusDays(1);
        
        // When
        boolean result = DateUtils.isToday(pastDate);
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isPast_pastDate_returnsTrue() {
        // Given
        LocalDate pastDate = LocalDate.now().minusDays(1);
        
        // When
        boolean result = DateUtils.isPast(pastDate);
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isFuture_futureDate_returnsTrue() {
        // Given
        LocalDate futureDate = LocalDate.now().plusDays(1);
        
        // When
        boolean result = DateUtils.isFuture(futureDate);
        
        // Then
        assertTrue(result);
    }

    @Test
    public void getSpanishDayName_validDate_returnsSpanishName() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 15); // Sunday
        
        // When
        String result = DateUtils.getSpanishDayName(date);
        
        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getSpanishMonthName_validDate_returnsSpanishName() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 15);
        
        // When
        String result = DateUtils.getSpanishMonthName(date);
        
        // Then
        assertEquals("diciembre", result);
    }
}