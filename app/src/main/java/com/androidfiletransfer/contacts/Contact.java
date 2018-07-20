package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.content.SharedPreferences;

import com.androidfiletransfer.Position;
import com.google.gson.Gson;

import java.util.Date;

public class Contact {

    private String deviceId;
    private String ipAddress;
    private boolean isOnline;
    private double distance;
    private transient double latitude;
    private transient double longitude;
    private long lastLogin;

    public Contact(String deviceId, String ipAddress) {

        this.deviceId = deviceId;
        this.ipAddress = ipAddress;
        isOnline = false;
        distance = 0;
        latitude = 0;
        longitude = 0;
        lastLogin = 0;
    }

    public static Contact getMyInformation(String deviceId, String ipAddress) {
        Contact myContactInformation = new Contact(deviceId, ipAddress);
        myContactInformation.setOnline(true);
        myContactInformation.setLastLoginToNow();
        return myContactInformation;
    }

    public static Contact getWith(String deviceId, Activity activity) {
        SharedPreferences sharedPrefs = activity.getSharedPreferences("Contacts", 0);
        String contactInJson = sharedPrefs.getString(deviceId, "");
        return new Gson().fromJson(contactInJson, Contact.class);
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
        isOnline = true;
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

    public void setLocationAndSave(Position position, Activity activity) {
        this.latitude = position.getLatitude();
        this.longitude = position.getLongitude();
        distance++; //TODO should actually set the distance
        setLastLoginToNow();
        save(activity);
    }
}
