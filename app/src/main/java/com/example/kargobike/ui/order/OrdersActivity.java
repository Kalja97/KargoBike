package com.example.kargobike.ui.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kargobike.database.entity.OrderF;
import com.example.kargobike.R;
import com.example.kargobike.viewmodel.order.OrderListViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    //Attributes
    private String orderNr;
    private ListView listview;
    private List<OrderF> orderList;
    private OrderListViewModel viewModel;

    //on create Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        setTitle("Orders");

        orderNr = getIntent().getStringExtra("orderNr");


        //Create the listview
        listview = findViewById(R.id.recyclerviewitem_order);
        orderList = new ArrayList<>();

        //Array adapter
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        OrderListViewModel.Factory factory = new OrderListViewModel.Factory(getApplication());

        //get data from database
        viewModel = ViewModelProviders.of(this, factory).get(OrderListViewModel.class);
        viewModel.getOrders().observe(this, orderEntities -> {
            if (orderEntities != null) {
                orderList = orderEntities;
                adapter.clear();
                adapter.addAll(orderList);
            }
        });

        //adapter to the listview
        listview.setAdapter(adapter);
    }
}
