package com.androidfiletransfer.files;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import java.io.File;

public class Files {

    private List<com.androidfiletransfer.files.File> files;

    public Files() {
        files = new ArrayList<>();
        setMyFiles();
    }

    public List<com.androidfiletransfer.files.File> get() {
        return files;
    }

    public com.androidfiletransfer.files.File get(int index) {
        return files.get(index);
    }

    private void setMyFiles() {
        File[] myFiles = getFilesFromDirectoryDownload();
        for (File myFile : myFiles) {
            files.add(new com.androidfiletransfer.files.File(myFile.getName(), myFile.getPath(), null));
        }
    }


    private File[] getFilesFromDirectoryDownload() {

        File downloadFolder = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        return downloadFolder.listFiles();
    }
}
