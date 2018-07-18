package com.androidfiletransfer.files;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.R;
import com.androidfiletransfer.connection.ServerCommand;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {

    private Activity activity;
    private MyFile files;

    public FilesAdapter(MyFile files, Activity activity) {
        this.activity = activity;
        this.files = files;
//        if(activity.getClass() == MainActivity.class) {
//            //add folder listener
//            if(newFileOrFolderIsDetectedInDirectoryDownload) { // TODO add listener for detection of new file or folder
//                files = MyFile.getFileInstanceFromDirectoryDownload();
//            }
//        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int index) {

        MyFile myFile = files.getFileAt(index);

        myViewHolder.setFileText(myFile.getFileName());

        myViewHolder.setDownloadButtonVisibility(myFile);
        myViewHolder.setFileLogoTypeVisibility(myFile);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView fileLogoTypeView;
        private TextView fileText;
        private ImageButton downloadButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            fileLogoTypeView = itemView.findViewById(R.id.fileLogoTypeView);
            fileText = itemView.findViewById(R.id.fileText);
            downloadButton = itemView.findViewById(R.id.downloadButton);
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadFile(getAdapterPosition());
                }

            });
        }

        @Override
        public void onClick(View v) {
            onFileClick(getAdapterPosition());
        }

        public void setFileText(String text) {
            fileText.setText(text);
        }

        public void setDownloadButtonVisibility(MyFile myFile) {
            if (activity.getClass() == MainActivity.class) {
                downloadButton.setVisibility(View.GONE);
            }
            else {
                if(myFile.isDirectory()) {
                    downloadButton.setVisibility(View.GONE);
                }
                else {
                    downloadButton.setVisibility(View.VISIBLE);
                }
            }
        }

        public void setFileLogoTypeVisibility(MyFile myFile) {
            if(myFile.isDirectory()) {
                fileLogoTypeView.setVisibility(View.VISIBLE);
            }
            else {
                fileLogoTypeView.setVisibility(View.GONE);
            }
        }
    }

    private void onFileClick(int filePosition) {
        MyFile myFile = files.getFileAt(filePosition);
        if (myFile.getFiles() != null) {
            myFile.toggleOpen();
            notifyDataSetChanged();
        }
    }

    private void downloadFile(int filePosition) {

        String ipAddress = activity.getIntent().getStringExtra("EXTRA_CONTACT_IP_ADDRESS");

        MyFile myFile = files.getFileAt(filePosition);
        Uri fileUri = Uri.parse("https://" + ipAddress + ServerCommand.GET_FILE + myFile.getPath()); //TODO this line does not work, is the problem from the server ?
//        Uri fileUri = Uri.parse("https://2017.brucon.org/images/b/bc/Twitter_logo.jpg"); // for testing/debugging

        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(fileUri);
        request.setTitle(myFile.getFileName());
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, myFile.getFileName());
//        request.setDestinationInExternalFilesDir(activity, Environment.DIRECTORY_DOWNLOADS, myFile.getFileName());
        downloadManager.enqueue(request);

    }
}
