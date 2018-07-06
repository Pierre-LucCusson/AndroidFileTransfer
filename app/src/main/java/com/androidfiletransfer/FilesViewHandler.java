package com.androidfiletransfer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidfiletransfer.Files.FilesAdapter;

public class FilesViewHandler {

    private MainActivity activity;
    private RecyclerView filesRecyclerView;
    private RecyclerView.Adapter filesAdapter;
    private RecyclerView.LayoutManager filesLayoutManager;

    public FilesViewHandler(MainActivity activity) {
        this.activity = activity;
        filesRecyclerView = activity.findViewById(R.id.filesRecyclerView);
        filesRecyclerView.setHasFixedSize(true);

        filesLayoutManager = new LinearLayoutManager(activity);
        filesRecyclerView.setLayoutManager(filesLayoutManager);

    }

    public void setFilesRecyclerViewContent() {
        filesAdapter = new FilesAdapter(getFiles(), activity);
        filesRecyclerView.setAdapter(filesAdapter);
    }

    private String[] getFiles() {
        String[] myFiles = {"My 1st File", "My 2nd File", "My 3rd File"}; //TODO
        return myFiles;
    }

}
