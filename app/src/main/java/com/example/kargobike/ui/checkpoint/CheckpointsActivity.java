package com.example.kargobike.ui.checkpoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kargobike.adapter.CheckPointAdapter;
import com.example.kargobike.database.entity.Checkpoint;
import com.example.kargobike.R;
import com.example.kargobike.ui.SettingsActivity;
import com.example.kargobike.ui.order.OrdersActivity;
import com.example.kargobike.util.RecyclerViewItemClickListener;
import com.example.kargobike.viewmodel.checkpoint.CheckpointListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CheckpointsActivity extends AppCompatActivity {

    private static final String TAG = "CheckpointsList";
    private CheckpointListViewModel CheckpointListViewModel;

    //Attributs
    private ListView listview;
    private List<Checkpoint> checkpointList;
    private CheckpointListViewModel viewModel;
    private String order;
    private String CheckpointId;
    private CheckPointAdapter<Checkpoint> adapter;

    //create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_checkpoints, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Actions für Actionbar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        setContentView(R.layout.activity_checkpoints);

        setTitle("CheckpointsActivity");

        order = getIntent().getStringExtra("OrderNr");
        CheckpointId = getIntent().getStringExtra("CheckpointId");

        if (CheckpointId == null) {
            //Initialize Database and data
            RecyclerView recyclerView = findViewById(R.id.recyclerviewitem_checkpoints);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //Handle itemClick and start a new activity
            checkpointList = new ArrayList<>();
            adapter = new CheckPointAdapter<>(new RecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Log.d(TAG, "Clicked position: "+ position);
                    Log.d(TAG, "Clicked on: "+checkpointList.get(position).getOrderNr());


                    Intent intent = new Intent(CheckpointsActivity.this, CheckpointsActivity.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                    );

                    intent.putExtra("OrderNr", checkpointList.get(position).getOrderNr());
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View v, int position) {
                    Log.d(TAG, "longClicked position:" + position);
                    Log.d(TAG, "longClicked on: " + checkpointList.get(position).toString());

                    Intent intent = new Intent(CheckpointsActivity.this, CheckpointsActivity.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                    );

                    intent.putExtra("OrderNr", checkpointList.get(position).getOrderNr());
                    startActivity(intent);
                }
            });

            //Create floatingButton for adding new Orders
            FloatingActionButton fab = findViewById(R.id.floatingActionAddCheckPoint);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(CheckpointsActivity.this, AddCheckpointActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            });

            CheckpointListViewModel.Factory factory = new CheckpointListViewModel.Factory(getApplication(), order);
            CheckpointListViewModel = ViewModelProviders.of(this, factory).get(CheckpointListViewModel.class);
            CheckpointListViewModel.getCheckpoints().observe(this, CheckpointEntities -> {
                if(CheckpointEntities != null) {
                    checkpointList = CheckpointEntities;
                    adapter.setData(checkpointList);
                }
            });
            recyclerView.setAdapter(adapter);

        }
    }
}