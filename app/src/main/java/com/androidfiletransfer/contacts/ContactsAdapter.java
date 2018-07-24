package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.R;
import com.androidfiletransfer.connection.Client;
import com.androidfiletransfer.connection.ServerCommand;
import com.androidfiletransfer.files.FilesActivity;

import java.util.Observable;
import java.util.Observer;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> implements Observer {

    private Contacts contacts;
    private Activity activity;
    private Observable contactsUpdaterObservable;
    private SortContactsBy sortContactsBy;

    private Button btnSortDeviceID, btnSortIpAddress, btnSortDistance, btnSortLastLogin;

    public ContactsAdapter(final Contacts contacts, Activity activity) {
        this.contacts = contacts;
        this.activity = activity;
        sortContactsBy = SortContactsBy.DEFAULT;

        contactsUpdaterObservable = ContactsUpdater.getInstance(activity);
        contactsUpdaterObservable.addObserver(this);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        setSortButtonsOnClickListener();
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Contact contact = contacts.get(i);

        if (contact.isOnline()) {
            myViewHolder.status.setImageDrawable(activity.getResources().getDrawable(android.R.drawable.presence_online));
        } else {
            myViewHolder.status.setImageDrawable(activity.getResources().getDrawable(android.R.drawable.presence_offline));
        }

        myViewHolder.deviceIdText.setText(contact.getDeviceId());
        myViewHolder.ipAddressText.setText(contact.getIpAddress());
        myViewHolder.distance.setText(String.valueOf(contact.getDistance()));
        myViewHolder.lastAccess.setText(String.valueOf(contact.getLastLoginInDateFormat()));
    }

    @Override
    public int getItemCount() {
        return contacts.get().size();
    }

    public void delete(int position) {
        contacts.delete(position, activity);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView status;
        TextView deviceIdText;
        TextView ipAddressText;
        TextView distance;
        TextView lastAccess;
        ImageButton saveOrDeleteButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Contact contact = contacts.get(position);
                    openFileActivityWithFilesOf(contact);
                }
            });

            status = itemView.findViewById(R.id.imageViewStatus);
            deviceIdText = itemView.findViewById(R.id.deviceIdText);
            ipAddressText = itemView.findViewById(R.id.ipAddressText);
            distance = itemView.findViewById(R.id.distance);
            lastAccess = itemView.findViewById(R.id.lastAccess);

            saveOrDeleteButton = itemView.findViewById(R.id.btnDelete);
            saveOrDeleteButton.setOnClickListener(this);
            if(activity.getClass() == ContactsActivity.class) {
                saveOrDeleteButton.setBackgroundResource(android.R.drawable.ic_menu_save);
            }
        }

        @Override
        public void onClick(View view) {
            if(activity.getClass() == MainActivity.class) {
                delete(getAdapterPosition());
            }
            else if (activity.getClass() == ContactsActivity.class) {
                Contact contact = contacts.get(getAdapterPosition());
                contact.save(activity);
                view.setVisibility(View.GONE);
            }
        }
    }

    private void openFileActivityWithFilesOf(final Contact contact) {

        new Thread(new Runnable() {
            @Override
            public void run() {

//                String filesInJson = MyFile.getFileInstanceFromDirectoryDownload().toJson(); //For debugging/testing
                String filesInJson = new Client(contact.getIpAddress()).run(ServerCommand.GET_FILES_LIST);
                openFileActivityWith(contact, filesInJson);

            }
        }).start();
    }

    private void openFileActivityWith(final Contact contact, String filesInJson) {
        if (filesInJson == null) {

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (contact.setOfflineAndSave(activity)) {
                        notifyDataSetChanged();
                    }
                    Toast.makeText(activity.getApplicationContext(), R.string.contact_offline, Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            if(contact.setOnlineAndSave(activity)) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }

            Intent intentFiles = new Intent(activity, FilesActivity.class);
            intentFiles.putExtra("EXTRA_FILES", filesInJson);
            intentFiles.putExtra("EXTRA_CONTACT_IP_ADDRESS", contact.getIpAddress());
            activity.startActivity(intentFiles);
        }
    }

    private void setSortButtonsOnClickListener() {
        btnSortDeviceID = activity.findViewById(R.id.btnSortID);
        btnSortIpAddress = activity.findViewById(R.id.btnSortIP);
        btnSortDistance = activity.findViewById(R.id.btnSortDistance);
        btnSortLastLogin = activity.findViewById(R.id.btnSortLastLogin);

        btnSortDeviceID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.orderByDeviceId();
                sortContactsBy = SortContactsBy.DEVICE_ID;
                notifyDataSetChanged();
            }
        });

        btnSortIpAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.orderByIpAddress();
                sortContactsBy = SortContactsBy.IP_ADDRESS;
                notifyDataSetChanged();
            }
        });

        btnSortDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.orderByDistance();
                sortContactsBy = SortContactsBy.DISTANCE;
                notifyDataSetChanged();
            }
        });

        btnSortLastLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.orderByLastLogin();
                sortContactsBy = SortContactsBy.LAST_LOGIN;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void update(Observable observable, Object contact) {
        if (observable instanceof ContactsUpdater) {
            String deviceId = ((Contact)contact).getDeviceId();

            contacts = new Contacts(activity);
            contacts.orderBy(sortContactsBy);

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    notifyDataSetChanged();
                }
            });

        }
    }

}