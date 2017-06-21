package me.warm.rxjava_rxandroid_example.entity;

/**
 * Created by warm on 17/6/21.
 */

public class Parent {

    private int id;
    private String fatherName;
    private String mumName;

    public Parent(int id, String fatherName, String mumName) {
        this.id = id;
        this.fatherName = fatherName;
        this.mumName = mumName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMumName() {
        return mumName;
    }

    public void setMumName(String mumName) {
        this.mumName = mumName;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", fatherName='" + fatherName + '\'' +
                ", mumName='" + mumName + '\'' +
                '}';
    }
}
