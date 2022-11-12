package com.example.equipmentstore.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EquipmentDetails implements Serializable {
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    private Bitmap image;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getSUBDIV_CODE() {
        return SUBDIV_CODE;
    }

    public void setSUBDIV_CODE(String SUBDIV_CODE) {
        this.SUBDIV_CODE = SUBDIV_CODE;
    }

    public String getITEMS() {
        return ITEMS;
    }

    public void setITEMS(String ITEMS) {
        this.ITEMS = ITEMS;
    }

    public String getREQUESTED_DATE() {
        return REQUESTED_DATE;
    }

    public void setREQUESTED_DATE(String REQUESTED_DATE) {
        this.REQUESTED_DATE = REQUESTED_DATE;
    }

    public String getAPPROVED_FLAG() {
        return APPROVED_FLAG;
    }

    public void setAPPROVED_FLAG(String APPROVED_FLAG) {
        this.APPROVED_FLAG = APPROVED_FLAG;
    }

    public String getAPPROVED_DATE() {
        return APPROVED_DATE;
    }

    public void setAPPROVED_DATE(String APPROVED_DATE) {
        this.APPROVED_DATE = APPROVED_DATE;
    }

    public String getRECEIVED_FLAG() {
        return RECEIVED_FLAG;
    }

    public void setRECEIVED_FLAG(String RECEIVED_FLAG) {
        this.RECEIVED_FLAG = RECEIVED_FLAG;
    }

    public String getRECEIVED_DATE() {
        return RECEIVED_DATE;
    }

    public void setRECEIVED_DATE(String RECEIVED_DATE) {
        this.RECEIVED_DATE = RECEIVED_DATE;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    @SerializedName("EnodedFile")
    @Expose
    private String EnodedFile;
    @SerializedName("FILE_NAME")
    @Expose
    private String FileName;
    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("USER_NAME")
    @Expose
    private String USER_NAME;
    @SerializedName("MOBILE_NO")
    @Expose
    private String MOBILE_NO;
    @SerializedName("SUBDIV_CODE")
    @Expose
    private String SUBDIV_CODE;
    @SerializedName("ITEMS")
    @Expose
    private String ITEMS;
    @SerializedName("REQUESTED_DATE")
    @Expose
    private String REQUESTED_DATE;

    @SerializedName("APPROVED_FLAG")
    @Expose
    private String APPROVED_FLAG;
    @SerializedName("APPROVED_DATE")
    @Expose
    private String APPROVED_DATE;
    @SerializedName("RECEIVED_FLAG")
    @Expose
    private String RECEIVED_FLAG;
    @SerializedName("RECEIVED_DATE")
    @Expose
    private String RECEIVED_DATE;
    @SerializedName("REMARK")
    @Expose
    private String REMARK;

    @SerializedName("RESV_REMARKS")
    @Expose
    private String RESV_Remark;

    public String getNAMES() {
        return NAMES;
    }

    public void setNAMES(String NAMES) {
        this.NAMES = NAMES;
    }

    @SerializedName("NAMES")
    @Expose
    private String NAMES;

    @SerializedName("ReceivedName")
    @Expose
    private String ReceivedName;
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @SerializedName("message")
    @Expose
    private String Message;

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getEnodedFile() {
        return EnodedFile;
    }

    public void setEnodedFile(String enodedFile) {
        EnodedFile = enodedFile;
    }

    public String getRESV_Remark() {
        return RESV_Remark;
    }


    public void setRESV_Remark(String RESV_Remark) {
        this.RESV_Remark = RESV_Remark;
    }

    public String getReceivedName() {
        return ReceivedName;
    }

    public void setReceivedName(String receivedName) {
        ReceivedName = receivedName;
    }
}
