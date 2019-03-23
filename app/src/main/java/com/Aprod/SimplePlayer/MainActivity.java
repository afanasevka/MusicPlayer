package com.Aprod.SimplePlayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    MyRecyclerViewAdapter adapter;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        3);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimplePlayerApp.getContext().setCurrentFile(Environment.getRootDirectory());
                adapter.clear();
                updateList();
            }
        });

        ImageButton upButton = findViewById(R.id.upFolderButton);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimplePlayerApp.getContext().setCurrentFile(SimplePlayerApp.getContext().getCurrentFile().getParentFile());
                adapter.clear();
                updateList();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "It's ok.", Toast.LENGTH_LONG).show();
                adapter.addItem("HoHoHo "+adapter.getItemCount());
            }
        });

        FloatingActionButton fabPlay = findViewById(R.id.fabPlay);
        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimplePlayerApp.getContext().SwitchPlayPause();
                //Toast.makeText(getApplicationContext(), "It's ok.", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton ib = null;
        ib = findViewById(R.id.previousFileButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimplePlayerApp.getContext().PlayPreviousFile();
            }
        });

        ib = findViewById(R.id.pauseFileButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimplePlayerApp.getContext().Pause();
            }
        });

        ib = findViewById(R.id.playFileButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimplePlayerApp.getContext().Play();
            }
        });

        ib = findViewById(R.id.nextFileButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimplePlayerApp.getContext().PlayNextFile();
            }
        });

        if (savedInstanceState != null)
            Toast.makeText(getApplicationContext(), savedInstanceState.getString("Path"), Toast.LENGTH_LONG).show();
//            currentFile = getFileStreamPath(savedInstanceState.getString("Path"));
//        else
            //currentFile = Environment.getRootDirectory();


        // set up the RecyclerView
        RecyclerView rv = findViewById(R.id.view_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, new ArrayList<String>());
        adapter.setClickListener(this);
        rv.setAdapter(adapter);

        updateList();


//Make sure you update Seekbar on UI thread
        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                SeekBar sb = findViewById(R.id.seekBar);
                sb.setProgress(SimplePlayerApp.getContext().getCurrentFilePosition());
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putString("Path", SimplePlayerApp.getContext().getCurrentFile().getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateList()
    {
        adapter.clear();
        for (String name:SimplePlayerApp.getContext().getFilesList()
             ) {
            adapter.addItem(name);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        File[] files = SimplePlayerApp.getContext().getCurrentFile().listFiles();
        String fileName = adapter.getItem(position);

        if (fileName.endsWith("ogg") || fileName.endsWith("mp3")) {
            try {
                String filePath = "";
                for (int i = 0; i < files.length; i++) {

                    if (fileName.equals(files[i].getName())) {
                        filePath = files[i].getCanonicalPath();
                        break;
                    }
                }
                SimplePlayerApp.getContext().PlayFile(filePath);
                int lenght = SimplePlayerApp.getContext().getFileLenght();
                int min = lenght/60;
                int sec = lenght%60;
                TextView tv = findViewById(R.id.textSongLenght);
                tv.setText(min + ":" + String.format("%02d", sec));
                SeekBar sb = findViewById(R.id.seekBar);
                sb.setMax(lenght);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            for (int i = 0; i < files.length; i++) {

                if (fileName.equals(files[i].getName())) {
                        SimplePlayerApp.getContext().setCurrentFile(files[i]);
//                    currentFile = files[i];
//                        adapter.clear();
                        updateList();
                    break;
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_quit)
            this.finish();

        return super.onOptionsItemSelected(item);
    }
}
