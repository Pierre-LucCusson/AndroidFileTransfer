package com.androidfiletransfer.connection;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;

import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.Position;
import com.androidfiletransfer.Tracker;
import com.androidfiletransfer.files.MyFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import fi.iki.elonen.NanoHTTPD;

public class Server extends NanoHTTPD {
    Activity activity;

    public static final int PORT = 8080;

    public Server() {
        super(PORT);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Response serve(IHTTPSession session) {

        if(session.getMethod() == Method.GET) {
            String uri = session.getUri();
            String command = new ServerCommand().get(uri);

            switch (command) {
                case ServerCommand.GET_CONTACTS_LIST:
                    return getContactsList();
                case ServerCommand.GET_CONTACT:
                    return getContact();
                case ServerCommand.GET_FILES_LIST:
                    return getFilesList();
                case ServerCommand.GET_FILE:
                    return getFile(session);
                case ServerCommand.GET_LOCATION:
                    return getLocation();
                default:
                    return newFixedLengthResponse(ServerCommand.DOES_NOT_EXIST);
            }
        }
        else {
            return newFixedLengthResponse(ServerCommand.DOES_NOT_EXIST);
        }
    }

    public Response getContactsList() {
        return newFixedLengthResponse(ServerCommand.GET_CONTACTS_LIST); //TODO maybe not
    }

    public Response getContact() {
        return newFixedLengthResponse(ServerCommand.GET_CONTACT); //TODO maybe not
    }

    public Response getFilesList() {
        return newFixedLengthResponse(MyFile.getFileInstanceFromDirectoryDownload().toJson());
    }

    public Response getFile(IHTTPSession session) {

        String[] uriSection = session.getUri().split(ServerCommand.GET_FILE + "/");

        File file = new File(uriSection[uriSection.length - 1]);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, fileInputStream, file.length()); //TODO can be downloaded on browser but not with client
//            return newFixedLengthResponse(fileInputStream.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(ServerCommand.FILE_NOT_FOUND);
    }

    public Response getLocation() {
        Location location = ((MainActivity)activity).getTracker().getLastLocation();
        return newFixedLengthResponse(new Position(location.getLatitude(), location.getLongitude()).toJson());
    }

}
