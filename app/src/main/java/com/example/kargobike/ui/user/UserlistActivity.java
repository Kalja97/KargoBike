package com.example.kargobike.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.User;
import com.example.kargobike.ui.LogActivity;
import com.example.kargobike.ui.order.OrdersActivity;
import com.example.kargobike.viewmodel.user.UserListViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

public class UserlistActivity extends AppCompatActivity {

    // Required Userlist parameters associated to ViewModel
    private List<User> userList;
    private ListView listview;
    private UserListViewModel viewModel;

    private Toolbar toolbar;

    private String currentUsername;

    // Method to list all list items instead of only the first item of the list
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //change title in toolbar and it's color
        setTitle("KargoBike - Users");
        toolbar.setTitleTextColor(Color.WHITE);

        currentUsername = getIntent().getStringExtra("user_name");

        // Set toolbar clickable to go to the orderLsit quickly
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserlistActivity.this, OrdersActivity.class);
                intent.putExtra("user_name", currentUsername);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            }
        });

        // Create List by ViewModel
        listview = findViewById(R.id.listview);

        userList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        UserListViewModel.Factory factory = new UserListViewModel.Factory(getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(UserListViewModel.class);
        viewModel.getUsers().observe(this, userEntities -> {
            if (userEntities != null) {
                userList = userEntities;
                adapter.clear();
                adapter.addAll(userList);
                setListViewHeightBasedOnChildren(listview);
            }
        });
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), EditUserActivity.class);

                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
                );
                intent.putExtra("userId", userList.get(position).getIdNumber());
                startActivity(intent);
            }
        });
    }

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

}
