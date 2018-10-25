package com.example.ftmanager;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name, surname, username, position;
    private int userID;
    public User(int id, String [] values){
        userID = id;
        username = values[0];
        name = "default";
        surname ="default";
        position = values[1];
    }
    public User(Parcel in) {
        userID = in.readInt();
        String [] values = in.readString().split(",", 0);
        username = values[0];
        name = "default";
        surname ="default";
        position = values[1];
    }
    public int getUserID(){
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPosition() {
        return position;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
