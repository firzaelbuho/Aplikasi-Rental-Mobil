package com.mobileapps.uas.model;

public class Report {

    private String date = "";
    private int orderCount = 0;
    private int income = 0;
    private long timeStamp = 0;

    public Report(String date, int orderCount, int income, long timeStamp) {
        this.date = date;
        this.orderCount = orderCount;
        this.income = income;
        this.timeStamp = timeStamp;
    }

    public Report(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
