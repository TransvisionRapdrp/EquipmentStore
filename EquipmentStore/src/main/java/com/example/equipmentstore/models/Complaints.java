package com.example.equipmentstore.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Complaints implements Serializable {
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

    @SerializedName("FILENAME")
    @Expose
    private String FILENAME;

    @SerializedName("EnodedFile")
    @Expose
    private String EnodedFile;

    public String getMessage() {
        return Message;
    }

    @SerializedName("message")
    @Expose
    private String Message;

    public String getEnodedFile() {
        return EnodedFile;
    }

    public void setEnodedFile(String enodedFile) {
        EnodedFile = enodedFile;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getSUBDIVCODE() {
        return SUBDIVCODE;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getDATE_TIME() {
        return DATE_TIME;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setSUBDIVCODE(String SUBDIVCODE) {
        this.SUBDIVCODE = SUBDIVCODE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setDATE_TIME(String DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }

    @SerializedName("DATE_TIME")
    @Expose
    private String DATE_TIME;

}
