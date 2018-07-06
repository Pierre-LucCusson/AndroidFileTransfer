package com.androidfiletransfer.connection;

import android.app.Activity;
import android.os.AsyncTask;

public abstract class AsyncCommand extends AsyncTask<Void, Integer, String> {
    protected Activity activity;
    protected Client client;
    protected String command;

    public AsyncCommand(Activity activity, Client client) {
        this.activity = activity;
        this.client = client;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return client.run(command);
    }
}
