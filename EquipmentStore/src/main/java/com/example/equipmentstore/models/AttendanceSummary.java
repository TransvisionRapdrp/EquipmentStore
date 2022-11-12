package com.example.equipmentstore.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceSummary {

    @SerializedName("USERNAME")
    @Expose
    private String USERNAME;
    @SerializedName("ADDRESS")
    @Expose
    private String ADDRESS;
    @SerializedName("SUBDIVCODE")
    @Expose
    private String SUBDIVCODE;

    public String getREMARK() {
        return REMARK;
    }

    @SerializedName("REMARK")
    @Expose
    private String REMARK;

    @SerializedName("LOG")
    @Expose
    private String LONGITUDE;
    @SerializedName("LAT")
    @Expose
    private String LATITUDE;

    @SerializedName("MOBILE_NO")
    @Expose
    private String MOBILE_NO;

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public void setDATETIME(String DATETIME) {
        this.DATETIME = DATETIME;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getSUBDIVCODE() {
        return SUBDIVCODE;
    }

    public String getDATETIME() {
        return DATETIME;
    }

    @SerializedName("DATETIME")
    @Expose
    private String DATETIME;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @SerializedName("Message")
    @Expose
    private String Message;
}
