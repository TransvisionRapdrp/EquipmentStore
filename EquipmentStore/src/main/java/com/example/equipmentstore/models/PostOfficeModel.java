package com.example.equipmentstore.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostOfficeModel implements Serializable {

    private String role="";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

   /* @SerializedName("Type_of_post")
    @Expose
    private String Type_of_post;

    @SerializedName("InOut")
    @Expose
    private String InOut;*/

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        message = message;
    }

    @SerializedName("Type_of_post")
    @Expose
    private String Type_of_post;

    @SerializedName("InOut")
    @Expose
    private String InOut;

    public String getInOut() {
        return InOut;
    }

    public void setInOut(String inOut) {
        InOut = inOut;
    }

    public String getType_of_post() {
        return Type_of_post;
    }

    public void setType_of_post(String type_of_post) {
        Type_of_post = type_of_post;
    }



    @SerializedName("LetterFrom")
    @Expose
    private String LetterFrom;

    @SerializedName("LetterTo")
    @Expose
    private String LetterTo;

    @SerializedName("DocketNo")
    @Expose
    private String DocketNo;

    @SerializedName("PostalName")
    @Expose
    private String PostalName;

    @SerializedName("ReceivedName")
    @Expose
    private String ReceivedName;

    @SerializedName("Dispatch")
    @Expose
    private String Dispatch;

    @SerializedName("Authorized")
    @Expose
    private String Authorized;

    @SerializedName("Senders")
    @Expose
    private String Senders;

    @SerializedName("Date")
    @Expose
    private String Date;

    @SerializedName("Branch_Name")
    @Expose
    private String Branch_Name;

    @SerializedName("SUBDIVCODE")
    @Expose
    private String SUBDIVCODE;

    @SerializedName("ID")
    @Expose
    private String ID;

    @SerializedName("Status")
    @Expose
    private String Status;

    @SerializedName("SDO_STATUS")
    @Expose
    private String SDO_STATUS;

    @SerializedName("SDO_STATUS_DATE")
    @Expose
    private String SDO_STATUS_DATE;

    @SerializedName("DIV_STATUS")
    @Expose
    private String DIV_STATUS;

    @SerializedName("DIV_STATUS_DATE")
    @Expose
    private String DIV_STATUS_DATE;

    @SerializedName("Remarks")
    @Expose
    private String Remarks;

    @SerializedName("PostID")
    @Expose
    private String PostID;

    @SerializedName("UserName")
    @Expose
    private String UserName;
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    /*public String getType_of_post() {
            return Type_of_post;
        }

        public void setType_of_post(String type_of_post) {
            Type_of_post = type_of_post;
        }

        public String getInOut() {
            return InOut;
        }

        public void setInOut(String inOut) {
            InOut = inOut;
        }
    */
    public String getLetterFrom() {
        return LetterFrom;
    }

    public void setLetterFrom(String letterFrom) {
        LetterFrom = letterFrom;
    }

    public String getLetterTo() {
        return LetterTo;
    }

    public void setLetterTo(String letterTo) {
        LetterTo = letterTo;
    }

    public String getDocketNo() {
        return DocketNo;
    }

    public void setDocketNo(String docketNo) {
        DocketNo = docketNo;
    }

    public String getPostalName() {
        return PostalName;
    }

    public void setPostalName(String postalName) {
        PostalName = postalName;
    }

    public String getReceivedName() {
        return ReceivedName;
    }

    public void setReceivedName(String receivedName) {
        ReceivedName = receivedName;
    }

    public String getDispatch() {
        return Dispatch;
    }

    public void setDispatch(String dispatch) {
        Dispatch = dispatch;
    }

    public String getAuthorized() {
        return Authorized;
    }

    public void setAuthorized(String authorized) {
        Authorized = authorized;
    }

    public String getSenders() {
        return Senders;
    }

    public void setSenders(String senders) {
        Senders = senders;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBranch_Name() {
        return Branch_Name;
    }

    public void setBranch_Name(String branch_Name) {
        Branch_Name = branch_Name;
    }

    public String getSUBDIVCODE() {
        return SUBDIVCODE;
    }

    public void setSUBDIVCODE(String SUBDIVCODE) {
        this.SUBDIVCODE = SUBDIVCODE;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSDO_STATUS() {
        return SDO_STATUS;
    }

    public void setSDO_STATUS(String SDO_STATUS) {
        this.SDO_STATUS = SDO_STATUS;
    }

    public String getSDO_STATUS_DATE() {
        return SDO_STATUS_DATE;
    }

    public void setSDO_STATUS_DATE(String SDO_STATUS_DATE) {
        this.SDO_STATUS_DATE = SDO_STATUS_DATE;
    }

    public String getDIV_STATUS() {
        return DIV_STATUS;
    }

    public void setDIV_STATUS(String DIV_STATUS) {
        this.DIV_STATUS = DIV_STATUS;
    }

    public String getDIV_STATUS_DATE() {
        return DIV_STATUS_DATE;
    }

    public void setDIV_STATUS_DATE(String DIV_STATUS_DATE) {
        this.DIV_STATUS_DATE = DIV_STATUS_DATE;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        PostID = postID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}

