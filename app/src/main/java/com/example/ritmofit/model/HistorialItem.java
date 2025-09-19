package com.example.ritmofit.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Domain model representing a single attendance record in the user's history.
 * This model handles date operations and formatting for Spanish locale.
 */
public class HistorialItem {
    private Long id;
    private String clase;
    private String sede;
    private LocalDate fecha;
    private LocalTime hora;
    private int duracion; // duration in minutes

    // Spanish locale formatter for display
    private static final DateTimeFormatter SPANISH_DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", new Locale("es", "ES"));
    
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm");

    public HistorialItem() {
    }

    public HistorialItem(Long id, String clase, String sede, LocalDate fecha, LocalTime hora, int duracion) {
        this.id = id;
        this.clase = clase;
        this.sede = sede;
        this.fecha = fecha;
        this.hora = hora;
        this.duracion = duracion;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    // Utility methods for date handling and formatting

    /**
     * Returns the date formatted for Spanish locale display
     * Example: "15 de diciembre, 2024"
     */
    public String getFormattedDate() {
        if (fecha == null) {
            return "";
        }
        return fecha.format(SPANISH_DATE_FORMATTER);
    }

    /**
     * Returns the time formatted for display
     * Example: "14:30"
     */
    public String getFormattedTime() {
        if (hora == null) {
            return "";
        }
        return hora.format(TIME_FORMATTER);
    }

    /**
     * Returns the date formatted for API calls (ISO format)
     * Example: "2024-12-15"
     */
    public String getApiFormattedDate() {
        if (fecha == null) {
            return "";
        }
        return fecha.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Returns duration formatted as hours and minutes in Spanish
     * Example: "1h 30min" or "45min"
     */
    public String getFormattedDuration() {
        if (duracion <= 0) {
            return "0min";
        }
        
        int hours = duracion / 60;
        int minutes = duracion % 60;
        
        if (hours > 0 && minutes > 0) {
            return hours + "h " + minutes + "min";
        } else if (hours > 0) {
            return hours + "h";
        } else {
            return minutes + "min";
        }
    }

    /**
     * Checks if this attendance record falls within a specific date range
     */
    public boolean isWithinDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fecha == null) {
            return false;
        }
        
        boolean afterFrom = fromDate == null || !fecha.isBefore(fromDate);
        boolean beforeTo = toDate == null || !fecha.isAfter(toDate);
        
        return afterFrom && beforeTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        HistorialItem that = (HistorialItem) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HistorialItem{" +
                "id=" + id +
                ", clase='" + clase + '\'' +
                ", sede='" + sede + '\'' +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", duracion=" + duracion +
                '}';
    }
}