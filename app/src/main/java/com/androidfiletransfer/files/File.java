package com.androidfiletransfer.files;

import com.google.gson.Gson;

import java.util.List;

public class File {

    private String fileName;
    private String path ;
    private List<File> files;

    public File(String fileName, String path, List<File> files) {
        this.fileName = fileName;
        this.path = path;
        this.files = files;
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

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
