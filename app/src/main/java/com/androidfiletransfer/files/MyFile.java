package com.androidfiletransfer.files;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MyFile {

    private String fileName;
    private String path ;
    private List<MyFile> files;
    private transient boolean isOpen = false;

    private MyFile() {
        File[] internalFiles = getFilesFromDirectoryDownload();
        files = new ArrayList<>();
        for (File internalFile : internalFiles) {
            files.add(new MyFile(internalFile));
        }
    }

    public static MyFile getFileInstanceFromDirectoryDownload() {
        return new MyFile();
    }

    public MyFile(String fileName, String path, List<MyFile> files) {
        this.fileName = fileName;
        this.path = path;
        this.files = files;
    }

    public MyFile(File file) {
        fileName = file.getName();
        path = file.getPath();
        if(file.isDirectory()) {
            files = new ArrayList<>();
            for (File innerFile : file.listFiles()) {
                files.add(new MyFile(innerFile));
            }
        }
        else {
            files = null;
        }
    }

    public static MyFile getFilesWith(String filesInJson) {
        return new Gson().fromJson(filesInJson, MyFile.class);
    }

    private File[] getFilesFromDirectoryDownload() {
        File downloadFolder = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        return downloadFolder.listFiles();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<MyFile> getFiles() {
        return files;
    }

    public void setFiles(List<MyFile> files) {
        this.files = files;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void toggleOpen() {
        isOpen = !isOpen;
    }

    public int size() {
        int totalAmountOfFiles = 0;
        if (fileName != null) {
            totalAmountOfFiles++;
        }
        else {
            if (files != null) {
                for (MyFile file : files) {
                    totalAmountOfFiles += file.size();
                }
            }
        }

        if (files != null && isOpen) {
            for (MyFile file : files) {
                totalAmountOfFiles += file.size();
            }
        }

        return totalAmountOfFiles;
    }

    public MyFile getFileAt(int position) {

        if (position != 0) {
            int filePosition = 0;
            if(fileName != null) {
                filePosition++;
            }
            for (MyFile myFile : files) {
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
        else {
            if (fileName == null) {
                return files.get(0);
            }
            else {
                return this;
            }
        }
        return this;
    }

}
