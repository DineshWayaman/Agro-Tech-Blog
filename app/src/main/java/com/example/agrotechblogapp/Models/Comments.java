package com.example.agrotechblogapp.Models;

public class Comments {
    private int id;
    private int pID;
    private String com;

    public Comments(int id, int pID, String com) {
        this.id = id;
        this.pID = pID;
        this.com = com;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }
}
