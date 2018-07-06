package com.androidfiletransfer.connection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Client {

    private OkHttpClient client;
    private String serverUrl;

    public Client(String serverUrl) {
        this.serverUrl = "http://" + serverUrl;
        client = new OkHttpClient();
    }


    public String run(String url) {

        Request request = new Request.Builder()
                .url(serverUrl + url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }
}
