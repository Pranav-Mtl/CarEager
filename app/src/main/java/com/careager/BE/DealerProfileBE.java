package com.careager.BE;

import java.io.Serializable;

/**
 * Created by appslure on 24-11-2015.
 */
public class DealerProfileBE implements Serializable {

    private String profileBaseURL;
    private String saleBaseURL;

    private String name;
    private String image;
    private String overview;
    private String location;
    private String email;
    private String phone;
    private String careagerRating;
    private String totalRating;
    private String rating;
    private String cover;
    private String approved;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getProfileBaseURL() {
        return profileBaseURL;
    }

    public void setProfileBaseURL(String profileBaseURL) {
        this.profileBaseURL = profileBaseURL;
    }

    public String getSaleBaseURL() {
        return saleBaseURL;
    }

    public void setSaleBaseURL(String saleBaseURL) {
        this.saleBaseURL = saleBaseURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCareagerRating() {
        return careagerRating;
    }

    public void setCareagerRating(String careagerRating) {
        this.careagerRating = careagerRating;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
