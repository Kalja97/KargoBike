package com.example.kargobike.ui.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.User;
import com.example.kargobike.ui.MainActivity;
import com.example.kargobike.ui.order.OrdersActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.user.UserViewModel;

public class AddUserActivity extends AppCompatActivity {

    private static final String TAG = "AddUser";

    //Attributes
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isDispatcher;

    private UserViewModel viewModel;
    private Toolbar toolbar;

    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    Switch swDispatcher;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String usr = "";

        //initialize viewmodel
        UserViewModel.Factory factory = new UserViewModel.Factory(getApplication(), usr);
        viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);

        //change title in toolbar and it's color
        setTitle("KargoBike - Users");
        toolbar.setTitleTextColor(Color.WHITE);

        // Set toolbar clickable to go to the orderLsit quickly
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUserActivity.this, OrdersActivity.class);
                intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            }
        });


        etFirstName = (EditText) findViewById(R.id.firstName);
        etLastName = (EditText) findViewById(R.id.lastName);
        etEmail = (EditText) findViewById(R.id.email);
        swDispatcher = (Switch) findViewById(R.id.switchDispatcher);

        //Buttons
        btnAdd = findViewById(R.id.btnadduser);

        //Add user to the database
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                firstName = etFirstName.getText().toString().trim();
                lastName = etLastName.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                isDispatcher = swDispatcher.isChecked();

                //Check if all filed are filled in
                if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
                    final AlertDialog alertDialog = new AlertDialog.Builder(AddUserActivity.this).create();
                    alertDialog.setTitle("Not all fields filled in");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Please fill in all fields first");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                    alertDialog.show();
                    return;
                }

                //create new location object
                User user = new User();
                user.setFirstname(firstName);
                user.setLastname(lastName);
                user.setEmail(email);
                user.setDispatcher(isDispatcher);

                //add to firebase
                viewModel.createUser(user, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "create user: success");
                        Intent intent = new Intent(AddUserActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "create user: failure", e);
                        final AlertDialog alertDialog = new AlertDialog.Builder(AddUserActivity.this).create();
                        alertDialog.setTitle("Can not save");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Cannot add this user");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                        alertDialog.show();
                    }
                });
            }
        });
    }
}