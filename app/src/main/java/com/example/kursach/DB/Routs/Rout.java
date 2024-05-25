package com.example.kursach.DB.Routs;

public class Rout {
    private int id;
    private String title;
    private String length;
    private String description;

    public Rout(int id, String title, String length, String description){
        this.id = id;
        this.title = title;
        this.length = length;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getLength() {
        return length;
    }
    public void setLength(String length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
