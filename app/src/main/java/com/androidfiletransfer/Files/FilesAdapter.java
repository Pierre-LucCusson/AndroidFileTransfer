package com.androidfiletransfer.Files;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfiletransfer.R;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {

    private Activity activity;
    private String[] files;

    public FilesAdapter(String[] files, Activity activity) {
        this.activity = activity;
        this.files = files;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.setFileText(files[i]);
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fileText;

        public MyViewHolder(View itemView) {
            super(itemView);

            fileText = itemView.findViewById(R.id.fileText);

        }

        @Override
        public void onClick(View v) {
//            getAdapterPosition(); //TODO should download the file
        }

        public void setFileText(String text) {
            fileText.setText(text);
        }
    }
}
