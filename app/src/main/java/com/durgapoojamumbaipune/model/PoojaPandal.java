package com.durgapoojamumbaipune.model;

/**
 * Created by Rini Banerjee on 06-09-2017.
 */

public class PoojaPandal {

    private String Name;
    private String MapLink;
    private String Description;
    private String CityId;
    private String PandalId;
    private String ImagePath;

    public PoojaPandal() {
    }

    public PoojaPandal(String name, String mapLink, String description, String cityId, String pandalId, String imagePath) {
        Name = name;
        MapLink = mapLink;
        Description = description;
        CityId = cityId;
        PandalId = pandalId;
        ImagePath = imagePath;
    }

    public String getPandalId() {
        return PandalId;
    }

    public void setPandalId(String pandalId) {
        PandalId = pandalId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMapLink() {
        return MapLink;
    }

    public void setMapLink(String mapLink) {
        MapLink = mapLink;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }
}
