package com.example.ftmanager;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name, username, position;
    private int userID;

    public User(String [] values){
        userID = Integer.parseInt(values[0]);
        username = values[1];
        position = values[2];
        name = values[3] + " " + values[4];

    }
    public User(Parcel in) {
        userID = in.readInt();
        username = in.readString();
        name = in.readString();
        position = in.readString();
    }
    public int getUserID(){
        return userID;
    }

    public String getName() {
        return name;
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
        parcel.writeInt(userID);
        parcel.writeString(username);
        parcel.writeString(name);
        parcel.writeString(position);
    }
}
