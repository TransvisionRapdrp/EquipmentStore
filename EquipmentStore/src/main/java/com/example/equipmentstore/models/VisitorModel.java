package com.example.equipmentstore.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitorModel {

    @SerializedName("Dateofvisit")
    @Expose
    private String dateofvisit;
    @SerializedName("VisitorsName")
    @Expose
    private String visitorsName;
    @SerializedName("romVisitor")
    @Expose
    private String romVisitor;
    @SerializedName("PurposeofVisit")
    @Expose
    private String purposeofVisit;
    @SerializedName("ToMeet")
    @Expose
    private String toMeet;
    @SerializedName("ContactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("PriorityAppointed")
    @Expose
    private String priorityAppointed;
    @SerializedName("Mailid")
    @Expose
    private String mailid;
    @SerializedName("InTime")
    @Expose
    private String inTime;
    @SerializedName("Outtime")
    @Expose
    private String outtime;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("Incharge")
    @Expose
    private String incharge;

    public String getDateofvisit() {
        return dateofvisit;
    }

    public void setDateofvisit(String dateofvisit) {
        this.dateofvisit = dateofvisit;
    }

    public String getVisitorsName() {
        return visitorsName;
    }

    public void setVisitorsName(String visitorsName) {
        this.visitorsName = visitorsName;
    }

    public String getRomVisitor() {
        return romVisitor;
    }

    public void setRomVisitor(String romVisitor) {
        this.romVisitor = romVisitor;
    }

    public String getPurposeofVisit() {
        return purposeofVisit;
    }

    public void setPurposeofVisit(String purposeofVisit) {
        this.purposeofVisit = purposeofVisit;
    }

    public String getToMeet() {
        return toMeet;
    }

    public void setToMeet(String toMeet) {
        this.toMeet = toMeet;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPriorityAppointed() {
        return priorityAppointed;
    }

    public void setPriorityAppointed(String priorityAppointed) {
        this.priorityAppointed = priorityAppointed;
    }

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIncharge() {
        return incharge;
    }

    public void setIncharge(String incharge) {
        this.incharge = incharge;
    }

}
