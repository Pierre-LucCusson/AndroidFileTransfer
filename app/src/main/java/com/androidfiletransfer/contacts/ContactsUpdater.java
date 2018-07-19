package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.widget.Toast;

import com.androidfiletransfer.connection.Client;
import com.androidfiletransfer.connection.ServerCommand;

import java.util.List;

public class ContactsUpdater {

    private Activity activity;
    private List<Contact> contacts;

    public ContactsUpdater(Activity activity) {
        this.activity = activity;
        contacts = new Contacts(activity).get();
        updateAllContacts();
    }

    private void updateAllContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (Contact contact : contacts) {
                        updateContact(contact);
                    }
                }
            }
        }).start();
    }

    private void updateContact(final Contact contact) {
        String location = new Client(contact.getIpAddress()).run(ServerCommand.GET_LOCATION);
        if (location != null) {
            contact.setLocationAndSave(location, activity);
            //TODO notify contacts view
//            //for testing
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(activity.getApplicationContext(), contact.getIpAddress() + " is online", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
        else {
            if(contact.setOfflineAndSave(activity)) {
                //TODO notify contacts view
            }
//            //for testing
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(activity.getApplicationContext(), contact.getIpAddress() + " is offline", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }


}
