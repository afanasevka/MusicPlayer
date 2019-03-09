package com.Aprod.SimplePlayer;

import android.app.Application;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.File;

public class SimplePlayerApp extends Application {
    private static SimplePlayerApp mContext;
    private File currentFile;
    private MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        currentFile = Environment.getRootDirectory();
        mp = new MediaPlayer();
    }

    public File getCurrentFile() {
        return this.currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public static SimplePlayerApp getContext() {
        return mContext;
    }
}
