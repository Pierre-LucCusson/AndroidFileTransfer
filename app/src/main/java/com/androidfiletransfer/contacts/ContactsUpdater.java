package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.widget.Toast;

import com.androidfiletransfer.Position;
import com.androidfiletransfer.connection.Client;
import com.androidfiletransfer.connection.ServerCommand;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ContactsUpdater {

    private Activity activity;
    private List<Contact> contacts;
    private List<Contact> contactsToRemove =  new ArrayList<>();

    public ContactsUpdater(Activity activity) {
        this.activity = activity;

        updateAllContacts();
    }

    private void updateAllContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    contacts = new Contacts(activity).get();
                    for (Contact contact : contacts) {
                        updateContact(contact);
                    }
                }
            }
        }).start();
    }

    private void updateContact(final Contact contact) {
        String locationInJson = new Client(contact.getIpAddress()).run(ServerCommand.GET_LOCATION);
        if (contactStillExist(contact.getDeviceId())) {

            if (locationInJson != null) {
                Position position = new Gson().fromJson(locationInJson, Position.class);
                contact.setLocationAndSave(position, activity);
                //TODO notify contacts view
//            //for testing
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity.getApplicationContext(), contact.getIpAddress() + " is online", Toast.LENGTH_SHORT).show();
//                    }
//                });
            } else {
                if (contact.setOfflineAndSave(activity)) {
                    //TODO notify contacts view
                }
//            //for testing
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity.getApplicationContext(), contact.getIpAddress() + " is offline", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        }
    }

    private boolean contactStillExist(String deviceId) {
        Contact contact = Contact.getWith(deviceId, activity);
        return contact != null;
    }


}
