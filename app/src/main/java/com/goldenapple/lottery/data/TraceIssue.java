package com.goldenapple.lottery.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ACE-PC on 2016/4/6.
 */
public class TraceIssue {
    /**
     * number : 180721101
     * time : 2018-07-21 22:24:55
     * limit_amount : 400000
     */

    @SerializedName("number")
    private String number;
    @SerializedName("time")
    private String time;
    @SerializedName("limit_amount")
    private int limitAmount;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

}
