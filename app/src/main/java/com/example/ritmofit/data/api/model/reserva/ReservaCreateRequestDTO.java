package com.example.ritmofit.data.api.model.reserva;

public class ReservaCreateRequestDTO {
    private Long usuarioId;
    private Long claseId;

    public ReservaCreateRequestDTO() {}

    public ReservaCreateRequestDTO(Long usuarioId, Long claseId) {
        this.usuarioId = usuarioId;
        this.claseId = claseId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getClaseId() {
        return claseId;
    }

    public void setClaseId(Long claseId) {
        this.claseId = claseId;
    }
}
