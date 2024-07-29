package com.marrok.schoolmanagermvn.model;

public class Student {
    public Student(Integer id, String fname, String lname, Integer year, Integer contact, Boolean gender, Integer classRooms) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.year = year;
        this.contact = contact;
        this.gender = gender;
        this.classRooms = classRooms;
    }

    private Integer id;


    private String fname;


    private String lname;


    private Integer year;


    private Integer contact;


    private Boolean gender = false;


    private Integer classRooms;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Integer getClassRooms() {
        return classRooms;
    }

    public void setClassRooms(Integer classRooms) {
        this.classRooms = classRooms;
    }

}