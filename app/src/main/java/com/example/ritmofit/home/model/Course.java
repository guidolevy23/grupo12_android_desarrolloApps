package com.example.ritmofit.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class Course implements Parcelable {

    private String name;
    private String description;
    private String professor;
    private String branch;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;

    // 🔹 NUEVOS ATRIBUTOS PARA CUPO (valores por defecto temporales)
    private int totalCapacity = 20;      // Cupo total
    private int availableSpots = 15;     // Cupos disponibles

    // --- Constructor completo ---
    public Course(String name, String description, String professor, String branch,
                  LocalDateTime startsAt, LocalDateTime endsAt) {
        this.name = name;
        this.description = description;
        this.professor = professor;
        this.branch = branch;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    // 🔹 NUEVO CONSTRUCTOR CON CUPO
    public Course(String name, String description, String professor, String branch,
                  LocalDateTime startsAt, LocalDateTime endsAt, int totalCapacity, int availableSpots) {
        this.name = name;
        this.description = description;
        this.professor = professor;
        this.branch = branch;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.totalCapacity = totalCapacity;
        this.availableSpots = availableSpots;
    }

    // --- Constructor for Parcel ---
    protected Course(Parcel in) {
        name = in.readString();
        description = in.readString();
        professor = in.readString();
        branch = in.readString();
        startsAt = LocalDateTime.parse(in.readString());
        endsAt = LocalDateTime.parse(in.readString());
        totalCapacity = in.readInt();        // 🔹 NUEVO
        availableSpots = in.readInt();       // 🔹 NUEVO
    }

    // --- Parcelable required methods ---
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(professor);
        dest.writeString(branch);
        dest.writeString(startsAt.toString());
        dest.writeString(endsAt.toString());
        dest.writeInt(totalCapacity);        // 🔹 NUEVO
        dest.writeInt(availableSpots);       // 🔹 NUEVO
    }

    // 🔹 NUEVO metodo: Obtener información del cupo
    public String getCapacityInfo() {
        return String.format("%d/%d cupos disponibles", availableSpots, totalCapacity);
    }

    // 🔹 NUEVO metodo: Verificar si hay cupos disponibles
    public boolean hasAvailableSpots() {
        return availableSpots > 0;
    }

    // 🔹 NUEVO metodo: Porcentaje de disponibilidad (para progreso visual)
    public int getAvailabilityPercentage() {
        if (totalCapacity == 0) return 0;
        return (availableSpots * 100) / totalCapacity;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    // metodo para construir el schedule


    public String getSchedule() {
        if (startsAt == null || endsAt == null) {
            return "Horario no disponible";
        }

        try {
            // Formatear la hora
            java.time.format.DateTimeFormatter timeFormatter =
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm");

            // Formatear la fecha
            java.time.format.DateTimeFormatter dateFormatter =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String startTime = startsAt.format(timeFormatter);
            String endTime = endsAt.format(timeFormatter);
            String date = startsAt.format(dateFormatter);

            return String.format("%s - %s (%s)", startTime, endTime, date);

        } catch (Exception e) {
            e.printStackTrace();
            // Si falla el formato, mostrar el formato raw
            return String.format("Horario: %s a %s",
                    startsAt.toString(), endsAt.toString());
        }
    }

    // dificultad y categoria, faltaria agregar en el back
    public String getDifficulty() {
        if (name != null) {
            String lowerName = name.toLowerCase();
            if (lowerName.contains("principiantes") || lowerName.contains("básico")) {
                return "Principiante";
            } else if (lowerName.contains("avanzado") || lowerName.contains("experto")) {
                return "Avanzado";
            }
        }
        return "Intermedio";
    }

    public String getCategory() {
        if (name != null) {
            String lowerName = name.toLowerCase();
            if (lowerName.contains("yoga")) {
                return "Yoga";
            } else if (lowerName.contains("crossfit")) {
                return "CrossFit";
            } else if (lowerName.contains("pilates")) {
                return "Pilates";
            } else if (lowerName.contains("hiit")) {
                return "HIIT";
            }
        }
        return "Fitness";
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    // --- Getters ---
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getProfessor() { return professor; }
    public String getBranch() { return branch; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public LocalDateTime getEndsAt() { return endsAt; }
    public int getTotalCapacity() { return totalCapacity; }          // 🔹 NUEVO
    public int getAvailableSpots() { return availableSpots; }        // 🔹 NUEVO


    // --- Setters ---
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setProfessor(String professor) { this.professor = professor; }
    public void setBranch(String branch) { this.branch = branch; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }
    public void setEndsAt(LocalDateTime endsAt) { this.endsAt = endsAt; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }        // 🔹 NUEVO
    public void setAvailableSpots(int availableSpots) { this.availableSpots = availableSpots; }    // 🔹 NUEVO
}
