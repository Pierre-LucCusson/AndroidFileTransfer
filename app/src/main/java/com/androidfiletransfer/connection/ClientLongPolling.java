package com.androidfiletransfer.connection;

import android.app.Activity;

import com.androidfiletransfer.Position;
import com.androidfiletransfer.contacts.Contact;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClientLongPolling {

    private static final int TIMEOUT_LENGTH = 10;

    private static final int OK = 200;
    private static final int REQUEST_TIMEOUT = 408;

    private OkHttpClient client;
    private String serverUrl;

    public ClientLongPolling(String serverUrl) {
        this.serverUrl = "http://" + serverUrl;
        client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_LENGTH, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_LENGTH, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_LENGTH, TimeUnit.SECONDS).build();
    }

    public String run(String url) {

        Request request = new Request.Builder()
                .url(serverUrl + url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.code() == OK) {
                return response.body().string();
            }
            else if (response.code() == REQUEST_TIMEOUT) {
                return run(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateLocation(Activity activity, Contact contact) {
        String url = "http://" + contact.getIpAddress() + ServerCommand.GET_LOCATION;
        String locationInJson = run(url);
        if (locationInJson != null) {
            Position position = new Gson().fromJson(locationInJson, Position.class);
            contact.setLocationAndSave(position, activity);
        }
        else {
            contact.setOfflineAndSave(activity);
        }
    }
}
