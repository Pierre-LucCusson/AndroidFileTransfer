package com.androidfiletransfer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Contact> contacts = new ArrayList<>();
        SharedPreferences sharedPrefs = getSharedPreferences("Contacts", 0);
        contacts = new ArrayList<>();

        // Load contacts
        contacts = new Gson().fromJson(sharedPrefs.getString("Contacts", ""), List.class);

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

        mAdapter = new ContactsAdapter(contacts, this, sharedPrefs);
        mRecyclerView.setAdapter(mAdapter);
    }
}
