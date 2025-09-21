package com.example.ritmofit.data.api.model.historial;

public class AsistenciaResponseDTO {
    private Long id;
    private String nombreClase;
    private String nombreSede;
    private String fecha;
    private int duracionMinutos;
    private String profesor;

    public AsistenciaResponseDTO() {}

    public AsistenciaResponseDTO(Long id, String nombreClase, String nombreSede, String fecha, int duracionMinutos, String profesor) {
        this.id = id;
        this.nombreClase = nombreClase;
        this.nombreSede = nombreSede;
        this.fecha = fecha;
        this.duracionMinutos = duracionMinutos;
        this.profesor = profesor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
}
