package com.example.ritmofit.data.api.model;

import java.time.LocalDateTime;

public class CourseResponse {

    private String name;
    private String description;
    private String professor;
    private String branch;
    private String startsAt;
    private String endsAt;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProfessor() {
        return professor;
    }

    public String getBranch() {
        return branch;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public String getEndsAt() {
        return endsAt;
    }
}
