package com.example.musica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_songs_text);

        if(!CheckPermission())
        {
            RequestPermission();

        }
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        ProgressBar loadBar = findViewById(R.id.loading);
        while(cursor.moveToNext())
        {

            AudioModel songData = new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if(new File(songData.getPath()).exists())
            songsList.add(songData);
        }
        loadBar.setVisibility(View.INVISIBLE);
        if(songsList.size()==0)
        {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else{

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
        }

    }
    boolean CheckPermission()
    {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED)
            return true;
        else return false;
    }
    void RequestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(MainActivity.this, "Приложению необходимо получить разрешение на доступ к файлам на вашем устройстве. Это можно установить в настройках", Toast.LENGTH_SHORT);

        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
        if(!CheckPermission()) {
            RequestPermission();
        }
    }
}