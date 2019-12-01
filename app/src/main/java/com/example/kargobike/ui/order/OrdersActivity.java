package com.example.kargobike.ui.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.telecom.Call;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.*;

import android.util.Log;
import android.view.*;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kargobike.R;
import com.example.kargobike.adapter.OrderAdapter;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.ui.SettingsActivity;
import com.example.kargobike.ui.checkpoint.CheckpointsActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.util.RecyclerViewItemClickListener;
import com.example.kargobike.viewmodel.order.OrderListViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private static final String TAG = "OrdersList";

    private OrderListViewModel OrderListViewModel;
    //private OrderMoveViewModel OrderMoveViewModel;

//    private CheckpointEntity Checkpoint ;
//    private CheckpointViewModel CheckpointViewModel;

    private OrderAdapter<Order> adapter ;

    private List<Order> Orders;

    private String OrderNr, CheckpointId;

    private Toolbar toolbar;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        setTitle("Orders");
        toolbar.setTitleTextColor(Color.WHITE);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_settings:
                Intent MySettings = new Intent(OrdersActivity.this, SettingsActivity.class);
                startActivity(MySettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        //DarkTheme
//        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
//            setTheme(R.style.DarkTheme);
//        else
//            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OrderNr = getIntent().getStringExtra("OrderNr");
        //CheckpointId = getIntent().getStringExtra("CheckpointId");

        //Create the OrdersActivity with all the Orders
        if(OrderNr == null){
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
                    Log.d(TAG, "Clicked position: "+ position);
                    Log.d(TAG, "Clicked on: "+Orders.get(position).getOrderNr());


                    Intent intent = new Intent(OrdersActivity.this, DetailsOrderActivity.class);
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

                    Intent intent = new Intent(OrdersActivity.this, DetailsOrderActivity.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                    );

                    intent.putExtra("OrderNr", Orders.get(position).getOrderNr());
                    startActivity(intent);
                }
            });


            //Create floatingButton for adding new Orders
            FloatingActionButton fab = findViewById(R.id.floatingActionAddOrder);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(OrdersActivity.this, AddOrderActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            });


            //Get the Orders in the database by calling the ViewModel
            OrderListViewModel.Factory factory = new OrderListViewModel.Factory(getApplication());
            OrderListViewModel = ViewModelProviders.of(this, factory).get(OrderListViewModel.class);
            OrderListViewModel.getOrders().observe(this, OrderEntities -> {
                if(OrderEntities != null) {
                    Orders = OrderEntities;
                    adapter.setData(Orders);
                }
            });
            recyclerView.setAdapter(adapter);

            //display the Order list for moving the Checkpoints if OrderNr != 0
        }else{

            RecyclerView recyclerView = findViewById(R.id.recyclerviewitem_orders);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //Get the Checkpoint we want to move
//            CheckpointViewModel.Factory wfactory = new CheckpointViewModel.Factory(getApplication(), CheckpointId, OrderNr);
//            CheckpointViewModel = ViewModelProviders.of(this, wfactory).get(CheckpointViewModel.class);
//            CheckpointViewModel.getCheckpoint().observe(this, CheckpointEntity -> {
//                if(CheckpointEntity != null){
//                    Checkpoint = CheckpointEntity;
//                }
//            });

//            //Handle ItemClick and update the Checkpoint
//            Orders = new ArrayList<>();
//            adapter = new OrderAdapter<>(new RecyclerViewItemClickListener() {
//                @Override
//                public void onItemClick(View v, int position) {
//                    Log.d(TAG, "Clicked position: "+ position);
//                    Log.d(TAG, "Clicked on: "+Orders.get(position).getBuilding());
//
//                    Checkpoint.setOrderNr(Orders.get(position).getId());
//
//                    //Update the OrderNr Checkpoint for moving
//                    CheckpointViewModel.updateCheckpoint(Checkpoint, new OnAsyncEventListener() {
//                        @Override
//                        public void onSuccess() {
//                            Log.d(TAG, "UpdateCheckpoint: Success");
//                        }
//
//                        @Override
//                        public void onFailure(Exception e) {
//                            Log.d(TAG, "UpdateCheckpoint: Failure", e);
//                        }
//                    });
//
//                    Intent intent = new Intent(OrdersActivity.this, CheckpointsActivity.class);
//                    intent.setFlags(
//                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY
//                    );
//
//                    intent.putExtra("OrderNr", Orders.get(position).getId());
//                    intent.putExtra("CheckpointId", CheckpointId);
//                    startActivity(intent);
//                }
//
//                @Override
//                public void onItemLongClick(View v, int position) {
//                    Log.d(TAG, "longClicked position:" + position);
//                    Log.d(TAG, "longClicked on: " + Orders.get(position).toString());
//
//                    Checkpoint.setOrderNr(Orders.get(position).getId());
//
//                    CheckpointViewModel.updateCheckpoint(Checkpoint, new OnAsyncEventListener() {
//                        @Override
//                        public void onSuccess() {
//                            Log.d(TAG, "UpdateCheckpoint: Success");
//                        }
//
//                        @Override
//                        public void onFailure(Exception e) {
//                            Log.d(TAG, "UpdateCheckpoint: Failure", e);
//                        }
//                    });
//
//
//                    Intent intent = new Intent(OrdersActivity.this, CheckpointsActivity.class);
//                    intent.setFlags(
//                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY
//                    );
//
//                    intent.putExtra("OrderNr", Orders.get(position).getOrderNr());
//                    intent.putExtra("CheckpointId", CheckpointId);
//                     startActivity(intent);
//
//                }
//            });
//
            //Display the Order list whithout the Order where the Checkpoint is already in
//            OrderMoveViewModel.Factory factory = new OrderMoveViewModel.Factory(getApplication(), OrderNr);
//            OrderMoveViewModel = ViewModelProviders.of(this, factory).get(OrderMoveViewModel.class);
//            OrderMoveViewModel.getOrderMoveable().observe(this, OrderEntities -> {
//                if(OrderEntities != null) {
//                    Orders = OrderEntities;
//                    adapter.setData(Orders);
//                }
//            });
            recyclerView.setAdapter(adapter);

        }

    }
}
