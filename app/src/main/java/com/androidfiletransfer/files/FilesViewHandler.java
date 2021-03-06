package com.androidfiletransfer.files;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidfiletransfer.MainActivity;
import com.androidfiletransfer.R;

public class FilesViewHandler {

    private Activity activity;
    private RecyclerView filesRecyclerView;
    private RecyclerView.Adapter filesAdapter;
    private RecyclerView.LayoutManager filesLayoutManager;

    public FilesViewHandler(MainActivity mainActivity) {
        this.activity = mainActivity;
        filesRecyclerView = activity.findViewById(R.id.filesRecyclerView);
        filesRecyclerView.setHasFixedSize(true);

        filesLayoutManager = new LinearLayoutManager(activity);
        filesRecyclerView.setLayoutManager(filesLayoutManager);

    }

    public FilesViewHandler(FilesActivity filesActivity) {
        this.activity = filesActivity;
        filesRecyclerView = activity.findViewById(R.id.filesRecyclerView);
        filesRecyclerView.setHasFixedSize(true);

        filesLayoutManager = new LinearLayoutManager(activity);
        filesRecyclerView.setLayoutManager(filesLayoutManager);

    }

    public void setFilesRecyclerViewContentWithMyFiles() {
        filesAdapter = new FilesAdapter(getFiles(), activity);
        filesRecyclerView.setAdapter(filesAdapter);
    }
    public void setFilesRecyclerViewContentWith(MyFile file) {
        filesAdapter = new FilesAdapter(file, activity);
        filesRecyclerView.setAdapter(filesAdapter);
    }

    private MyFile getFiles() {
        return MyFile.getFileInstanceFromDirectoryDownload();
    }

}
