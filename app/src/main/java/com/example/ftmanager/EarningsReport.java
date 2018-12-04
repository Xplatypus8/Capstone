package com.example.ftmanager;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class EarningsReport implements Parcelable {
    private int id, locationID, userID;
    private BigDecimal cash, credit, total;
    private String date;

    public EarningsReport(String [] data){
        id = Integer.parseInt(data[0]);
        locationID = Integer.parseInt(data[1]);
        userID = Integer.parseInt(data[2]);
        cash = new BigDecimal(data[3]);
        credit = new BigDecimal(data[4]);
        total = cash.add(credit);
        date = data[5];

    }

    protected EarningsReport(Parcel in) {
        id = in.readInt();
        locationID = in.readInt();
        userID = in.readInt();
        cash = BigDecimal.valueOf(in.readDouble());
        credit = BigDecimal.valueOf(in.readDouble());
        date = in.readString();
        total = cash.add(credit);
    }

    public static final Creator<EarningsReport> CREATOR = new Creator<EarningsReport>() {
        @Override
        public EarningsReport createFromParcel(Parcel in) {
            return new EarningsReport(in);
        }

        @Override
        public EarningsReport[] newArray(int size) {
            return new EarningsReport[size];
        }
    };

    public int getId(){return id;}

    public int getUserID() {
        return userID;
    }

    public int getLocationID() {
        return locationID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(locationID);
        parcel.writeInt(userID);
        //total is not needed for passing between activities.
        parcel.writeDouble(cash.doubleValue());
        parcel.writeDouble(credit.doubleValue());
        parcel.writeString(date);
    }

    //Used to format date in several activities
    public static String formatDateYYYYMMDD(String mmddyyyy){
        String [] dateArray = mmddyyyy.split("/");
        return dateArray[2] + "-" + dateArray[0] + "-" + dateArray[1];
    }

    //Used to format date in several activities
    public static String formatDateMMDDYYYY(String yyyymmdd){
        String [] dateArray = yyyymmdd.split("-");
        return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0];
    }
}
