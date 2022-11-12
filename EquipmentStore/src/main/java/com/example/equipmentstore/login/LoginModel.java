package com.example.equipmentstore.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginModel implements Serializable {
    @SerializedName("USERNAME")
    @Expose
    private String USERNAME;

    @SerializedName("PASSWORD")
    @Expose
    private String PASSWORD;

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

 /*   @SerializedName("SUBDIVCODE")
    @Expose
    private String SUBDIVCODE;

    @SerializedName("GROUPS")
    @Expose
    private String GROUPS;*/

/*    public String getSUBDIVCODE() {
        return SUBDIVCODE;
    }

    public void setSUBDIVCODE(String SUBDIVCODE) {
        this.SUBDIVCODE = SUBDIVCODE;
    }

    public String getGROUPS() {
        return GROUPS;
    }

    public void setGROUPS(String GROUPS) {
        this.GROUPS = GROUPS;
    }*/
}
