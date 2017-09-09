package com.durgapoojamumbaipune.model;

/**
 * Created by Rini Banerjee on 09-09-2017.
 */

public class Review {

    private String PandalId;
    private String reviewComments;
    private String ratings;
    private String date;

    public Review() {
    }

    public Review(String pandalId, String reviewComments, String ratings, String date) {
        PandalId = pandalId;
        this.reviewComments = reviewComments;
        this.ratings = ratings;
        this.date = date;
    }

    public String getPandalId() {
        return PandalId;
    }

    public void setPandalId(String pandalId) {
        PandalId = pandalId;
    }

    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
