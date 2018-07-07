package com.androidfiletransfer.contacts;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidfiletransfer.Contact;
import com.androidfiletransfer.ContactsActivity;
import com.androidfiletransfer.ContactsAdapter;
import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ContactsViewHandler {

    private AppCompatActivity activity;
    private RecyclerView contactsRecyclerView;
    private RecyclerView.Adapter contactsAdapter;
    private RecyclerView.LayoutManager contactsLayoutManager;

    public ContactsViewHandler(MainActivity mainActivity) {
        this.activity = mainActivity;
        contactsRecyclerView = mainActivity.findViewById(R.id.contactsRecyclerView);
        init();
    }

    public ContactsViewHandler(ContactsActivity contactsActivity) {
        this.activity = contactsActivity;
        contactsRecyclerView = contactsActivity.findViewById(R.id.contactsRecyclerView);
        init();
    }

    private void init() {
        contactsRecyclerView.setHasFixedSize(true);
        contactsLayoutManager = new LinearLayoutManager(activity);
        contactsRecyclerView.setLayoutManager(contactsLayoutManager);
    }

    public void setContactsRecyclerViewContent() {
        SharedPreferences sharedPrefs = activity.getSharedPreferences("Contacts", 0);
        contactsAdapter = new ContactsAdapter(getContacts(), activity, sharedPrefs);
        contactsRecyclerView.setAdapter(contactsAdapter);
    }

    private List<Contact> getContacts() {
        SharedPreferences sharedPrefs = activity.getSharedPreferences("Contacts", 0);
        List<Contact> contacts = new Gson().fromJson(sharedPrefs.getString("Contacts", ""), List.class);

        // TODO Remove this eventually, keep it for testing or comment it
        if (contacts == null) {
            contacts = new ArrayList<Contact>();

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

        return contacts;
    }

}
