package com.androidfiletransfer.files;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MyFile {

    private String fileName;
    private String path ;
    private List<MyFile> files;
    private transient boolean isOpen = false;

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
        }
        else {
            files = null;
        }
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

}
