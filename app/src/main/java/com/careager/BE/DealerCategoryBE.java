package com.careager.BE;

import java.io.Serializable;

/**
 * Created by appslure on 12-12-2015.
 */
public class DealerCategoryBE implements Serializable {
    private String categoryJSON;
    private String statesJSON;
    private String companyJSON;

    public String getCategoryJSON() {
        return categoryJSON;
    }

    public void setCategoryJSON(String categoryJSON) {
        this.categoryJSON = categoryJSON;
    }

    public String getStatesJSON() {
        return statesJSON;
    }

    public void setStatesJSON(String statesJSON) {
        this.statesJSON = statesJSON;
    }

    public String getCompanyJSON() {
        return companyJSON;
    }

    public void setCompanyJSON(String companyJSON) {
        this.companyJSON = companyJSON;
    }
}
