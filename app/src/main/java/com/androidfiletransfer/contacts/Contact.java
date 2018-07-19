package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import java.util.Date;

public class Contact {

    private String deviceId;
    private String ipAddress;
    private boolean isOnline;
    private double distance;
    private long lastLogin;

    public Contact(String deviceId, String ipAddress) {

        this.deviceId = deviceId;
        this.ipAddress = ipAddress;
        isOnline = false;
        distance = 0;
        lastLogin = 0;
    }

    public static Contact getMyInformation(String deviceId, String ipAddress) {
        Contact myContactInformation = new Contact(deviceId, ipAddress);
        myContactInformation.setOnline(true);
        myContactInformation.setLastLoginToNow();
        return myContactInformation;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Contact fromJson(String json) {
        return new Gson().fromJson(json, Contact.class);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public String getLastLoginInDateFormat() {
        return new Date(lastLogin).toString();
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setLastLoginToNow() {
        lastLogin = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Contact{" +
                "deviceId='" + deviceId + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", isOnline=" + isOnline +
                ", distance=" + distance +
                ", lastLogin=" + lastLogin +
                '}';
    }


    public void save(Activity activity) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("Contacts", 0).edit();
        editor.putString(deviceId, toJson());
        editor.commit();
    }

    public void delete(Activity activity) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("Contacts", 0).edit();
        editor.remove(deviceId);
        editor.commit();
    }

    public boolean setOnlineAndSave(Activity activity) {
        if (!isOnline) {
            isOnline = true;
            save(activity);
            lastLogin = System.currentTimeMillis();
           return true;
        }
        else {
            return false;
        }
    }

    public boolean setOfflineAndSave(Activity activity) {
        if (isOnline) {
            isOnline = false;
            save(activity);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean setLocationAndSave(String location, Activity activity) { //TODO
//        if(location has change || !isOnline) {
//            setLocation where ???
//            isOnline = true;
//            save(activity);
//            return true;
//        }
        return false;
    }
}
