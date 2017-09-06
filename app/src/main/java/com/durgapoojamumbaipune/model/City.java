package com.durgapoojamumbaipune.model;

/**
 * Created by Rini Banerjee on 06-09-2017.
 */

public class City {

    private String Name;
    private String Image;

    public City() {
    }

    public City(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
