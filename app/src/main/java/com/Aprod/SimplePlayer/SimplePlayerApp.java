package com.Aprod.SimplePlayer;

import android.app.Application;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class SimplePlayerApp extends Application {
    private static SimplePlayerApp mContext;
    private File currentFile;
    private MediaPlayer mp;

    public static SimplePlayerApp getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        currentFile = Environment.getRootDirectory();

        mp = new MediaPlayer();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                PlayNextFile();
            }
        });
    }

    public ArrayList<String> getFilesList()
    {
        ArrayList<String> list = new ArrayList<>();

        File[] files = SimplePlayerApp.getContext().getCurrentFile().listFiles();
        for (int i = 0; i < files.length; i++)
        {
            list.add(files[i].getName());
        }
        Collections.sort(list);
        return list;
    }

    public File getCurrentFile() {
        return this.currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public int getFileLenght()
    {
        return mp.getDuration()/1000;
    }

    public int getCurrentFilePosition()
    {
        return mp.getCurrentPosition()/1000;
    }

    public void PlayFile(String filePath)
    {
        try {
            mp.reset();
            mp.setDataSource(filePath);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SwitchPlayPause()
    {
        if (mp == null)
            return;
        if (mp.isPlaying())
            mp.pause();
        else
            mp.start();
    }

    public void PlayPreviousFile()
    {}

    public void Pause()
    {
        mp.pause();
    }

    public void Play()
    {
        mp.start();
    }

    public void PlayNextFile()
    {}


}
