package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Contacts implements Comparable<Contact> {

    private List<Contact> contacts;

    @Deprecated //TODO to remove
    public Contacts() {
        contacts = new ArrayList<>();

        Contact contact1 = new Contact("DeviceId5", "192.168.10.9");
        contact1.setDistance(8);
        contact1.setLastLogin(5);
        contact1.setOnline(true);
        contacts.add(contact1);

        Contact contact2 = new Contact("DeviceId1", "192.168.10.5");
        contact2.setDistance(10);
        contact2.setLastLogin(4);
        contact2.setOnline(false);
        contacts.add(contact2);

        Contact contact3 = new Contact("DeviceId8", "192.168.10.2");
        contact3.setDistance(7);
        contact3.setLastLogin(9);
        contact3.setOnline(true);
        contacts.add(contact3);

        Contact contact4 = new Contact("DeviceId2", "192.168.10.10");
        contact4.setDistance(2);
        contact4.setLastLogin(8);
        contact4.setOnline(true);
        contacts.add(contact4);

        Contact contact5 = new Contact("DeviceId10", "192.168.10.3");
        contact5.setDistance(0);
        contact5.setLastLogin(5);
        contact5.setOnline(false);
        contacts.add(contact5);
    }

    public Contacts(Activity activity) {
        SharedPreferences sharedPrefs = activity.getSharedPreferences("Contacts", 0);

        Map<String, ?> mapsContacts = sharedPrefs.getAll();
        String[] stringMapsContacts = mapsContacts.values().toArray(new String[0]);
        contacts = new ArrayList<>();
        for (String stringMapsContact : stringMapsContacts) {
            contacts.add(new Gson().fromJson(stringMapsContact, Contact.class));
        }
    }

    public Contacts(String contactsInJson) {
        contacts = new ArrayList<>();
        Contact[] contactsList = new Gson().fromJson(contactsInJson, Contact[].class);
        for (Contact contact : contactsList) {
            contacts.add(contact);
        }
    }

    public List<Contact> get() {
        return contacts;
    }

    public Contact get(int index) {
        return contacts.get(index);
    }

    public void delete(int index, Activity activity) {
        contacts.get(index).delete(activity);
        contacts.remove(index);
    }

    public String toJson() {
        return new Gson().toJson(contacts);
    }

    @Override
    public int compareTo(Contact contactToCompareWith) {
        return compareTo(contactToCompareWith);
    }

    public void orderByDeviceId() {
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact contactA, Contact contactB) {
                return contactA.getDeviceId().compareTo(contactB.getDeviceId());
            }
        });
    }

    public void orderByIpAddress() {

        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact contactA, Contact contactB) {
                return contactA.getIpAddress().compareTo(contactB.getIpAddress());
            }
        });
    }

    public void orderByDistance() {

        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact contactA, Contact contactB) {
                return (Double.valueOf(contactA.getDistance())).compareTo(Double.valueOf(contactB.getDistance()));
            }
        });
    }

    public void orderByLastLogin() {

        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact contactA, Contact contactB) {
                return (Long.valueOf(contactA.getLastLogin())).compareTo(Long.valueOf(contactB.getLastLogin()));
            }
        });
    }


    public void orderBy(SortContactsBy sortContactsBy) {
        if (sortContactsBy != null) {
            switch (sortContactsBy) {
                case DEVICE_ID:
                    orderByDeviceId();
                    return;
                case IP_ADDRESS:
                    orderByIpAddress();
                    return;
                case DISTANCE:
                    orderByDistance();
                    return;
                case LAST_LOGIN:
                    orderByLastLogin();
                    return;
                default:
                    return;
            }
        }
    }
}
