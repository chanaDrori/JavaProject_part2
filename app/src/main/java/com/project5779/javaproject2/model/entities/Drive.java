/**
 * Project in Java- Android.
 * Writers - Tirtza Raaya Rubinstain && Chana Drori
 * 12/2018
 * the class drive describe drive in a taxis company.
 */
package com.project5779.javaproject2.model.entities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Drive {
    private StateOfDrive state;
    //private Location startPoint;
    //private Location endPoint;
    private String startTime;
    private String endTime;
    private String nameClient;
    private String phoneClient;
    private String emailClient;
    private String startPointString;
    private String endPointString;
    private String driverID;

    private String key;

    /**
     * constructor of drive.
     * @param _state enum- state of the ride (start/work/end)
     * @param _startPoint string - The starting point of the ride
     * @param _endPoint string - The ending point of the ride
     * @param _startTime string - The start time of the ride
     * @param _endTime string - The end time of the ride
     * @param _nameClient string - The client's name
     * @param _phoneClient string - The client's phone number
     * @param _emailClient string - The client's mail address
     */
    public Drive(StateOfDrive _state, Location _startPoint, Location _endPoint, String _startTime, String _endTime,
                 String _nameClient, String _phoneClient, String _emailClient)
    {
        this.state = _state;
        //this.startPoint = _startPoint;
        //this.endPoint = _endPoint;
        this.startTime = _startTime;
        this.endTime = _endTime;
        this.nameClient = _nameClient;
        this.phoneClient = _phoneClient;
        this.emailClient = _emailClient;
    }

    public Drive(String _state, String _startTime, String _endTime,
                 String _nameClient, String _phoneClient, String _emailClient)
    {
        this.state = StateOfDrive.valueOf(_state);
        this.startTime = _startTime;
        this.endTime = _endTime;
        this.nameClient = _nameClient;
        this.phoneClient = _phoneClient;
        this.emailClient = _emailClient;
    }

    /**
     * default constructor
     */
    public Drive() {
        state = StateOfDrive.AVAILABLE;
        driverID = "";
        endTime ="";
        key ="";
    }

    /**
     * getter
     * @return state. enum- state of the ride (start/work/end)
     */
    public StateOfDrive getState() {
        return state;
    }

    /**
     * getter
     * @return endPoint. Location - The ending point of the ride
     */
   /* public Location getEndPoint() {
        return endPoint;
    }*/
    /**
     * getter
     * @return emailClient. string - The client's mail address
     */
    public String getEmailClient() {
        return emailClient;
    }

    /**
     * getter
     * @return endTime. string - The end time of the ride
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * getter
     * @return nameClient. string - The client's name
     */
    public String getNameClient() {
        return nameClient;
    }

    /**
     * getter
     * @return phoneClient. string - The client's phone number
     */
    public String getPhoneClient() {
        return phoneClient;
    }

    /**
     * getter
     * @return startPoint. Location - The starting point of the ride
     */
   /* public Location getStartPoint() {
        return startPoint;
    }*/

    /**
     * getter
     * @return startTime. string - The start time of the ride
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * getter
     * @return startPointString. string - The starting point of the ride
     */
    public String getStartPointString() {
        return startPointString;
    }

    /**
     * setter
     * @param startPointString string - The starting point of the ride
     */
    public void setStartPointString(String startPointString) {
        this.startPointString = startPointString;
    }

    /**
     * getter
     * @return endPointString. string - The ending point of the ride
     */
    public String getEndPointString() {
        return endPointString;
    }

    /**
     * setter
     * @param endPointString string - The ending point of the ride
     */
    public void setEndPointString(String endPointString) {
        this.endPointString = endPointString;
    }
    /**
     * setter
     * @param endPoint Location - The ending point of the ride
     */
   /* public void setEndPoint(Location endPoint) {
        this.endPoint = endPoint;
    }*/

    /**
     * setter
     * @param emailClient string - The client's mail address
     */
    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    /**
     * setter
     * @param endTime string - The end time of the ride
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * setter
     * @param nameClient string - The client's name
     */
    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    /**
     * setter
     * @param phoneClient string - The client's phone number
     */
    public void setPhoneClient(String phoneClient) {
        this.phoneClient = phoneClient;
    }

    /**
     * setter
     * @param startPoint Location - The starting point of the ride
     */
   /* public void setStartPoint(Location startPoint) {
        this.startPoint = startPoint;
    }*/

    /**
     * setter
     * @param startTime string - The start time of the ride
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * setter
     * @param state enum- state of the ride (start/work/end)
     */
    public void setState(StateOfDrive state) {
        this.state = state;
    }

    /**
     *
     * @param obj object for compare.
     * @return true if the object equals to this Drive.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        Drive drive = (Drive) obj;
        return (//this.startPoint.equals(drive.getStartPoint()) &&
                this.state.equals( drive.getState()) &&
                //this.endPoint.equals(drive.getEndPoint()) &&
                this.emailClient.equals(drive.getEmailClient()) &&
                //this.endTime.equals(drive.getEndTime()) &&
                this.nameClient.equals(drive.getNameClient()) &&
                this.phoneClient.equals(drive.getPhoneClient()) &&
                this.startTime.equals(drive.getStartTime()));
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    @Override
    public String toString() {
        return this.getStartTime() + " " +this.getStartPointString();
    }

    public Location getLocation(Context context) throws Exception {
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        Location locationA = null;
        if(gc.isPresent()){
            List<Address> list = gc.getFromLocationName("1600 Amphitheatre Parkway, Mountain View, CA", 1);
            Address address = list.get(0);
            double lat = address.getLatitude();
            double lng = address.getLongitude();

            locationA = new Location("A");

            locationA.setLatitude(lat);
            locationA.setLongitude(lng);
        }
        return locationA;
    }

    public double getLat(Context context) throws Exception {

        return getLocation(context).getLatitude();
    }

    public double getLon(Context context) throws Exception {

        return getLocation(context).getLongitude();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
