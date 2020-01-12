package com.example.kargobike.ui.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kargobike.R;
import com.example.kargobike.adapter.OrderAdapter;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.ui.LogActivity;
import com.example.kargobike.util.RecyclerViewItemClickListener;
import com.example.kargobike.viewmodel.order.OrderListViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllOrdersActivity extends AppCompatActivity {

    private static final String TAG = "AllOrdersActitivy";

    private OrderListViewModel orderListViewModel;
    private OrderAdapter<Order> adapter;
    private List<Order> Orders;

    private String OrderNr;

    private Toolbar toolbar;
    private TextView user;

    private String username;


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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Show user in toolbar
        user = findViewById(R.id.toolbarTextView);
        user.setText(getIntent().getStringExtra("user_name"));

        //change title in toolbar and it's color
        setTitle("KargoBike - All Orders");
        toolbar.setTitleTextColor(Color.WHITE);

        // Set toolbar clickable to go to the orderLsit quickly
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllOrdersActivity.this, OrdersActivity.class);
                intent.putExtra("user_name", username);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            }
        });

        username = getIntent().getStringExtra("user_name");
        OrderNr = getIntent().getStringExtra("OrderNr");

        //Create the OrdersActivity with all the Orders
        if (OrderNr == null) {
            //Initialize Database and data
            RecyclerView recyclerView = findViewById(R.id.recyclerviewitem_orders);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //Handle itemClick and start a new activity
            Orders = new ArrayList<>();
            adapter = new OrderAdapter<>(new RecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Log.d(TAG, "Clicked position: " + position);
                    Log.d(TAG, "Clicked on: " + Orders.get(position).getOrderNr());


                    Intent intent = new Intent(AllOrdersActivity.this, DetailsOrderActivity.class);
                    intent.putExtra("user_restriction", "true");
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                    );
                    intent.putExtra("OrderNr", Orders.get(position).getOrderNr());
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View v, int position) {
                    Log.d(TAG, "longClicked position:" + position);
                    Log.d(TAG, "longClicked on: " + Orders.get(position).toString());

                    Intent intent = new Intent(AllOrdersActivity.this, DetailsOrderActivity.class);
                    intent.putExtra("user_restriction", "true");
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                    );

                    intent.putExtra("OrderNr", Orders.get(position).getOrderNr());
                    startActivity(intent);
                }
            });

            //Get the Orders in the database by calling the ViewModel
            OrderListViewModel.Factory factory = new OrderListViewModel.Factory(getApplication());
            orderListViewModel = ViewModelProviders.of(this, factory).get(OrderListViewModel.class);
            orderListViewModel.getOrders().observe(this, OrderEntities -> {
                if (OrderEntities != null) {
                    Orders = OrderEntities;
                    adapter.setData(Orders);
                }
            });
            recyclerView.setAdapter(adapter);

            //display the Order list for moving the Checkpoints if OrderNr != 0
        } else {

            RecyclerView recyclerView = findViewById(R.id.recyclerviewitem_orders);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            recyclerView.setAdapter(adapter);
        }
    }
}
