package com.marrok.schoolmanagermvn.model;


import java.time.LocalDate;

public class StudentInscription {

    private Integer id;


    private Integer studentId;


    private Integer sessionId;


    private LocalDate registrationDate;

    private String price;

    public StudentInscription(Integer id, Integer studentId, Integer sessionId, LocalDate registrationDate, String price) {
        this.id = id;
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.registrationDate = registrationDate;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}