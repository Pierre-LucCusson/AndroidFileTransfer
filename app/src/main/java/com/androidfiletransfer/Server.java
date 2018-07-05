package com.androidfiletransfer;

import fi.iki.elonen.NanoHTTPD;

public class Server extends NanoHTTPD {

    public static final int PORT = 8080;

    public Server() {
        super(PORT);
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
                    return getFile();
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
        return newFixedLengthResponse(ServerCommand.GET_CONTACTS_LIST); //TODO
    }

    public Response getContact() {
        return newFixedLengthResponse(ServerCommand.GET_CONTACT); //TODO
    }

    public Response getFilesList() {
        return newFixedLengthResponse(ServerCommand.GET_FILES_LIST); //TODO
    }

    public Response getFile() {
        return newFixedLengthResponse(ServerCommand.GET_FILE); //TODO
    }

    public Response getLocation() {
        return newFixedLengthResponse(ServerCommand.GET_LOCATION); //TODO
    }

}
