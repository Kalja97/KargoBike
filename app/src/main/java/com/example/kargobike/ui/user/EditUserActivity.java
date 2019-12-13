package com.example.kargobike.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.User;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.user.UserViewModel;

public class EditUserActivity extends AppCompatActivity {

    private static final String TAG = "EditUser";

    //Edit texts of the layout
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private Switch swDispatcher;

    private String firstName;
    private String lastName;
    private String email;
    private Boolean isDispatcher;

    private UserViewModel viewModel;
    private User user;

    //Button
    private Button btnChange;

    //Toolbar
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        String usr = getIntent().getStringExtra("userId");

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //change title in toolbar and it's color
        setTitle("KargoBike - Users");
        toolbar.setTitleTextColor(Color.WHITE);

        initiateView();

        UserViewModel.Factory factory = new UserViewModel.Factory(getApplication(), usr);

        viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        viewModel.getUser().observe(this, userEntity -> {
            if (userEntity != null) {
                user = userEntity;
                updateContent();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = etFirstName.getText().toString().trim();
                lastName = etLastName.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                isDispatcher = swDispatcher.isChecked();

                //call method for checking if required fields are filled
                if (checkInputField()) {
                    //call method for saving checkpoint
                    saveChanges();
                }
                return;
            }
        });
    }

    //initialize the edit texts
    private void initiateView() {
        etFirstName = findViewById(R.id.change_firstName);
        etLastName = findViewById(R.id.change_lastName);
        etEmail = findViewById(R.id.change_email);
        swDispatcher = findViewById(R.id.change_isDispatcher);
        btnChange = findViewById(R.id.btnChangeUser);
    }

    //update text in the view
    private void updateContent() {
        if (user != null) {
            etFirstName.setText(user.getFirstname());
            etLastName.setText(user.getLastname());
            etEmail.setText(user.getEmail());
            swDispatcher.setChecked(user.getDispatcher());
        }
    }

    //save the changes into the database
    private void saveChanges() {
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setDispatcher(isDispatcher);

        viewModel.updateUser(user, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "updateUser: success");
                Intent intent = new Intent(EditUserActivity.this, UserlistActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateUser: failure", e);
                final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(EditUserActivity.this).create();
                alertDialog.setTitle("Can not save");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Cannot edit this user");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();
            }
        });
    }


    private boolean checkInputField(){
        //Check if all filed are filled in
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
            final AlertDialog alertDialog = new AlertDialog.Builder(EditUserActivity.this).create();
            alertDialog.setTitle("Not all fields filled in");
            alertDialog.setCancelable(true);
            alertDialog.setMessage("Please fill in all fields first");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        //String orderNr = getIntent().getStringExtra("OrderNr");

        menu.add(0, 2, Menu.NONE, getString(R.string.action_delete))
                .setIcon(R.drawable.ic_delete_white)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == 2){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.action_delete));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.message_DELETE_USER));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_delete), (dialog, which) -> {
                deleteUser();
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteUser() {
        user.setActive(false);

        viewModel.updateUser(user, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "deleteUser: success");
                Intent intent = new Intent(EditUserActivity.this, UserlistActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "deleteUser: failure", e);
                final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(EditUserActivity.this).create();
                alertDialog.setTitle("Can not save");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Cannot edit this user");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();
            }
        });
    }
}