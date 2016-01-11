package com.careager.BE;

import java.io.Serializable;

/**
 * Created by Pranav Mittal on 11/5/2015.
 */
public class DealerSignUpBE implements Serializable {

    private String name;
    private String email;
    private String contact;
    private String password;
    private String location;
    private String state;
    private String dealerCategory;
    private String dealerCompany;
    private String gcmId;
    private String deviceId;

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDealerCompany() {
        return dealerCompany;
    }

    public void setDealerCompany(String dealerCompany) {
        this.dealerCompany = dealerCompany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getDealerCategory() {
        return dealerCategory;
    }

    public void setDealerCategory(String dealerCategory) {
        this.dealerCategory = dealerCategory;
    }
}
