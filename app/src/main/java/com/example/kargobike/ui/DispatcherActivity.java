package com.example.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kargobike.R;
import com.example.kargobike.ui.order.AllOrdersActivity;
import com.example.kargobike.ui.product.AddProductActivity;
import com.example.kargobike.ui.product.ProductlistActivity;
import com.example.kargobike.ui.user.AddUserActivity;
import com.example.kargobike.ui.user.UserlistActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.kargobike.R.id.addProductImageButton;
import static com.example.kargobike.R.id.addUserImageButton;
import static com.example.kargobike.R.id.editProductImageButton;
import static com.example.kargobike.R.id.editUserImageButton;
import static com.example.kargobike.R.id.getAllOrdersImageButton;

public class DispatcherActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView user;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        //DarkTheme
//        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
//            setTheme(R.style.DarkTheme);
//        else
//            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Show user in toolbar
        user = (TextView)findViewById(R.id.toolbarTextView);
        user.setText(getIntent().getStringExtra("user_name"));

        //change title in toolbar and it's color
        setTitle("KargoBike");
        toolbar.setTitleTextColor(Color.WHITE);

        // Set toolbar clickable to go to the orderLsit quickly
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DispatcherActivity.this, OrdersActivity.class);
                intent.putExtra("user_name", user.getText());
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            }
        });


        //Create imageButton to edit users
        ImageButton btnEditUser = (ImageButton)findViewById(editUserImageButton);
        btnEditUser.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserlistActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });


        //Create imageButton to add users
        ImageButton btnAddUser = (ImageButton)findViewById(addUserImageButton);
        btnAddUser.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddUserActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        //Create imageButton to edit products
        ImageButton btnEditProduct = (ImageButton)findViewById(editProductImageButton);
        btnEditProduct.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProductlistActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        //Create imageButton to to edit products
        ImageButton btnAddProduct = (ImageButton)findViewById(addProductImageButton);
        btnAddProduct.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddProductActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });

        //Create imageButton to to see all orders
        ImageButton btnGetAllOrders = (ImageButton)findViewById(getAllOrdersImageButton);
        btnGetAllOrders.setOnClickListener(view -> {
            Intent intent = new Intent(this, AllOrdersActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            startActivity(intent);
        });
    }
}


