package com.example.ftmanager;

public class Schedule {

    private String employeeOne, employeeTwo, date;
    private int id, locationID;
    private boolean updateRequired;

    public Schedule(String employeeOne, String employeeTwo, String date, int locationID){
        this.employeeOne = employeeOne;
        this.employeeTwo = employeeTwo;
        this.date = date;
        this.locationID = locationID;
        this.updateRequired = false;
    }

    public Schedule(String [] data){
        this.id = Integer.parseInt(data[0]);
        this.locationID = Integer.parseInt(data[1]);
        this.employeeOne = data[2];
        this.employeeTwo = data[3];
        this.date = EarningsReport.formatDateMMDDYYYY(data[4]);
        this.updateRequired = false;

    }


    public String getEmployeeOne() {
        return employeeOne;
    }

    public String getEmployeeTwo() {
        return employeeTwo;
    }

    public String getDate() {
        return date;
    }

    public int getLocationID() {
        return locationID;
    }
    public int getID(){return id;}

    public boolean updateRequired(){return updateRequired;}

    public void setUpdateRequired(boolean updateRequired){this.updateRequired = updateRequired;}

    public void setEmployeeOne(String name){this.employeeOne = name;}

    public void setEmployeeTwo(String name){this.employeeTwo = name;}

    public void setID(int id){this.id = id;}


}
