package com.example.equipmentstore.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Staff_OutGoingModel  implements Serializable {

    private String modeoftransport="";

    public String getModeoftransport() {
        return modeoftransport;
    }

    public void setModeoftransport(String modeoftransport) {
        this.modeoftransport = modeoftransport;
    }

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("Visit_To")
    @Expose
    private String Visit_To;

    @SerializedName("Reason")
    @Expose
    private String Reason;

    @SerializedName("Mode_Of_Transport")
    @Expose
    private String Mode_Of_Transport;

    @SerializedName("Out_time")
    @Expose
    private String Out_time;

    @SerializedName("IN__time")
    @Expose
    private String IN__time;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Date")
    @Expose
    private String Date;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVisit_To() {
        return Visit_To;
    }

    public void setVisit_To(String visit_To) {
        Visit_To = visit_To;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getMode_Of_Transport() {
        return Mode_Of_Transport;
    }

    public void setMode_Of_Transport(String mode_Of_Transport) {
        Mode_Of_Transport = mode_Of_Transport;
    }

    public String getOut_time() {
        return Out_time;
    }

    public void setOut_time(String out_time) {
        Out_time = out_time;
    }

    public String getIN__time() {
        return IN__time;
    }

    public void setIN__time(String IN__time) {
        this.IN__time = IN__time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
