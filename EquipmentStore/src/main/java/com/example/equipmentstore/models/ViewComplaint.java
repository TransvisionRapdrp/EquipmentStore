package com.example.equipmentstore.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ViewComplaint implements Serializable {

    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("NAME")
    @Expose
    private String NAME;
    @SerializedName("SUBDIVCODE")
    @Expose
    private String SUBDIVCODE;
    @SerializedName("MOBILE")
    @Expose
    private String MOBILE;
    @SerializedName("TYPE")
    @Expose
    private String TYPE;
    @SerializedName("DESCRIPTION")
    @Expose
    private String DESCRIPTION;

    public String getMessage() {
        return Message;
    }

    @SerializedName("Message")
    @Expose
    private String Message;

    @SerializedName("DATE_TIME")
    @Expose
    private String DATE_TIME;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSUBDIVCODE() {
        return SUBDIVCODE;
    }

    public void setSUBDIVCODE(String SUBDIVCODE) {
        this.SUBDIVCODE = SUBDIVCODE;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDATE_TIME() {
        return DATE_TIME;
    }

    public void setDATE_TIME(String DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }
}
