package com.androidfiletransfer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ServerService  extends Service {
    private Server server;

    private IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        ServerService getService() {
            return ServerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        server = new Server();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onLowMemory() {
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public IBinder getmBinder() {
        return mBinder;
    }

    public void setmBinder(IBinder mBinder) {
        this.mBinder = mBinder;
    }
}
