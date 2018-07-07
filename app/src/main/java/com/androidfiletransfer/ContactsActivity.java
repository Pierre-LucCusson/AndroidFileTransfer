package com.androidfiletransfer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.androidfiletransfer.contacts.ContactsViewHandler;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ContactsViewHandler contactsView = new ContactsViewHandler(this);
        contactsView.setContactsRecyclerViewContent();
    }
}
