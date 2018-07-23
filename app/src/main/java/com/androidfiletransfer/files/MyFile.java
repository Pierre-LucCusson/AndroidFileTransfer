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
    private boolean isDirectory = false;
    private transient boolean isOpen = false;

    private MyFile() {
        File[] internalFiles = getFilesFromDirectoryDownload();
        files = new ArrayList<>();
        for (File internalFile : internalFiles) {
            files.add(new MyFile(internalFile));
        }
    }

    private MyFile(MyFile[] myFiles) {
        files = new ArrayList<>();
        for (MyFile internalFile : myFiles) {
            files.add(internalFile);
        }
    }

    public static MyFile getFileInstanceFromDirectoryDownload() {
        return new MyFile();
    }

    public MyFile(File file) {
        fileName = file.getName();
        path = file.getPath();
        if(file.isDirectory()) {
            isDirectory = true;
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
        MyFile[] myFile =  new Gson().fromJson(filesInJson, MyFile[].class);
        return new MyFile(myFile);
    }

    private File[] getFilesFromDirectoryDownload() {
        File downloadFolder = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        return downloadFolder.listFiles();
    }

    public void delete()
    {
        if(isDirectory == false)
        {
            File file = new File(path);
            file.delete();
        }
    }

    public void deleteAtPosition(int position)
    {
        MyFile myFile = files.remove(position);

        if(myFile.isDirectory == false)
        {
            File file = new File(myFile.getPath());
            file.delete();
        }
    }

    public String toJson() {
        return new Gson().toJson(files);
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    public List<MyFile> getFiles() {
        return files;
    }

    public void setFiles(List<MyFile> files) {
        this.files = files;
    }

    public boolean isDirectory() {
        return isDirectory;
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

        if (isDirectory && isOpen) {
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
                if (myFile.isDirectory && myFile.isOpen()) {
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
