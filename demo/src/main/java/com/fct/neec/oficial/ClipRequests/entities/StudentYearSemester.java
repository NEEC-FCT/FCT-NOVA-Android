package com.fct.neec.oficial.ClipRequests.entities;

import java.io.Serializable;

public class StudentYearSemester extends Entity implements Serializable {
    private String year, semester;

    public StudentYearSemester() {}

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
