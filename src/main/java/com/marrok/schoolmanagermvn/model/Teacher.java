package com.marrok.schoolmanagermvn.model;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Teacher {

    private Integer id;


    private String fname;


    private String lname;


    private Integer phone;


    private String address;


    private Boolean gender = false;

    public Teacher( String fname, String lname, Integer phone, String address, Boolean gender) {

        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
    }

    public Teacher(Integer id, String fname, String lname, Integer phone, String address, Boolean gender) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
    }

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

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacher_id=" + id +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone=" + phone +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                '}';
    }
}