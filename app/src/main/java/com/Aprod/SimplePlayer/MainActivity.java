package com.Aprod.SimplePlayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    MyRecyclerViewAdapter adapter;
//    File currentFile;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                if (mp == null)
                    return;
                if (mp.isPlaying())
                    mp.pause();
                else
                    mp.start();
                //Toast.makeText(getApplicationContext(), "It's ok.", Toast.LENGTH_LONG).show();
            }
        });


//        if (savedInstanceState != null)
//            currentFile = getFileStreamPath(savedInstanceState.getString("Path"));
//        else
            //currentFile = Environment.getRootDirectory();
        mp = new MediaPlayer();



        // set up the RecyclerView
        RecyclerView rv = findViewById(R.id.view_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, new ArrayList<String>());
        adapter.setClickListener(this);
        rv.setAdapter(adapter);

        updateList();
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
        File[] files = SimplePlayerApp.getContext().getCurrentFile().listFiles();

        for (int i = 0; i < files.length; i++)
        {
            adapter.addItem(files[i].getName());
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
                mp.reset();

                mp.setDataSource(filePath);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            for (int i = 0; i < files.length; i++) {

                if (fileName.equals(files[i].getName())) {
                    SimplePlayerApp.getContext().setCurrentFile(files[i]);
//                    currentFile = files[i];
                    adapter.clear();
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
