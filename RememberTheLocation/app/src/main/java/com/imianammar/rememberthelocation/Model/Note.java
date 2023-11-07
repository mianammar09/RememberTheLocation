package com.imianammar.rememberthelocation.Model;

public class Note {
    private String locationName;
    private String description;
    private String imagePath;
    private int id;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}