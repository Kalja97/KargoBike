package com.example.kargobike.ui.checkpoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kargobike.Entities.Checkpoint;
import com.example.kargobike.R;
import com.example.kargobike.ui.SettingsActivity;
import com.example.kargobike.viewmodel.checkpoint.CheckpointListViewModel;

import java.util.ArrayList;
import java.util.List;

public class CheckpointsActivity extends AppCompatActivity {

    //Attributs
    private ListView listview;
    private List<Checkpoint> checkpointList;
    private CheckpointListViewModel viewModel;
    private String order;

    //create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_checkpoints, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Actions für Actionbar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                Intent intentHome = new Intent(this, AddCheckpointActivity.class);
                intentHome.putExtra("order", order);
                startActivity(intentHome);
                return true;

            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //on create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setTitle("CheckpointsActivity");

        order = getIntent().getStringExtra("order");

        //Create listview
        listview = findViewById(R.id.listview);
        checkpointList = new ArrayList<>();

        //Array adapter
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        CheckpointListViewModel.Factory factory = new CheckpointListViewModel.Factory(getApplication(), order);

        //get data from database
        viewModel = ViewModelProviders.of(this, factory).get(CheckpointListViewModel.class);
        viewModel.getCheckpoints().observe(this, checkpointEntities -> {
            if (checkpointEntities != null) {
                checkpointList = checkpointEntities;
                adapter.clear();
                adapter.addAll(checkpointList);
            }
        });

        listview.setAdapter(adapter);

        //onclicklistener for listview
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), DetailsCheckpointActivity.class);
/*
                intent.putExtra("checkpointName", checkpointList.get(position).getCheckpointname());
                intent.putExtra("order", checkpointList.get(position).getCountryName());

 */
                startActivity(intent);
            }
        });
    }
}
