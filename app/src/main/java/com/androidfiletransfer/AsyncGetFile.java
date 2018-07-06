package com.androidfiletransfer;

import android.app.Activity;

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
