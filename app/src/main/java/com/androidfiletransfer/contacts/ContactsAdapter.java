package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.R;
import com.androidfiletransfer.Tracker;
import com.androidfiletransfer.connection.Client;
import com.androidfiletransfer.connection.ServerCommand;
import com.androidfiletransfer.files.FilesActivity;
import com.androidfiletransfer.files.MyFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>{

    private Contacts contacts;
    private Activity activity;

    private Button btnSortIpAddress, btnSortDistance, btnSortLastLogin;

    public ContactsAdapter(final Contacts contacts, Activity activity) {
        this.contacts = contacts;
        this.activity = activity;

        btnSortIpAddress = activity.findViewById(R.id.btnSortIP);
        btnSortDistance = activity.findViewById(R.id.btnSortDistance);
        btnSortLastLogin = activity.findViewById(R.id.btnSortLastLogin);

        btnSortIpAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.orderByIpAddress();
                notifyDataSetChanged();
            }
        });

        btnSortDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.orderByDistance();
                notifyDataSetChanged();
            }
        });

        btnSortLastLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contacts.orderByLastLogin();
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
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

        myViewHolder.name.setText(contact.getIpAddress());
        myViewHolder.distance.setText(String.valueOf(contact.getDistance()));
        myViewHolder.lastAccess.setText(String.valueOf(contact.getLastLogin()));
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
        TextView name;
        TextView distance;
        TextView lastAccess;
        Button saveOrDeleteButton;

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
            name = itemView.findViewById(R.id.name);
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

        // Get location
        String locationJson = new Client(contact.getIpAddress()).run(ServerCommand.GET_LOCATION); //TODO need to be tested

        Tracker tracker = new Tracker(activity);
        Location currentLocation = tracker.getLastLocation();
        double[] coordinated = {currentLocation.getLatitude(), currentLocation.getLongitude()}; // Set default location to client
        if( locationJson != null ) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            coordinated = gson.fromJson(locationJson, double[].class); // Get coordinate from server response
        }

        Location location = new Location(""); // Set coordinate
        location.setLatitude(coordinated[0]);
        location.setLongitude(coordinated[1]);

        contact.setDistance(location.distanceTo(tracker.getLastLocation())); // Calculate coordinate and set to contact



        new Thread(new Runnable() {
            @Override
            public void run() {

//                String filesInJson = MyFile.getFileInstanceFromDirectoryDownload().toJson(); //For debugging/testing
                String filesInJson = new Client(contact.getIpAddress()).run(ServerCommand.GET_FILES_LIST);
                openFileActivityWith(contact, filesInJson);

            }
        }).start();
    }

    private void openFileActivityWith(Contact contact, String filesInJson) {
        if (filesInJson == null) {
            if (contact.isOnline()) {
                contact.setOnline(false);
                contact.save(activity);
                notifyDataSetChanged();
            }
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity.getApplicationContext(), R.string.contact_offline, Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            if (!contact.isOnline()) {
                contact.setOnline(true);
                contact.save(activity);
                notifyDataSetChanged();
            }

            Intent intentFiles = new Intent(activity, FilesActivity.class);
            intentFiles.putExtra("EXTRA_FILES", filesInJson);
            activity.startActivity(intentFiles);
        }
    }

}