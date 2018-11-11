package com.example.ftmanager;

import java.math.BigDecimal;

public class EarningsReport {
    private int userID, locationID;
    private BigDecimal cash, credit, total;
    private String date;

    public EarningsReport(String [] data){
        locationID = Integer.parseInt(data[1]);
        userID = Integer.parseInt(data[2]);
        cash = new BigDecimal(data[3]);
        credit = new BigDecimal(data[4]);
        total = cash.add(credit);
        date = data[5];

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getDayVal(){
        return Double.parseDouble(date.split("-")[2]);
    }


}
