package com.example.ftmanager;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
    private String name;
    private int locationID;

    public Location(String [] values){
        locationID = Integer.parseInt(values[0]);
        name = values[1];

    }
    public Location(Parcel in) {
        locationID = in.readInt();
        name = in.readString();
    }
    public int getLocationID(){
        return locationID;
    }

    public String getName() {
        return name;
    }


    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(locationID);
        parcel.writeString(name);
    }
}
