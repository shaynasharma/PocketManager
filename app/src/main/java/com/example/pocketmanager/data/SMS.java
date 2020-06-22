package com.example.pocketmanager.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SMS implements Serializable {

    private String msgType;
    private String msgAmt;
    private String msgDate;
    private String msgBal;


    public SMS() {
    }

    public SMS(String msgType, String msgAmt, String msgDate, String msgBal) {
        this.msgType = msgType;
        this.msgAmt = msgAmt;
        this.msgDate = msgDate;
        this.msgBal = msgBal;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public String getDrOrCr() {
        if (this.msgType.equals("Personal Expenses") || this.msgType.equals("Food") || this.msgType.equals("Transport"))
            return "DR";
        else
            return "CR";

    }

    public String getMsgAmt() {
        return this.msgAmt;
    }

    public Double getAmtDouble() {
        return Double.parseDouble(this.msgAmt);
    }

    public String getMsgDate() {
        return this.msgDate;
    }

    public String getMsgBal() {
        return this.msgBal;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setMsgAmt(String msgAmt) {
        this.msgAmt = msgAmt;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public void setMsgBal(String msgBal) {
        this.msgBal = msgBal;
    }

    public long getDateLong() {
        return Long.parseLong(this.msgDate);
    }

    public String getFormatDate() {
        return getDate(Long.parseLong(this.msgDate));
    }

    public String getDay() {
        return new SimpleDateFormat("dd/MM").format(new Date(Long.parseLong(this.msgDate)));
    }

    public String getWeek() {
        return "Week:" + new SimpleDateFormat("W").format(new Date(Long.parseLong(this.msgDate))) + " of " + new SimpleDateFormat("MMM").format(new Date(Long.parseLong(this.msgDate)));
    }

    public String getMonth() {
        return new SimpleDateFormat("MMM").format(new Date(Long.parseLong(this.msgDate))) + "'" + new SimpleDateFormat("yy").format(new Date(Long.parseLong(this.msgDate)));
    }

    public String getTime() {
        return new SimpleDateFormat("HH:mm").format(new Date(Long.parseLong(this.msgDate)));
    }

    public String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        return new SimpleDateFormat("dd/MMM/yy").format(new Date(milliSeconds));
    }
}