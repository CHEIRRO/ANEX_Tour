package com.example.kursach.DB.Sights;

public class Sight {
    private int id;
    private String title;
    private String data;
    private String description;

    public Sight(int id, String title, String data, String description) {
        this.id = id;
        this.title = title;
        this.data = data;
        this.description = description;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
