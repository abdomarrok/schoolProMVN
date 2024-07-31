package com.marrok.schoolmanagermvn.model;

public class Module {

    private Integer id;
    private String name;

    // No-argument constructor
    public Module() {
    }

    // Constructor with parameters
    public Module(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Module(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module module = (Module) o;

        if (!id.equals(module.id)) return false;
        return name.equals(module.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
