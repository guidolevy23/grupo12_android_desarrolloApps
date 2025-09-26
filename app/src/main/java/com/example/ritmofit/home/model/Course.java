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

    // --- Constructor for Parcel ---
    protected Course(Parcel in) {
        name = in.readString();
        description = in.readString();
        professor = in.readString();
        branch = in.readString();
        startsAt = LocalDateTime.parse(in.readString()); // lo leemos como String y parseamos
        endsAt = LocalDateTime.parse(in.readString());
    }

    // --- Parcelable required methods ---
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(professor);
        dest.writeString(branch);
        dest.writeString(startsAt.toString()); // guardamos como String ISO
        dest.writeString(endsAt.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }
    // metodo para construir el schedule

    /*public String getSchedule() {
        if (startsAt == null || endsAt == null) {
            return "Horario no disponible";
        }

        try {

            // Determinar el formato basado en el contenido del string
            SimpleDateFormat inputFormat;
            if (startsAt.contains("T")) {
                // Formato ISO: "2025-09-10T08:00:00"
                inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            } else {
                // Formato SQL: "2025-09-10 08:00:00"
                inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date startDate = inputFormat.parse(startsAt);
            Date endDate = inputFormat.parse(endsAt);

            String startTime = timeFormat.format(startDate);
            String endTime = timeFormat.format(endDate);
            String date = dateFormat.format(startDate);

            String schedule = String.format("%s - %s (%s)", startTime, endTime, date);

            return schedule;

        } catch (ParseException e) {
            e.printStackTrace();

            // Si falla el parsing, mostrar el formato raw
            return String.format("Horario: %s a %s", startsAt, endsAt);
        }
    }*/

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

    // --- Setters ---
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setProfessor(String professor) { this.professor = professor; }
    public void setBranch(String branch) { this.branch = branch; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }
    public void setEndsAt(LocalDateTime endsAt) { this.endsAt = endsAt; }
}
