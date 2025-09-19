package com.example.ritmofit.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Model representing a date range for filtering attendance history.
 * Provides validation methods and API formatting utilities.
 */
public class DateRange {
    private LocalDate fromDate;
    private LocalDate toDate;

    public DateRange() {
    }

    public DateRange(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    // Getters and setters
    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    // Validation methods

    /**
     * Validates that the date range is logically correct
     * @return true if the range is valid, false otherwise
     */
    public boolean isValid() {
        if (fromDate == null && toDate == null) {
            return true; // No filter applied
        }
        
        if (fromDate == null || toDate == null) {
            return true; // Single-ended range is valid
        }
        
        return !fromDate.isAfter(toDate); // fromDate should not be after toDate
    }

    /**
     * Returns validation error message in Spanish if range is invalid
     * @return error message or null if valid
     */
    public String getValidationError() {
        if (!isValid()) {
            return "La fecha 'desde' no puede ser posterior a la fecha 'hasta'";
        }
        return null;
    }

    /**
     * Checks if the range has any filter applied
     * @return true if at least one date is set
     */
    public boolean hasFilter() {
        return fromDate != null || toDate != null;
    }

    /**
     * Checks if both dates are set (complete range)
     * @return true if both fromDate and toDate are set
     */
    public boolean isCompleteRange() {
        return fromDate != null && toDate != null;
    }

    /**
     * Checks if a given date falls within this range
     * @param date the date to check
     * @return true if the date is within the range
     */
    public boolean contains(LocalDate date) {
        if (date == null) {
            return false;
        }
        
        boolean afterFrom = fromDate == null || !date.isBefore(fromDate);
        boolean beforeTo = toDate == null || !date.isAfter(toDate);
        
        return afterFrom && beforeTo;
    }

    // API formatting methods

    /**
     * Returns the fromDate formatted for API calls (ISO format)
     * @return formatted date string or null if fromDate is null
     */
    public String getFromDateForApi() {
        return fromDate != null ? fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }

    /**
     * Returns the toDate formatted for API calls (ISO format)
     * @return formatted date string or null if toDate is null
     */
    public String getToDateForApi() {
        return toDate != null ? toDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }

    // Utility methods for common date ranges

    /**
     * Creates a DateRange for the current month
     * @return DateRange representing the current month
     */
    public static DateRange currentMonth() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);
        LocalDate lastDayOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        return new DateRange(firstDayOfMonth, lastDayOfMonth);
    }

    /**
     * Creates a DateRange for the last N days
     * @param days number of days to go back
     * @return DateRange representing the last N days
     */
    public static DateRange lastNDays(int days) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days - 1);
        return new DateRange(startDate, today);
    }

    /**
     * Creates a DateRange for a specific month and year
     * @param year the year
     * @param month the month (1-12)
     * @return DateRange representing the specified month
     */
    public static DateRange forMonth(int year, int month) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        return new DateRange(firstDay, lastDay);
    }

    /**
     * Clears the date range (removes all filters)
     */
    public void clear() {
        this.fromDate = null;
        this.toDate = null;
    }

    /**
     * Creates a copy of this DateRange
     * @return a new DateRange with the same dates
     */
    public DateRange copy() {
        return new DateRange(this.fromDate, this.toDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        DateRange dateRange = (DateRange) o;
        
        if (fromDate != null ? !fromDate.equals(dateRange.fromDate) : dateRange.fromDate != null)
            return false;
        return toDate != null ? toDate.equals(dateRange.toDate) : dateRange.toDate == null;
    }

    @Override
    public int hashCode() {
        int result = fromDate != null ? fromDate.hashCode() : 0;
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }
}