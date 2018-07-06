package com.androidfiletransfer;

import android.app.Activity;

public class ClientCommandManager {
    private Activity activity;
    private Client client;

    public ClientCommandManager(Activity activity, String url) {
        this.activity = activity;
        this.client = new Client(url);
    }

    public void getFile()
    {
        AsyncCommand asyncCommand = new AsyncGetFile(activity, client);
        asyncCommand.execute();
    }
}
