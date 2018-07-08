package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Contacts {

    private List<Contact> contacts;

    @Deprecated //TODO to remove
    public Contacts() {
        contacts = new ArrayList<>();

        Contact contact1 = new Contact("DeviceId1", "192.168.10.1");
        contact1.setDistance(1);
        contact1.setLastLogin(1);
        contact1.setOnline(true);
        contacts.add(contact1);

        Contact contact2 = new Contact("DeviceId2", "192.168.10.2");
        contact2.setDistance(2);
        contact2.setLastLogin(2);
        contact2.setOnline(false);
        contacts.add(contact2);

        Contact contact3 = new Contact("DeviceId3", "192.168.10.3");
        contact3.setDistance(3);
        contact3.setLastLogin(3);
        contact3.setOnline(true);
        contacts.add(contact3);

        Contact contact4 = new Contact("DeviceId4", "192.168.10.4");
        contact4.setDistance(4);
        contact4.setLastLogin(4);
        contact4.setOnline(true);
        contacts.add(contact4);

        Contact contact5 = new Contact("DeviceId5", "192.168.10.5");
        contact5.setDistance(5);
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

    public String toJson() {
        return new Gson().toJson(contacts);
    }
}
