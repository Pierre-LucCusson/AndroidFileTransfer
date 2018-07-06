package com.androidfiletransfer;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public void add(SharedPreferences sharedPreferences, List contacts) {
        // Add current contact to the list
        contacts.add(this);

        save(sharedPreferences, contacts);
    }

    public void delete(SharedPreferences sharedPreferences, List contacts) {
        // Remove current contact from the list
        contacts.remove(this);

        save(sharedPreferences, contacts);
    }

    public void save(SharedPreferences sharedPreferences, List contacts) {
        // Save the contacts list in shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("Contacts", new Gson().toJson(contacts));
        editor.commit();
    }

}
