package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.location.Location;

import com.androidfiletransfer.Position;
import com.androidfiletransfer.Tracker;
import com.androidfiletransfer.connection.Client;
import com.androidfiletransfer.connection.ServerCommand;
import com.google.gson.Gson;

import java.util.List;
import java.util.Observable;

public class ContactsUpdater extends Observable {

    private static ContactsUpdater INSTANCE = null;
    private Activity activity;
    private List<Contact> contacts;
    private Tracker tracker;

    private ContactsUpdater(Activity activity) {
        this.activity = activity;
        tracker = new Tracker(activity);

        updateAllContacts();
    }

    public static ContactsUpdater getInstance(final Activity activity) {
        if (INSTANCE == null) {
            INSTANCE = new ContactsUpdater(activity);
        }
        return INSTANCE;
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
                setLocationAndSave(contact, position);

                setChanged();
                notifyObservers(contact);

            } else {
                if (contact.setOfflineAndSave(activity)) {
                    setChanged();
                    notifyObservers(contact);
                }

            }
        }
    }

    private boolean contactStillExist(String deviceId) {
        Contact contact = Contact.getWith(deviceId, activity);
        return contact != null;
    }

    private void setLocationAndSave(Contact contact, Position positionOfContact) {

        Location location = new Location("");
        location.setLatitude(positionOfContact.getLatitude());
        location.setLongitude(positionOfContact.getLongitude());

        contact.setDistance(location.distanceTo(tracker.getLastLocation()));
        contact.save(activity);
    }


}
