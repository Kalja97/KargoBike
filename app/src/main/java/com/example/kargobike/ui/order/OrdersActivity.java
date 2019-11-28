package com.example.kargobike.ui.order;

import android.content.Intent;
import android.os.Bundle;

import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kargobike.R;
import com.example.kargobike.adapter.OrderAdapter;
import com.example.kargobike.database.entity.OrderF;
import com.example.kargobike.ui.SettingsActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.util.RecyclerViewItemClickListener;
import com.example.kargobike.viewmodel.order.OrderListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;



public class OrdersActivity extends AppCompatActivity {

    private static final String TAG = "OrdersList";

    private OrderListViewModel OrderListViewModel;
    //private OrderMoveViewModel OrderMoveViewModel;

//    private WorkstationEntity workstation ;
//    private WorkstationViewModel workstationViewModel;

    private OrderAdapter<OrderF> adapter ;

    private List<OrderF> Orders;

    private String OrderId, workstationId;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        setTitle("Orders");
        //getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
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

        OrderId = getIntent().getStringExtra("OrderId");
        //workstationId = getIntent().getStringExtra("workstationId");

        //Create the OrdersActivity with all the Orders
        if(OrderId == null){
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

                    intent.putExtra("OrderId", Orders.get(position).getOrderNr());
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

                    intent.putExtra("OrderId", Orders.get(position).getOrderNr());
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

            //display the Order list for moving the workstations if OrderId != 0
        }else{

            RecyclerView recyclerView = findViewById(R.id.recyclerviewitem_orders);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    LinearLayoutManager.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            //Get the Workstation we want to move
//            WorkstationViewModel.Factory wfactory = new WorkstationViewModel.Factory(getApplication(), workstationId, OrderId);
//            workstationViewModel = ViewModelProviders.of(this, wfactory).get(WorkstationViewModel.class);
//            workstationViewModel.getWorkstation().observe(this, workstationEntity -> {
//                if(workstationEntity != null){
//                    workstation = workstationEntity;
//                }
//            });

            //Handle ItemClick and update the workstation
//            Orders = new ArrayList<>();
//            adapter = new OrderAdapter<>(new RecyclerViewItemClickListener() {
//                @Override
//                public void onItemClick(View v, int position) {
//                    Log.d(TAG, "Clicked position: "+ position);
//                    Log.d(TAG, "Clicked on: "+Orders.get(position).getBuilding());
//
//                    workstation.setOrderId(Orders.get(position).getId());
//
//                    //Update the OrderId workstation for moving
//                    workstationViewModel.updateWorkstation(workstation, new OnAsyncEventListener() {
//                        @Override
//                        public void onSuccess() {
//                            Log.d(TAG, "UpdateWorkstation: Success");
//                        }
//
//                        @Override
//                        public void onFailure(Exception e) {
//                            Log.d(TAG, "UpdateWorkstation: Failure", e);
//                        }
//                    });
//
//                    Intent intent = new Intent(OrdersActivity.this, WorkstationsActivity.class);
//                    intent.setFlags(
//                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY
//                    );
//
//                    intent.putExtra("OrderId", Orders.get(position).getId());
//                    intent.putExtra("workstationId", workstationId);
//                    startActivity(intent);
//                }
//
//                @Override
//                public void onItemLongClick(View v, int position) {
//                    Log.d(TAG, "longClicked position:" + position);
//                    Log.d(TAG, "longClicked on: " + Orders.get(position).toString());
//
//                    workstation.setOrderId(Orders.get(position).getId());
//
//                    workstationViewModel.updateWorkstation(workstation, new OnAsyncEventListener() {
//                        @Override
//                        public void onSuccess() {
//                            Log.d(TAG, "UpdateWorkstation: Success");
//                        }
//
//                        @Override
//                        public void onFailure(Exception e) {
//                            Log.d(TAG, "UpdateWorkstation: Failure", e);
//                        }
//                    });
//
//
//                    Intent intent = new Intent(OrdersActivity.this, WorkstationsActivity.class);
//                    intent.setFlags(
//                            Intent.FLAG_ACTIVITY_NO_ANIMATION |
//                                    Intent.FLAG_ACTIVITY_NO_HISTORY
//                    );
//
//                    intent.putExtra("OrderId", Orders.get(position).getOrderNr());
//                    intent.putExtra("workstationId", workstationId);
//                    startActivity(intent);
//
//                }
//            });
//
//            //Display the Order list whithout the Order where the workstation is already in
//            OrderMoveViewModel.Factory factory = new OrderMoveViewModel.Factory(getApplication(), OrderId);
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
