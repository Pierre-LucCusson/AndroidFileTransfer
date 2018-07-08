package com.androidfiletransfer.contacts;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.R;

public class ContactsViewHandler {

    private AppCompatActivity activity;
    private RecyclerView contactsRecyclerView;
    private RecyclerView.Adapter contactsAdapter;
    private RecyclerView.LayoutManager contactsLayoutManager;

    private Contacts contacts;

    public ContactsViewHandler(MainActivity mainActivity) {
        this.activity = mainActivity;
        contactsRecyclerView = mainActivity.findViewById(R.id.contactsRecyclerView);
        contacts = new Contacts(mainActivity);
        init();
    }

    public ContactsViewHandler(ContactsActivity contactsActivity, String contactsInJson) {
        this.activity = contactsActivity;
        contactsRecyclerView = contactsActivity.findViewById(R.id.contactsRecyclerView);
        contacts = new Contacts(contactsInJson);
        init();
    }

    private void init() {
        contactsRecyclerView.setHasFixedSize(true);
        contactsLayoutManager = new LinearLayoutManager(activity);
        contactsRecyclerView.setLayoutManager(contactsLayoutManager);
        contactsAdapter = new ContactsAdapter(contacts, activity);
        contactsRecyclerView.setAdapter(contactsAdapter);
    }
}
