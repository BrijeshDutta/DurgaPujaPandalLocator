package com.durgapoojamumbaipune.model;

/**
 * Created by Rini Banerjee on 08-09-2017.
 */

public class Request {
    private String PandalName;
    private String PandalDesciption;
    private String PandalAddress;
    private String PandalContactNumber;

    public Request() {
    }

    public Request(String pandalName, String pandalDesciption, String pandalAddress, String pandalContactNumber) {
        PandalName = pandalName;
        PandalDesciption = pandalDesciption;
        PandalAddress = pandalAddress;
        PandalContactNumber = pandalContactNumber;
    }

    public String getPandalName() {
        return PandalName;
    }

    public void setPandalName(String pandalName) {
        PandalName = pandalName;
    }

    public String getPandalDesciption() {
        return PandalDesciption;
    }

    public void setPandalDesciption(String pandalDesciption) {
        PandalDesciption = pandalDesciption;
    }

    public String getPandalAddress() {
        return PandalAddress;
    }

    public void setPandalAddress(String pandalAddress) {
        PandalAddress = pandalAddress;
    }

    public String getPandalContactNumber() {
        return PandalContactNumber;
    }

    public void setPandalContactNumber(String pandalContactNumber) {
        PandalContactNumber = pandalContactNumber;
    }
}
