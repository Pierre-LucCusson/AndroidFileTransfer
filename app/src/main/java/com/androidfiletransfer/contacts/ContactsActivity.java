package com.androidfiletransfer.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.androidfiletransfer.R;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        String contactsInJson = getIntent().getStringExtra("EXTRA_CONTACTS");
        ContactsViewHandler contactsView = new ContactsViewHandler(this, contactsInJson);
        contactsView.setContactsRecyclerViewContent();
    }
}
