package com.marrok.schoolmanagermvn.model;

public class Student {

    private Integer id;
    private String fname;
    private String lname;
    private String birthDate;
    private String level; // Added the 'level' field
    private String contact;
    private Boolean gender; // Keeping as Boolean, since tinyint(1) often represents a boolean value

    // Constructor
    public Student(Integer id, String fname, String lname, String birthDate, String level, String contact, Boolean gender) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.birthDate = birthDate;
        this.level = level;
        this.contact = contact;
        this.gender = gender;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }
}
