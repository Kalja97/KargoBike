package com.example.kargobike.ui.checkpoint;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.Checkpoint;
import com.example.kargobike.ui.MainActivity;
import com.example.kargobike.ui.order.OrdersActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.checkpoint.CheckpointViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

public class AddCheckpointActivity extends AppCompatActivity {

    private static final String TAG = "AddCheckpointActivity";

    //Attributs
    private EditText etCheckpointName;
    private Spinner spType;
    private TextView tvRider;
    private TextView tvDateTime;
    private TextView tvGps;

    private CheckpointViewModel viewModel;
    private Toolbar toolbar;

    //Intent infos
    private String orderNr;

    //Strings
    private String checkpointName;
    private String rider;
    private String datetime;
    private String type;
    private String gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_checkpoint);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String ord = "";
        String ord2 = " ";
        CheckpointViewModel.Factory factory = new CheckpointViewModel.Factory(getApplication(), ord/* ord2*/);
        viewModel = ViewModelProviders.of(this, factory).get(CheckpointViewModel.class);

        //change title in toolbar and it's color
        setTitle("KargoBike - Checkpoints");
        toolbar.setTitleTextColor(Color.WHITE);

        // Set toolbar clickable to go to the orderLsit quickly
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCheckpointActivity.this, OrdersActivity.class);
                intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            }
        });

        initializeForm();

        //Button add and call mathod save
        Button btnSave = findViewById(R.id.btnaddcheckpoint);

        orderNr = getIntent().getStringExtra("OrderNr");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get text from inputs
                checkpointName = etCheckpointName.getText().toString().trim();
                type = spType.getSelectedItem().toString();
                rider = tvRider.getText().toString().trim();
                datetime = tvDateTime.getText().toString().trim();
                gps = tvGps.getText().toString().trim();

                //call method for checking if required fields are filled
                if(checkInputField(type)){
                    //call method for saving checkpoint
                    saveChanges(checkpointName, type, gps, datetime, rider);
                }
                return;
            }
        });
    }

    private void initializeForm() {
        //Initialize editText for input
        etCheckpointName = findViewById(R.id.checkpointname);
        tvRider = findViewById(R.id.rider);
        tvDateTime = findViewById(R.id.datetime);
        tvDateTime.setText(getCurrentDateTime());
        spType = findViewById(R.id.type);
        tvGps = findViewById(R.id.gps);
    }

    private String getCurrentDateTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy / HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }

    private boolean checkInputField(String type){
        //Check if all filed are filled in
        if(type.isEmpty()){
            final AlertDialog alertDialog = new AlertDialog.Builder(AddCheckpointActivity.this).create();
            alertDialog.setTitle("Not all fields filled in");
            alertDialog.setCancelable(true);
            alertDialog.setMessage("Please fill in all fields first");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
            return false;
        }
        return true;
    }

    //Method for saving
    private void saveChanges(String checkpointname, String type, String gps, String datetime, String rider) {

        if (checkpointName.isEmpty()){
            checkpointName = "";
        }

        //create new checkpoint
        Checkpoint newCheckpoint = new Checkpoint(orderNr, checkpointname, type, gps, datetime, rider);

        viewModel.createCheckpoint(newCheckpoint, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "create checkpoint: success");
                Intent intent = new Intent(AddCheckpointActivity.this, MainActivity.class);
                intent.putExtra("orderNr", orderNr);
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "create order: failure", e);
                final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(AddCheckpointActivity.this).create();
                alertDialog.setTitle("Can not save");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Cannot add this order");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();
            }
        });

    }
}

