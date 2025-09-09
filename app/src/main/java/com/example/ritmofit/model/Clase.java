package com.example.ritmofit.model;

import android.os.Parcelable;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;
public class Clase implements Parcelable{

    private Long id;

    private String disciplina;

    private String sede;

    private String profesor;

    private int cupo;

    private String fechaHora;

//    private List<Reserva> reservas;

//    private List<Asistencia>  asistencias;

    private Integer duracionMin; // ejemplo: 60

    public Clase(Long id, String disciplina, String sede, String profesor, int cupo, String fechaHora, Integer duracionMin) {
        this.id = id;
        this.disciplina = disciplina;
        this.sede = sede;
        this.profesor = profesor;
        this.cupo = cupo;
        this.fechaHora = fechaHora;
        this.duracionMin = duracionMin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(Integer duracionMin) {
        this.duracionMin = duracionMin;
    }

    protected Clase(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        disciplina = in.readString();
        sede = in.readString();
        profesor = in.readString();
        cupo = in.readInt();
        fechaHora = in.readString();
        if (in.readByte() == 0) {
            duracionMin = null;
        } else {
            duracionMin = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(disciplina);
        dest.writeString(sede);
        dest.writeString(profesor);
        dest.writeInt(cupo);
        dest.writeString(fechaHora);
        if (duracionMin == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(duracionMin);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Clase> CREATOR = new Creator<Clase>() {
        @Override
        public Clase createFromParcel(Parcel in) {
            return new Clase(in);
        }

        @Override
        public Clase[] newArray(int size) {
            return new Clase[size];
        }
    };
}
