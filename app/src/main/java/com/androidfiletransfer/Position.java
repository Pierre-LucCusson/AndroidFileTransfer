package com.androidfiletransfer;

import com.google.gson.Gson;

public class Position {

    private double latitude;
    private double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Position fromJson (String positionInJson) {
        return new Gson().fromJson(positionInJson, Position.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
