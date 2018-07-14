package com.androidfiletransfer.files;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import java.io.File;

public class MyFiles {

    private List<MyFile> myFiles;

    public MyFiles() {
        myFiles = new ArrayList<>();
        setMyFiles();
    }

    public List<MyFile> get() {
        return myFiles;
    }

    public MyFile get(int index) {
        return myFiles.get(index);
    }

    private void setMyFiles() {
        File[] files = getFilesFromDirectoryDownload();
        for (File file : files) {
            myFiles.add(new MyFile(file));
        }
    }


    private File[] getFilesFromDirectoryDownload() {

        File downloadFolder = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        return downloadFolder.listFiles();
    }
}
