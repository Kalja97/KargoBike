package com.example.kargobike.ui.checkpoint;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kargobike.R;

import com.example.kargobike.database.entity.Checkpoint;
import com.example.kargobike.ui.SettingsActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.checkpoint.CheckpointViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

public class EditCheckpointActivity extends AppCompatActivity {

    private static final String TAG = "EditCheckpointActivity";

    //Textviews
    private EditText etCheckpointName;
    private Spinner spType;

    //Button
    Button btnChange;

    //Toolbar
    private Toolbar toolbar;

    //get intent
    String id;
    String orderNr;

    //Strings for saving
    String checkpointName;
    String type;

    private Checkpoint checkpoint;
    private CheckpointViewModel vmCheckpoint;

    //on create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_checkpoint);

        id = getIntent().getStringExtra("CheckpointId");
        orderNr = getIntent().getStringExtra("OrderNr");

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //change title in toolbar and it's color
        setTitle("KargoBike - Checkpoints");
        toolbar.setTitleTextColor(Color.WHITE);

        initiateView();

        CheckpointViewModel.Factory factory = new CheckpointViewModel.Factory(getApplication(), orderNr, id);

        vmCheckpoint = ViewModelProviders.of(this, factory).get(CheckpointViewModel.class);
        vmCheckpoint.getCheckpoint().observe(this, checkpointEntity -> {
            if (checkpointEntity != null) {
                checkpoint = checkpointEntity;
                updateContent();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpointName = etCheckpointName.getText().toString().trim();
                type = spType.getSelectedItem().toString();

                //call method for checking if required fields are filled
                if(checkInputField(type)){
                    //call method for saving checkpoint
                    saveChanges(checkpointName, type);
                }
                return;
            }
        });
    }

    //initialize the edit texts
    private void initiateView() {
        etCheckpointName = findViewById(R.id.change_checkpointname);
        spType = findViewById(R.id.change_type);
        btnChange = findViewById(R.id.btnChangeCheckpoint);
    }

    //update text in the view
    private void updateContent() {
        if (checkpoint != null) {
            etCheckpointName.setText(checkpoint.getCheckpointName());
            spType.setSelection(0);
        }
    }

    //save the changes into the database
    private void saveChanges(String checkpointName, String type) {
        checkpoint.setCheckpointName(checkpointName);
        checkpoint.setType(type);

        vmCheckpoint.updateCheckpoint(checkpoint, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "updateCheckpoint: success");
                Intent intent = new Intent(EditCheckpointActivity.this, CheckpointsActivity.class);
                intent.putExtra("OrderNr", orderNr);
                intent.putExtra("CheckpointId", id);
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateCheckpoint: failure", e);
                final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(EditCheckpointActivity.this).create();
                alertDialog.setTitle("Can not save");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Cannot edit this order");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();
            }
        });
    }

    private boolean checkInputField(String type){
        //Check if all filed are filled in
        if(type.isEmpty()){
            final AlertDialog alertDialog = new AlertDialog.Builder(EditCheckpointActivity.this).create();
            alertDialog.setTitle("Not all fields filled in");
            alertDialog.setCancelable(true);
            alertDialog.setMessage("Please fill in all fields first");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
            return false;
        }
        return true;
    }
}