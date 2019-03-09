package com.Aprod.SimplePlayer;

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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    MyRecyclerViewAdapter adapter;
    File currentFile;

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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });



        //        String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
        currentFile = Environment.getRootDirectory();


        // set up the RecyclerView
        RecyclerView rv = findViewById(R.id.view_list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, new ArrayList<String>());
        adapter.setClickListener(this);
        rv.setAdapter(adapter);

        updateList();
    }

    private void updateList()
    {
        File[] files = currentFile.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            adapter.addItem(files[i].getName());
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        File[] files = currentFile.listFiles();
        String fileName = adapter.getItem(position);
        for (int i = 0; i < files.length; i++) {

            if (fileName.equals(files[i].getName()))
            {
                currentFile = files[i];
                adapter.clear();
                updateList();
                break;
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
            return true;
        }

        if (id == R.id.action_quit)
            this.finish();

        return super.onOptionsItemSelected(item);
    }
}
