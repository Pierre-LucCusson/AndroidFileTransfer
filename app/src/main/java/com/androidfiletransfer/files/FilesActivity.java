package com.androidfiletransfer.files;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androidfiletransfer.R;

public class FilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String contactsFilesInJson = getIntent().getStringExtra("EXTRA_FILES");
        MyFile contactsFiles = MyFile.getFilesWith(contactsFilesInJson);
//        MyFile contactsFiles = MyFile.getFileInstanceFromDirectoryDownload(); //For debugging/testing

        FilesViewHandler filesView = new FilesViewHandler(this);
        filesView.setFilesRecyclerViewContentWith(contactsFiles);
    }

}
