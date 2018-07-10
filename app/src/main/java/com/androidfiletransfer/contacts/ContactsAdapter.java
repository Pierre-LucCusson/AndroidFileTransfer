package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.R;

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
        Button btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Contact contact = contacts.get(position);
//                    openFileDownloadActivity(); // TODO
                }
            });

            status = (ImageView) itemView.findViewById(R.id.imageViewStatus);
            name = (TextView) itemView.findViewById(R.id.name);
            distance = (TextView) itemView.findViewById(R.id.distance);
            lastAccess = (TextView) itemView.findViewById(R.id.lastAccess);

            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(this);
            if(activity.getClass() == ContactsActivity.class) {
                btnDelete.setBackgroundResource(android.R.drawable.ic_menu_save);
            }
        }

        @Override
        public void onClick(View v) {
            if(activity.getClass() == MainActivity.class) {
                delete(getAdapterPosition());
            }
            else if (activity.getClass() == ContactsActivity.class) {
                Contact contact = contacts.get(getAdapterPosition());
                contact.save(activity);
            }
        }
    }

}