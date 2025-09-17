package com.example.ritmofit.data.api.model.reserva;

public class ReservaResponseDTO {
    private Long id;
    private String estado;
    private String clase;
    private String sede;
    private String horario;

    public ReservaResponseDTO() {}

    public ReservaResponseDTO(Long id, String estado, String clase, String sede, String horario) {
        this.id = id;
        this.estado = estado;
        this.clase = clase;
        this.sede = sede;
        this.horario = horario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
