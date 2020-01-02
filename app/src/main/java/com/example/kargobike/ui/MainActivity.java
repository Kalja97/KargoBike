package com.example.kargobike.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.kargobike.R;
import com.example.kargobike.ui.checkpoint.AddCheckpointActivity;
import com.example.kargobike.ui.checkpoint.CheckpointsActivity;
import com.example.kargobike.ui.order.AddOrderActivity;
import com.example.kargobike.ui.order.ExportActivity;
import com.example.kargobike.ui.order.OrdersActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import static com.example.kargobike.R.id.ReportsImageButton;
import static com.example.kargobike.R.id.getAllOrdersImageButton;
import static com.example.kargobike.R.id.checkpointAddImageButton;
import static com.example.kargobike.R.id.checkpointListImageButton;
import static com.example.kargobike.R.id.orderAddImageButton;
import static com.example.kargobike.R.id.orderListImageButton;

/**
 * Author: Samuel Pinto Da Silva
 * Creation date:
 * Last update date: 03.12.2019
 */



public class MainActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private TextView user;
    private String username;

    private SlidrInterface slidr;

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

    //Method for logout
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        ;
        mGoogleSignInClient.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        //DarkTheme
//        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
//            setTheme(R.style.DarkTheme);
//        else
//            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidr = Slidr.attach(this);


        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get username
        username = getIntent().getStringExtra("user_name");

        //Show user in toolbar
        user = (TextView) findViewById(R.id.toolbarTextView);
        user.setText(getIntent().getStringExtra("user_name"));

        //change title in toolbar and it's color
        setTitle("KargoBike");
        toolbar.setTitleTextColor(Color.WHITE);

        //Create imageButton to display the list of orders
        ImageButton oListButton = (ImageButton) findViewById(orderListImageButton);
        oListButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
            intent.putExtra("user_name", username);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });


        //Create imageButton for adding new Orders
        ImageButton oAdd = (ImageButton) findViewById(orderAddImageButton);
        oAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddOrderActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        //Create imageButton for adding new Checkpoints
        ImageButton cAdd = (ImageButton) findViewById(checkpointAddImageButton);
        cAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddCheckpointActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        //Create imageButton to display the list of orders
        ImageButton cListButton = (ImageButton) findViewById(checkpointListImageButton);
        cListButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CheckpointsActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        ImageButton btnDispatcherTools = (ImageButton) findViewById(getAllOrdersImageButton);
        btnDispatcherTools.setOnClickListener(view -> {
            Intent intent = new Intent(this, DispatcherActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        ImageButton btnExportOrders = (ImageButton) findViewById(ReportsImageButton);
        btnExportOrders.setOnClickListener(view -> {
            Intent intent = new Intent(this, ExportActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

    }

    public void unlockSlide(View v) {

        slidr.unlock();

        ImageButton oListButton = (ImageButton) findViewById(orderListImageButton);
        oListButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, OrdersActivity.class);
            intent.putExtra("user_name", username);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        oListButton.performClick();
    }

    public void lockSlide(View v) {
        slidr.lock();

        ImageButton oListButton = (ImageButton) findViewById(orderListImageButton);
        oListButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, OrdersActivity.class);
            intent.putExtra("user_name", username);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        oListButton.performClick();
    }


}


