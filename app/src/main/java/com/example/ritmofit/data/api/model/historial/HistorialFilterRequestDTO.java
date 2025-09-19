package com.example.ritmofit.data.api.model.historial;

public class HistorialFilterRequestDTO {
    private Long usuarioId;
    private String fechaInicio;
    private String fechaFin;

    public HistorialFilterRequestDTO() {}

    public HistorialFilterRequestDTO(Long usuarioId, String fechaInicio, String fechaFin) {
        this.usuarioId = usuarioId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
