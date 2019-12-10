package com.example.kargobike.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.order.OrderViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class EditOrderActivity extends AppCompatActivity {
/*
    private static final String TAG = "EditOrder";

    //Edit texts of the layout
    private EditText etOrderNr;
    private EditText etDateDelivery;
    private EditText etDatePickup;
    private EditText etHowManyPackages;
    private EditText etReceiver;
    private EditText etRider;
    private EditText etSender;

    //location attributes
    private String orderNr;
    private String dateDelivery;
    private String datePickup;
    private int howManyPackages;
    private String receiver;
    private String rider;
    private String sender;


    private OrderViewModel viewModel;
    private Order order;

    //on create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        String loc = getIntent().getStringExtra("orderNr");

        initiateView();

        //Get data from database
        OrderViewModel.Factory factory = new OrderViewModel.Factory(getApplication(), loc);

        viewModel = ViewModelProviders.of(this, factory).get(OrderViewModel.class);
        viewModel.getOrder().observe(this, orderEntity -> {
            if (orderEntity != null) {
                order = orderEntity;
                updateContent();
            }
        });
    }

    //call method saveChanges for saving changes in the DB
    private void saving(){

        dateDelivery = etDateDelivery.getText().toString().trim();
        datePickup = etDatePickup.getText().toString().trim();
        howManyPackages =  Integer.valueOf(etHowManyPackages.getText().toString());
        receiver = etReceiver.getText().toString().trim();
        sender = etSender.getText().toString().trim();
        rider = etRider.getText().toString().trim();
        orderNr = etOrderNr.getText().toString().trim();


        //call method saveChanges
        saveChanges(orderNr, dateDelivery, datePickup, howManyPackages, receiver,sender,rider);
    }

    //Saving changes into database
    private void saveChanges(String orderNr, String dateDelivery, String datePickup, int howManyPackages, String receiver, String sender,
                             String rider) {

        order.setOrderNr(orderNr);
        order.setDateDelivery(dateDelivery);
        order.setDatePickup(datePickup);
        order.setHowMany(howManyPackages);
        order.setReceiver(receiver);
        order.setCustomer(sender);
        order.setRider(rider);


        viewModel.updateOrder(order, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "updateOrder: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateOrder: failure", e);
            }
        });
    }

    //Delete order
    private void delete(){
        viewModel.deleteOrder(order, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Delete order: success");
                backToMain();
            }

            @Override
            public void onFailure(Exception e) {}
        });
    }

    //to start the intent -> back to main
    public void backToMain(){
        Intent intentOrder = new Intent(this, OrdersActivity.class);
        startActivity(intentOrder);
    }

    //initialize the edit texts
    private void initiateView() {
        etOrderNr = findViewById(R.id.change_orderNr);
        etDateDelivery = findViewById(R.id.change_dateDelivery);
        etDatePickup = findViewById(R.id.change_datePickup);
        etReceiver = findViewById(R.id.change_Receiver);
        etSender = findViewById(R.id.change_sender);
        etRider = findViewById(R.id.change_rider);
        etHowManyPackages = findViewById(R.id.change_howManyPackages);

        etOrderNr.setEnabled(false);
    }



    //update text in the view
    private void updateContent() {
        if (order != null) {
            etOrderNr.setText(order.getOrderNr());
            etDateDelivery.setText(order.getDateDelivery());
            etReceiver.setText(order.getReceiver());
            etSender.setText(order.getCustomer());
            etRider.setText(order.getRider());
            etHowManyPackages.setText(String.valueOf(order.getHowMany()));
        }
    }


 */
}
