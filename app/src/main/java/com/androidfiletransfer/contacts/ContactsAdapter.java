package com.androidfiletransfer.contacts;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidfiletransfer.R;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>{

    private Contacts contacts;
    private Activity activity;

    public ContactsAdapter(Contacts contacts, Activity activity) {
        this.contacts = contacts;
        this.activity = activity;
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
        Contact contact = contacts.get(position);
        contact.delete(activity);
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

            status = (ImageView) itemView.findViewById(R.id.imageViewStatus);
            name = (TextView) itemView.findViewById(R.id.name);
            distance = (TextView) itemView.findViewById(R.id.distance);
            lastAccess = (TextView) itemView.findViewById(R.id.lastAccess);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            delete(getAdapterPosition());
        }
    }

}