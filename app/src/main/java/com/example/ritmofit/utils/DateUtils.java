package com.example.ritmofit.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Utility class for date and time operations with Spanish locale formatting.
 * Provides parsing, formatting, and validation methods for the Historial feature.
 */
public class DateUtils {

    // Spanish locale for formatting
    private static final Locale SPANISH_LOCALE = new Locale("es", "ES");




    // Common date formatters
    public static final DateTimeFormatter API_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final DateTimeFormatter API_DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final DateTimeFormatter DISPLAY_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", SPANISH_LOCALE);
    public static final DateTimeFormatter SHORT_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy", SPANISH_LOCALE);
    public static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter MONTH_YEAR_FORMATTER = 
        DateTimeFormatter.ofPattern("MMMM yyyy", SPANISH_LOCALE);


    // Para parsear LocalDateTime que viene del backend (ej: "2025-09-25T08:00:00")
    public static final DateTimeFormatter BACKEND_DATETIME_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Para mostrar fecha + hora completas al usuario
    public static final DateTimeFormatter DISPLAY_FULL_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM yyyy, HH:mm", SPANISH_LOCALE);

    // Para mostrar solo la fecha pero en formato largo
    public static final DateTimeFormatter DISPLAY_LONG_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", SPANISH_LOCALE);

    // Private constructor to prevent instantiation
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Parsing methods

    /**
     * Parses a date string in ISO format (yyyy-MM-dd) to LocalDate
     * @param dateString the date string to parse
     * @return LocalDate or null if parsing fails
     */
    public static LocalDate parseApiDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        try {
            return LocalDate.parse(dateString, API_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Parses a datetime string in ISO format to LocalDateTime
     * @param dateTimeString the datetime string to parse
     * @return LocalDateTime or null if parsing fails
     */
    public static LocalDateTime parseApiDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        
        try {
            return LocalDateTime.parse(dateTimeString, API_DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Parses a time string in HH:mm format to LocalTime
     * @param timeString the time string to parse
     * @return LocalTime or null if parsing fails
     */
    public static LocalTime parseTime(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) {
            return null;
        }
        
        try {
            return LocalTime.parse(timeString, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // Formatting methods

    /**
     * Formats a LocalDate for API calls (ISO format)
     * @param date the date to format
     * @return formatted date string or null if date is null
     */
    public static String formatForApi(LocalDate date) {
        return date != null ? date.format(API_DATE_FORMATTER) : null;
    }

    /**
     * Formats a LocalDateTime for API calls (ISO format)
     * @param dateTime the datetime to format
     * @return formatted datetime string or null if dateTime is null
     */
    public static String formatForApi(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(API_DATETIME_FORMATTER) : null;
    }

    /**
     * Formats a LocalDate for display in Spanish locale
     * Example: "15 de diciembre, 2024"
     * @param date the date to format
     * @return formatted date string or empty string if date is null
     */
    public static String formatForDisplay(LocalDate date) {
        return date != null ? date.format(DISPLAY_DATE_FORMATTER) : "";
    }

    /**
     * Formats a LocalDate in short format for display
     * Example: "15/12/2024"
     * @param date the date to format
     * @return formatted date string or empty string if date is null
     */
    public static String formatShort(LocalDate date) {
        return date != null ? date.format(SHORT_DATE_FORMATTER) : "";
    }

    /**
     * Formats a LocalTime for display
     * Example: "14:30"
     * @param time the time to format
     * @return formatted time string or empty string if time is null
     */
    public static String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : "";
    }

    /**
     * Formats a LocalDate to show month and year in Spanish
     * Example: "diciembre 2024"
     * @param date the date to format
     * @return formatted month-year string or empty string if date is null
     */
    public static String formatMonthYear(LocalDate date) {
        return date != null ? date.format(MONTH_YEAR_FORMATTER) : "";
    }

    // Validation methods

    /**
     * Validates that a date range is logically correct
     * @param fromDate the start date (can be null)
     * @param toDate the end date (can be null)
     * @return true if the range is valid
     */
    public static boolean isValidDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            return true; // Null dates are considered valid (no filter)
        }
        return !fromDate.isAfter(toDate);
    }

    /**
     * Checks if a date is within a specified range
     * @param date the date to check
     * @param fromDate the start of the range (inclusive, can be null)
     * @param toDate the end of the range (inclusive, can be null)
     * @return true if the date is within the range
     */
    public static boolean isDateInRange(LocalDate date, LocalDate fromDate, LocalDate toDate) {
        if (date == null) {
            return false;
        }
        
        boolean afterFrom = fromDate == null || !date.isBefore(fromDate);
        boolean beforeTo = toDate == null || !date.isAfter(toDate);
        
        return afterFrom && beforeTo;
    }

    // Utility methods for common date operations

    /**
     * Gets the first day of the current month
     * @return LocalDate representing the first day of the current month
     */
    public static LocalDate getFirstDayOfCurrentMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }

    /**
     * Gets the last day of the current month
     * @return LocalDate representing the last day of the current month
     */
    public static LocalDate getLastDayOfCurrentMonth() {
        LocalDate now = LocalDate.now();
        return now.withDayOfMonth(now.lengthOfMonth());
    }

    /**
     * Gets the first day of a specific month and year
     * @param year the year
     * @param month the month (1-12)
     * @return LocalDate representing the first day of the specified month
     */
    public static LocalDate getFirstDayOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    /**
     * Gets the last day of a specific month and year
     * @param year the year
     * @param month the month (1-12)
     * @return LocalDate representing the last day of the specified month
     */
    public static LocalDate getLastDayOfMonth(int year, int month) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        return firstDay.withDayOfMonth(firstDay.lengthOfMonth());
    }

    /**
     * Calculates the number of days between two dates
     * @param startDate the start date
     * @param endDate the end date
     * @return number of days between the dates, or 0 if either date is null
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return Math.abs(startDate.until(endDate).getDays());
    }

    /**
     * Checks if a date is today
     * @param date the date to check
     * @return true if the date is today
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }

    /**
     * Checks if a date is in the past
     * @param date the date to check
     * @return true if the date is before today
     */
    public static boolean isPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * Checks if a date is in the future
     * @param date the date to check
     * @return true if the date is after today
     */
    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    // Spanish locale specific methods

    /**
     * Gets the Spanish name for a day of the week
     * @param date the date to get the day name for
     * @return Spanish day name or empty string if date is null
     */
    public static String getSpanishDayName(LocalDate date) {
        if (date == null) {
            return "";
        }
        
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE", SPANISH_LOCALE);
        return date.format(dayFormatter);
    }

    /**
     * Gets the Spanish name for a month
     * @param date the date to get the month name for
     * @return Spanish month name or empty string if date is null
     */
    public static String getSpanishMonthName(LocalDate date) {
        if (date == null) {
            return "";
        }
        
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", SPANISH_LOCALE);
        return date.format(monthFormatter);
    }
}