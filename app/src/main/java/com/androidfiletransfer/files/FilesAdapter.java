package com.androidfiletransfer.files;

import android.app.Activity;
import android.opengl.Visibility;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.R;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {

    private Activity activity;
    private MyFiles files;

    public FilesAdapter(MyFiles files, Activity activity) {
        this.activity = activity;
        this.files = files;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int index) {

        MyFile myFile = files.get(index);

        myViewHolder.setFileText(myFile.getFileName());

        myViewHolder.setDownloadButtonVisibility(myFile);
        myViewHolder.setFileLogoTypeVisibility(myFile);
    }

    @Override
    public int getItemCount() {
        return files.get().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView fileLogoTypeView;
        private TextView fileText;
        private ImageButton downloadButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            fileLogoTypeView = itemView.findViewById(R.id.fileLogoTypeView);
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

        public void setDownloadButtonVisibility(MyFile myFile) {
            if (activity.getClass() == MainActivity.class) {
                downloadButton.setVisibility(View.GONE);
            }
            else {
                if(myFile.getFiles() == null) {
                    downloadButton.setVisibility(View.VISIBLE);
                }
                else {
                    downloadButton.setVisibility(View.GONE);
                }
            }
        }

        public void setFileLogoTypeVisibility(MyFile myFile) {
            if(myFile.getFiles() == null) {
                fileLogoTypeView.setVisibility(View.GONE);
            }
            else {
                fileLogoTypeView.setVisibility(View.VISIBLE);
            }
        }
    }
}
