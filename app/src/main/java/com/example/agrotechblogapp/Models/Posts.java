package com.example.agrotechblogapp.Models;

public class Posts {
    private int id;
    private String postDesc;
    private byte[] i_img;

    public Posts(int id, String postDesc, byte[] i_img) {
        this.id = id;
        this.postDesc = postDesc;
        this.i_img = i_img;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public byte[] getI_img() {
        return i_img;
    }

    public void setI_img(byte[] i_img) {
        this.i_img = i_img;
    }
}
