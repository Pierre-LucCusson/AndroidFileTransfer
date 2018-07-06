package com.androidfiletransfer.files;

import android.app.Activity;

import com.androidfiletransfer.connection.AsyncCommand;
import com.androidfiletransfer.connection.Client;
import com.androidfiletransfer.connection.ServerCommand;

public class AsyncGetFile extends AsyncCommand {
    public AsyncGetFile(Activity activity, Client client) {
        super(activity, client);
        this.command = ServerCommand.GET_FILE;
    }

    @Override
    protected void onPostExecute(String result) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                // TODO:
            }
        });
    }
}
