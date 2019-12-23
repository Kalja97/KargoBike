package com.example.kargobike.ui.checkpoint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kargobike.adapter.CheckPointAdapter;
import com.example.kargobike.adapter.OrderAdapter;
import com.example.kargobike.database.entity.Checkpoint;
import com.example.kargobike.R;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.database.repository.CheckpointRepository;
import com.example.kargobike.database.repository.OrderRepository;
import com.example.kargobike.firebase.OrderLiveData;
import com.example.kargobike.ui.LogActivity;
import com.example.kargobike.ui.SettingsActivity;
import com.example.kargobike.ui.order.DetailsOrderActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.util.RecyclerViewItemClickListener;
import com.example.kargobike.viewmodel.checkpoint.CheckpointListViewModel;
import com.example.kargobike.viewmodel.order.OrderViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CheckpointsActivity extends AppCompatActivity {

    private static final String TAG = "CheckpointsList";
    private CheckpointListViewModel CheckpointListViewModel;
    private CheckpointListViewModel CheckpointListByOrder;

    //Attributs
    // private ListView listview;
    private List<Checkpoint> checkpointList;
    private List<Checkpoint> checkpointListOfOrder;
    // private CheckpointListViewModel viewModel;

    private String orderNr;
    private Order order;
    private String CheckpointId;
    private CheckPointAdapter<Checkpoint> adapter;

    // for selection dialog
    private List<Checkpoint> checkpointListForSelect;
    private CheckpointListViewModel CheckpointListViewModelForSelect;
    private CheckPointAdapter<Checkpoint> adapterForSelect;
    private ArrayList<String> selectedCP = new ArrayList<>();

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

        orderNr = getIntent().getStringExtra("OrderNr");

        // For dialog --> list creation
        if(orderNr != null)
        {
            order = (Order)getIntent().getSerializableExtra("Order");

            CheckpointListViewModel.Factory factory2 = new CheckpointListViewModel.Factory(getApplication(),null);
            CheckpointListViewModelForSelect = ViewModelProviders.of(this, factory2).get(CheckpointListViewModel.class);
            checkpointListForSelect = new ArrayList<>();
            CheckpointListViewModelForSelect.getCheckpoints().observe(this, CheckpointEntities -> {
                if(CheckpointEntities != null)
                {
                    checkpointListForSelect = CheckpointListViewModelForSelect.getCheckpoints().getValue();
                }
            });


            CheckpointListViewModel.Factory factoryByOrder = new CheckpointListViewModel.Factory(getApplication(),orderNr);
            CheckpointListByOrder = ViewModelProviders.of(this, factoryByOrder).get(CheckpointListViewModel.class);
            CheckpointListByOrder.getCheckpointsByOrder().observe(this, CheckpointEntities -> {
                if(CheckpointEntities != null)
                {
                    checkpointList = CheckpointEntities;
                    //adapter.setData((ArrayList)CheckpointListViewModelForSelect.getCheckpointsByOrder().getValue());
                    adapter.setData(checkpointList);
                }
            });
        }

        // For all checkPoints (Global)
        else
        {
            CheckpointListViewModel.Factory factory = new CheckpointListViewModel.Factory(getApplication(),orderNr);
            CheckpointListViewModel = ViewModelProviders.of(this, factory).get(CheckpointListViewModel.class);
            CheckpointListViewModel.getCheckpoints().observe(this, CheckpointEntities -> {
                if(CheckpointEntities != null)
                {
                    checkpointList = CheckpointEntities;
                    adapter.setData(checkpointList);
                }
            });
        }


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
                    Log.d(TAG, "Clicked position: " + position);
                    Log.d(TAG, "Clicked on: " + checkpointList.get(position).getcheckPointID());


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
                    Log.d(TAG, "Clicked position: " + position);
                    Log.d(TAG, "Clicked on: " + checkpointList.get(position).getcheckPointID());


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

            //Create floatingButton for adding new checkpoints in orders
            FloatingActionButton fab = findViewById(R.id.floatingActionAddCheckPoint);
            fab.setOnClickListener(new View.OnClickListener() {

                // based on the code of AndiGeeky from https://stackoverflow.com/questions/32323605/how-do-i-control-on-multichoice-alertdialog/32324244#32324244
                @Override
                public void onClick(View v) {
                    // Build an AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckpointsActivity.this);


                    // Tab with the names checkpoints names
                    String[] cpNamesTab = new String[checkpointListForSelect.size()];

                    for (int i = 0; i < cpNamesTab.length; i++) {
                        cpNamesTab[i] = checkpointListForSelect.get(i).getCheckpointName();
                    }

                    // Tab with the names checked checkpoints
                    final boolean[] checkedCP = new boolean[checkpointListForSelect.size()];

                    for (int i = 0; i < checkedCP.length; i++) {
                        checkedCP[i] = false;
                    }

                    // Convert the checkpoints array to list
                    ArrayList<String> cpList = new ArrayList<>();

                    for(int i = 0; i< cpNamesTab.length; i++)
                    {
                        cpList.add(cpNamesTab[i]);
                    }
                    // Convert the color array to list
                    // final List<String> colorsList = Arrays.asList(colors);

                    // Set multiple choice items for alert dialog
                /*
                    AlertDialog.Builder setMultiChoiceItems(CharSequence[] items, boolean[]
                    checkedItems, DialogInterface.OnMultiChoiceClickListener listener)
                        Set a list of items to be displayed in the dialog as the content,
                        you will be notified of the selected item via the supplied listener.
                 */
                /*
                    DialogInterface.OnMultiChoiceClickListener
                    public abstract void onClick (DialogInterface dialog, int which, boolean isChecked)

                        This method will be invoked when an item in the dialog is clicked.

                        Parameters
                        dialog The dialog where the selection was made.
                        which The position of the item in the list that was clicked.
                        isChecked True if the click checked the item, else false.
                 */
                    builder.setMultiChoiceItems(cpNamesTab, checkedCP, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                            // Update the current focused item's checked status
                            checkedCP[which] = isChecked;

                            // Get the current focused item
                            String currentItem = cpList.get(which);

                            // Notify the current action
                           /* Toast.makeText(getApplicationContext(),
                                    currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();*/
                        }
                    });

                    // Specify the dialog is not cancelable
                    builder.setCancelable(false);

                    // Set a title for alert dialog
                    builder.setTitle("Checkpoints selector");


                    // Set the positive/yes button click listener
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when click positive button
                            //tv.setText("Your preferred colors..... \n");
                            for (int i = 0; i < checkedCP.length; i++) {
                                boolean checked = checkedCP[i];
                                if (checked) {
                                    selectedCP.add(checkpointListForSelect.get(i).getcheckPointID());
                                }
                            }

                            order.setCheckpointsID(selectedCP);
                            System.out.println("Test: " + selectedCP.get(0));

                            // creation of the orderViewModel --> to be able to update the DB
                            OrderViewModel.Factory factoryO = new OrderViewModel.Factory(getApplication(), orderNr);
                            OrderViewModel oVM ;
                            oVM = ViewModelProviders.of(CheckpointsActivity.this, factoryO).get(OrderViewModel.class);
                            oVM.updateOrder(order, new OnAsyncEventListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "updateOrder: Success");
                                    //onBackPressed();
                                    Toast.makeText(CheckpointsActivity.this,R.string.action_add_checkpointsInOrder,Toast.LENGTH_LONG);
                                    finish();
                                    startActivity(getIntent());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.d(TAG, "updateOrder: Failure", e);
                                    Toast.makeText(CheckpointsActivity.this,R.string.action_error_add_checkpointsInOrder,Toast.LENGTH_LONG);
                                    onBackPressed();
                                }
                            });
                        }
                    }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when click the neutral button
                        }
                    });


                    // Set the neutral/cancel button click listener

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
                }
            });

            recyclerView.setAdapter(adapter);

        }
    }

}