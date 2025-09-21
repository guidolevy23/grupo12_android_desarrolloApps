package com.example.ritmofit.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Unit tests for DateRange model class.
 * Tests validation, utility methods, and date range operations.
 */
public class DateRangeTest {

    @Test
    public void constructor_noArgs_createsEmptyRange() {
        // When
        DateRange range = new DateRange();
        
        // Then
        assertNull(range.getFromDate());
        assertNull(range.getToDate());
        assertTrue(range.isValid());
        assertFalse(range.hasFilter());
    }

    @Test
    public void constructor_withDates_createsProperlInitializedRange() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        
        // When
        DateRange range = new DateRange(fromDate, toDate);
        
        // Then
        assertEquals(fromDate, range.getFromDate());
        assertEquals(toDate, range.getToDate());
        assertTrue(range.isValid());
        assertTrue(range.hasFilter());
    }

    @Test
    public void isValid_validRange_returnsTrue() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        boolean result = range.isValid();
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isValid_invalidRange_returnsFalse() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 31);
        LocalDate toDate = LocalDate.of(2024, 12, 1);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        boolean result = range.isValid();
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isValid_nullDates_returnsTrue() {
        // Given
        DateRange range = new DateRange(null, null);
        
        // When
        boolean result = range.isValid();
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isValid_singleEndedRange_returnsTrue() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        DateRange range1 = new DateRange(fromDate, null);
        DateRange range2 = new DateRange(null, fromDate);
        
        // When & Then
        assertTrue(range1.isValid());
        assertTrue(range2.isValid());
    }

    @Test
    public void getValidationError_validRange_returnsNull() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        String error = range.getValidationError();
        
        // Then
        assertNull(error);
    }

    @Test
    public void getValidationError_invalidRange_returnsErrorMessage() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 31);
        LocalDate toDate = LocalDate.of(2024, 12, 1);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        String error = range.getValidationError();
        
        // Then
        assertNotNull(error);
        assertTrue(error.contains("fecha 'desde' no puede ser posterior"));
    }

    @Test
    public void hasFilter_withDates_returnsTrue() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        DateRange range1 = new DateRange(fromDate, null);
        DateRange range2 = new DateRange(null, fromDate);
        DateRange range3 = new DateRange(fromDate, fromDate);
        
        // When & Then
        assertTrue(range1.hasFilter());
        assertTrue(range2.hasFilter());
        assertTrue(range3.hasFilter());
    }

    @Test
    public void hasFilter_noDates_returnsFalse() {
        // Given
        DateRange range = new DateRange(null, null);
        
        // When
        boolean result = range.hasFilter();
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isCompleteRange_bothDatesSet_returnsTrue() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        boolean result = range.isCompleteRange();
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isCompleteRange_singleDate_returnsFalse() {
        // Given
        LocalDate date = LocalDate.of(2024, 12, 1);
        DateRange range1 = new DateRange(date, null);
        DateRange range2 = new DateRange(null, date);
        
        // When & Then
        assertFalse(range1.isCompleteRange());
        assertFalse(range2.isCompleteRange());
    }

    @Test
    public void contains_dateInRange_returnsTrue() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        LocalDate testDate = LocalDate.of(2024, 12, 15);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        boolean result = range.contains(testDate);
        
        // Then
        assertTrue(result);
    }

    @Test
    public void contains_dateOutOfRange_returnsFalse() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        LocalDate testDate = LocalDate.of(2025, 1, 1);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        boolean result = range.contains(testDate);
        
        // Then
        assertFalse(result);
    }

    @Test
    public void contains_dateOnBoundary_returnsTrue() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When & Then
        assertTrue(range.contains(fromDate));
        assertTrue(range.contains(toDate));
    }

    @Test
    public void contains_nullDate_returnsFalse() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        boolean result = range.contains(null);
        
        // Then
        assertFalse(result);
    }

    @Test
    public void contains_openEndedRange_worksCorrectly() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate testDate1 = LocalDate.of(2024, 12, 15);
        LocalDate testDate2 = LocalDate.of(2024, 11, 15);
        
        DateRange fromOnlyRange = new DateRange(fromDate, null);
        DateRange toOnlyRange = new DateRange(null, fromDate);
        
        // When & Then
        assertTrue(fromOnlyRange.contains(testDate1));
        assertFalse(fromOnlyRange.contains(testDate2));
        
        assertTrue(toOnlyRange.contains(testDate2));
        assertTrue(toOnlyRange.contains(fromDate));
        assertFalse(toOnlyRange.contains(testDate1));
    }

    @Test
    public void getFromDateForApi_validDate_returnsIsoFormat() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        DateRange range = new DateRange(fromDate, null);
        
        // When
        String result = range.getFromDateForApi();
        
        // Then
        assertEquals("2024-12-01", result);
    }

    @Test
    public void getFromDateForApi_nullDate_returnsNull() {
        // Given
        DateRange range = new DateRange(null, null);
        
        // When
        String result = range.getFromDateForApi();
        
        // Then
        assertNull(result);
    }

    @Test
    public void getToDateForApi_validDate_returnsIsoFormat() {
        // Given
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange range = new DateRange(null, toDate);
        
        // When
        String result = range.getToDateForApi();
        
        // Then
        assertEquals("2024-12-31", result);
    }

    @Test
    public void currentMonth_returnsCurrentMonthRange() {
        // When
        DateRange range = DateRange.currentMonth();
        
        // Then
        assertNotNull(range.getFromDate());
        assertNotNull(range.getToDate());
        
        LocalDate now = LocalDate.now();
        LocalDate expectedFrom = now.withDayOfMonth(1);
        LocalDate expectedTo = now.withDayOfMonth(now.lengthOfMonth());
        
        assertEquals(expectedFrom, range.getFromDate());
        assertEquals(expectedTo, range.getToDate());
        assertTrue(range.isValid());
        assertTrue(range.hasFilter());
        assertTrue(range.isCompleteRange());
    }

    @Test
    public void lastNDays_returnsCorrectRange() {
        // Given
        int days = 7;
        
        // When
        DateRange range = DateRange.lastNDays(days);
        
        // Then
        assertNotNull(range.getFromDate());
        assertNotNull(range.getToDate());
        
        LocalDate today = LocalDate.now();
        LocalDate expectedFrom = today.minusDays(days - 1);
        
        assertEquals(expectedFrom, range.getFromDate());
        assertEquals(today, range.getToDate());
        assertTrue(range.isValid());
    }

    @Test
    public void forMonth_returnsCorrectMonthRange() {
        // Given
        int year = 2024;
        int month = 12;
        
        // When
        DateRange range = DateRange.forMonth(year, month);
        
        // Then
        LocalDate expectedFrom = LocalDate.of(2024, 12, 1);
        LocalDate expectedTo = LocalDate.of(2024, 12, 31);
        
        assertEquals(expectedFrom, range.getFromDate());
        assertEquals(expectedTo, range.getToDate());
        assertTrue(range.isValid());
        assertTrue(range.isCompleteRange());
    }

    @Test
    public void clear_removesAllDates() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        range.clear();
        
        // Then
        assertNull(range.getFromDate());
        assertNull(range.getToDate());
        assertFalse(range.hasFilter());
        assertTrue(range.isValid());
    }

    @Test
    public void copy_createsIdenticalRange() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange original = new DateRange(fromDate, toDate);
        
        // When
        DateRange copy = original.copy();
        
        // Then
        assertEquals(original.getFromDate(), copy.getFromDate());
        assertEquals(original.getToDate(), copy.getToDate());
        assertEquals(original, copy);
        assertNotSame(original, copy); // Different objects
    }

    @Test
    public void equals_sameRanges_returnsTrue() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange range1 = new DateRange(fromDate, toDate);
        DateRange range2 = new DateRange(fromDate, toDate);
        
        // When & Then
        assertEquals(range1, range2);
        assertEquals(range1.hashCode(), range2.hashCode());
    }

    @Test
    public void equals_differentRanges_returnsFalse() {
        // Given
        LocalDate fromDate1 = LocalDate.of(2024, 12, 1);
        LocalDate toDate1 = LocalDate.of(2024, 12, 31);
        LocalDate fromDate2 = LocalDate.of(2024, 11, 1);
        LocalDate toDate2 = LocalDate.of(2024, 11, 30);
        
        DateRange range1 = new DateRange(fromDate1, toDate1);
        DateRange range2 = new DateRange(fromDate2, toDate2);
        
        // When & Then
        assertNotEquals(range1, range2);
    }

    @Test
    public void toString_containsDateInformation() {
        // Given
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        DateRange range = new DateRange(fromDate, toDate);
        
        // When
        String result = range.toString();
        
        // Then
        assertTrue(result.contains("2024-12-01"));
        assertTrue(result.contains("2024-12-31"));
        assertTrue(result.contains("DateRange"));
    }

    @Test
    public void setters_updateDatesCorrectly() {
        // Given
        DateRange range = new DateRange();
        LocalDate fromDate = LocalDate.of(2024, 12, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
        
        // When
        range.setFromDate(fromDate);
        range.setToDate(toDate);
        
        // Then
        assertEquals(fromDate, range.getFromDate());
        assertEquals(toDate, range.getToDate());
        assertTrue(range.isValid());
        assertTrue(range.hasFilter());
    }
}