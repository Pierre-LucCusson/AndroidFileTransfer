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

    public MyFile getFileAt(int position) {
        if (position != 0) {
            int filePosition = 0;
            for (MyFile myFile : myFiles) {
                if (myFile.isOpen()) {
                    if (filePosition + myFile.size() > position) {
                        return myFile.getFileAt(position - filePosition);
                    } else {
                        filePosition += myFile.size();
                    }
                } else {
                    if(filePosition == position) {
                        return myFile;
                    }
                    else {
                        filePosition++;
                    }
                }

            }
        }
        return myFiles.get(0);
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

    public int size() {
        int totalAmountOfFiles = 0;

        for (MyFile myFile : myFiles) {
            totalAmountOfFiles += myFile.size();
        }

        return totalAmountOfFiles;
    }
}
