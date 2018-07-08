package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.content.SharedPreferences;
import com.google.gson.Gson;

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

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
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

}
