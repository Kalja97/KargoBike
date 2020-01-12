package com.example.kargobike.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.User;
import com.example.kargobike.ui.checkpoint.AddCheckpointActivity;
import com.example.kargobike.ui.checkpoint.CheckpointsActivity;
import com.example.kargobike.ui.order.AddOrderActivity;
import com.example.kargobike.ui.order.OrdersActivity;
import com.example.kargobike.viewmodel.user.UserListViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import static com.example.kargobike.R.id.checkpointAddImageButton;
import static com.example.kargobike.R.id.checkpointListImageButton;
import static com.example.kargobike.R.id.getAllOrdersImageButton;
import static com.example.kargobike.R.id.orderAddImageButton;
import static com.example.kargobike.R.id.orderListImageButton;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView user;
    private String username;
    private String user_email;
    private Boolean isDispatcher = true;

    private List<User> users;
    private UserListViewModel userListViewModel;

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
        mGoogleSignInClient.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Get data of logged user
        getUser();

        super.onCreate(savedInstanceState);

        // Wait for fetching user data before continue
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    initializeView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);
    }

    private void initializeView() {
        setContentView(R.layout.activity_main);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get user
        username = getIntent().getStringExtra("user_name");
        user_email = getIntent().getStringExtra("user_email");

        //Show user in toolbar
        user = findViewById(R.id.toolbarTextView);
        user.setText(getIntent().getStringExtra("user_name"));

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(user_email)) {
                isDispatcher = users.get(i).getDispatcher();
            }
        }

        //change title in toolbar and it's color
        setTitle("KargoBike");
        toolbar.setTitleTextColor(Color.WHITE);

        //Create imageButton to display the list of orders
        ImageButton oListButton = findViewById(orderListImageButton);
        oListButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
            intent.putExtra("user_name", username);
            intent.putExtra("user_restriction", isDispatcher.toString());
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });


        //Create imageButton for adding new Orders
        ImageButton oAdd = findViewById(orderAddImageButton);
        oAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddOrderActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        //Create imageButton for adding new Checkpoints
        ImageButton cAdd = findViewById(checkpointAddImageButton);
        cAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddCheckpointActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        //Create imageButton to display the list of checkpoints
        ImageButton cListButton = findViewById(checkpointListImageButton);
        cListButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CheckpointsActivity.class);
            intent.putExtra("user_restriction", isDispatcher.toString());
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        ImageButton btnDispatcherTools = findViewById(getAllOrdersImageButton);
        btnDispatcherTools.setOnClickListener(view -> {

            if (isDispatcher) {
                Intent intent = new Intent(this, DispatcherActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            } else {
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("No permission");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("You have no permission to use this function.");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();
                return;
            }
        });
    }

    private void getUser() {
        //get userlist
        users = new ArrayList<>();
        UserListViewModel.Factory factory = new UserListViewModel.Factory(getApplication());
        userListViewModel = ViewModelProviders.of(this, factory).get(UserListViewModel.class);
        userListViewModel.getUsers().observe(this, userEntities -> {
            if (userEntities != null) {
                users = userEntities;
            }
        });
    }
}


