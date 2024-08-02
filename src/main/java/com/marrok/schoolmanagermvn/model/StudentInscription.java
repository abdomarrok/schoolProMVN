package com.marrok.schoolmanagermvn.model;

import com.marrok.schoolmanagermvn.util.DatabaseHelper;

import java.sql.SQLException;
import java.time.LocalDate;

public class StudentInscription {

    private Integer id;
    private Integer studentId;
    private Integer sessionId;
    private LocalDate registrationDate;
    private String price;
    private String studentFullName; // New field
    private String sessionDetails;  // New field

    public StudentInscription(Integer id, Integer studentId, Integer sessionId, LocalDate registrationDate, String price) {
        this.id = id;
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.registrationDate = registrationDate;
        this.price = price;
    }

    public StudentInscription(Integer id, Integer studentId, Integer sessionId, LocalDate registrationDate, String price, String studentFullName, String sessionDetails) {
        this.id = id;
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.registrationDate = registrationDate;
        this.price = price;
        this.studentFullName = studentFullName;
        this.sessionDetails = sessionDetails;
    }

    public StudentInscription(int inscriptionId, String fullName, String sessionInfo, LocalDate registrationDate, String price) {
        this.id = inscriptionId;
        this.studentFullName=fullName;
        this.sessionDetails=sessionInfo;
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

    public String getStudentFullName() {
        return studentFullName;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }

    public String getSessionDetails() {
        return sessionDetails;
    }

    public void setSessionDetails(String sessionDetails) {
        this.sessionDetails = sessionDetails;
    }
}
