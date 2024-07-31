package com.marrok.schoolmanagermvn.model;

public class Session_model {
    private int id;
    private int module_ID;
    private int teacher_ID;

    public Session_model(int id, int module_ID, int teacher_ID) {
        this.id = id;
        this.module_ID = module_ID;
        this.teacher_ID = teacher_ID;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModule_ID() {
        return module_ID;
    }

    public void setModule_ID(int module_ID) {
        this.module_ID = module_ID;
    }

    public int getTeacher_ID() {
        return teacher_ID;
    }

    public void setTeacher_ID(int teacher_ID) {
        this.teacher_ID = teacher_ID;
    }

    // ToString method for easy representation
    @Override
    public String toString() {
        return "Session_model{" +
                "id=" + id +
                ", module_ID=" + module_ID +
                ", teacher_ID=" + teacher_ID +
                '}';
    }
}
