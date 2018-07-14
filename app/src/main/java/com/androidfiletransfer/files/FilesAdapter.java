package com.androidfiletransfer.files;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.R;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {

    private Activity activity;
    private Files files;

    public FilesAdapter(Files files, Activity activity) {
        this.activity = activity;
        this.files = files;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int index) {
        myViewHolder.setFileText(files.get(index).getFileName());

        if (activity.getClass() == MainActivity.class) {
            myViewHolder.setDownloadButtonVisibilityToGone();
        }
    }

    @Override
    public int getItemCount() {
        return files.get().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fileText;
        private ImageButton downloadButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            fileText = itemView.findViewById(R.id.fileText);
            downloadButton = itemView.findViewById(R.id.downloadButton);
        }

        @Override
        public void onClick(View v) {
//            getAdapterPosition(); //TODO should download the file
        }

        public void setFileText(String text) {
            fileText.setText(text);
        }

        public void setDownloadButtonVisibilityToGone() {
            downloadButton.setVisibility(View.GONE);
        }
    }
}
