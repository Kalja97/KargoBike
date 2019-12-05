package com.example.kargobike.ui.checkpoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.example.kargobike.adapter.CheckPointAdapter;
import com.example.kargobike.database.entity.Checkpoint;
import com.example.kargobike.R;
import com.example.kargobike.ui.LogActivity;
import com.example.kargobike.ui.SettingsActivity;
import com.example.kargobike.util.RecyclerViewItemClickListener;
import com.example.kargobike.viewmodel.checkpoint.CheckpointListViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CheckpointsActivity extends AppCompatActivity {

    private static final String TAG = "CheckpointsList";
    private CheckpointListViewModel CheckpointListViewModel;

    //Attributs
   // private ListView listview;
    private List<Checkpoint> checkpointList;
   // private CheckpointListViewModel viewModel;
    //private String order;
    private String CheckpointId;
    private CheckPointAdapter<Checkpoint> adapter;

    private Toolbar toolbar;

    //create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Actions für Actionbar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                Intent intentHome = new Intent(this, LogActivity.class);
                startActivity(intentHome);
                return true;

            case R.id.action_settings:


                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);;
        mGoogleSignInClient.signOut();
    }

    //on create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoints);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //change title in toolbar and it's color
        setTitle("KargoBike - Checkpoints");
        toolbar.setTitleTextColor(Color.WHITE);

        //order = getIntent().getStringExtra("OrderNr");
        //CheckpointId = getIntent().getStringExtra("CheckpointId");

        CheckpointListViewModel.Factory factory = new CheckpointListViewModel.Factory(getApplication());
        CheckpointListViewModel = ViewModelProviders.of(this, factory).get(CheckpointListViewModel.class);
        CheckpointListViewModel.getCheckpoints().observe(this, CheckpointEntities -> {
            if(CheckpointEntities != null) {
                checkpointList = CheckpointEntities;
                adapter.setData(checkpointList);
            }
        });

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
                    Log.d(TAG, "Clicked on: "+checkpointList.get(position).getcheckPointID());


                    Intent intent = new Intent(CheckpointsActivity.this, EditCheckpointActivity.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                    );
                    intent.putExtra("CheckpointID", checkpointList.get(position).getId());
                    //intent.putExtra("OrderNr", order);
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View v, int position) {
                    Log.d(TAG, "Clicked position: "+ position);
                    Log.d(TAG, "Clicked on: "+checkpointList.get(position).getcheckPointID());


                    Intent intent = new Intent(CheckpointsActivity.this, EditCheckpointActivity.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                    );
                    intent.putExtra("CheckpointId", checkpointList.get(position).getId());
                    //intent.putExtra("OrderNr", checkpointList.get(position).getcheckPointID());
                    intent.putExtra("checkpointName", checkpointList.get(position).getCheckpointName());
                    startActivity(intent);
                }
            });
            /*
            //Create floatingButton for adding new checkpoints in orders
            FloatingActionButton fab = findViewById(R.id.floatingActionAddCheckPoint);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(CheckpointsActivity.this, AddCheckpointActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra("OrderNr", order);
                startActivity(intent);
            });

*/
            recyclerView.setAdapter(adapter);

        }
    }
}