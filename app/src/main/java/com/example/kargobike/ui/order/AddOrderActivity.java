package com.example.kargobike.ui.order;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.order.OrderViewModel;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

public class AddOrderActivity extends AppCompatActivity {

    private static final String TAG = "AddOrder";

    //Attributes
    private String dateDelivery;
    private String datePickup;
    private String howManyPackages;
    private String product;
    private String receiver;
    private String rider;
    private String sender;
    private String state;
    private int howMany;

    private OrderViewModel viewModel;
    private Toolbar toolbar;

    private DatePickerDialog.OnDateSetListener DateSetListenerDelivery;
    private DatePickerDialog.OnDateSetListener DateSetListenerPickup;


    //on create method

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_orders);


        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String loc = "";

        //initialize viewmodel
        OrderViewModel.Factory factory = new OrderViewModel.Factory(getApplication(), loc);
        viewModel = ViewModelProviders.of(this, factory).get(OrderViewModel.class);

        //change title in toolbar and it's color
        setTitle("Add order");
        toolbar.setTitleTextColor(Color.WHITE);

        //Initialize editText and Spinner
        EditText etHowManyPackages = (EditText) findViewById(R.id.howManyPackages);
        Spinner spProduct = (Spinner) findViewById(R.id.product);
        EditText etReceiver = (EditText) findViewById(R.id.receiver);
        EditText etRider = (EditText) findViewById(R.id.rider);
        EditText etSender = (EditText) findViewById(R.id.sender);
        Spinner spState = (Spinner) findViewById(R.id.state);
        EditText etDateDelivery = (EditText) findViewById(R.id.dateDelivery);
        EditText etDatePickup = (EditText) findViewById(R.id.datePickup);

        etDateDelivery.setFocusable(false);
        etDatePickup.setFocusable(false);

        //OnClickListener für Date Delivery
        etDateDelivery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddOrderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListenerDelivery,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //OnClickListener für Date Pickup
        etDatePickup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddOrderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListenerPickup,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //DateSetListener for Date Delivery
        DateSetListenerDelivery = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                etDateDelivery.setText(date);


            }
        };

        //DateSetListener for Date Pickup
        DateSetListenerPickup = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                etDatePickup.setText(date);


            }
        };

        //Buttons
        Button add = findViewById(R.id.btnaddlocation);
        Button cancel = findViewById(R.id.btncancel);

        //cancel inputs and go back to the mainpage
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancel = new Intent(AddOrderActivity.this, OrdersActivity.class);
                startActivity(intentCancel);
            }
        });

        //Add order to the database
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Get text from inputs
                dateDelivery = etDateDelivery.getText().toString().trim();
                datePickup = etDatePickup.getText().toString().trim();
                howManyPackages = etHowManyPackages.getText().toString().trim();
                receiver = etReceiver.getText().toString().trim();
                sender = etSender.getText().toString().trim();
                rider = etRider.getText().toString().trim();
                state = spState.getSelectedItem().toString();
                product = spProduct.getSelectedItem().toString();

                //Check if all filed are filled in
                if(dateDelivery.isEmpty() || datePickup.isEmpty() || howManyPackages.isEmpty() || receiver.isEmpty() ||
                sender.isEmpty() || rider.isEmpty() || state.isEmpty() || product.isEmpty()){
                    final AlertDialog alertDialog = new AlertDialog.Builder(AddOrderActivity.this).create();
                    alertDialog.setTitle("Not all fields filled in");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Please fill in all fields first");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                    alertDialog.show();
                    return;
                }

                //Check if packages input is a number
                //if not set error
                String regexStr = "^[0-9]*$";

                if(!etHowManyPackages.getText().toString().trim().matches(regexStr))
                {
                    etHowManyPackages.setError(getString(R.string.error_invalid_packages));
                    etHowManyPackages.requestFocus();
                    return;
                }

                //cast String to integer
                howMany = Integer.valueOf(etHowManyPackages.getText().toString());

                //create new location object
                Order order = new Order();
                order.setDateDelivery(dateDelivery);
                order.setDatePickup(datePickup);
                order.setHowMany(howMany);
                order.setReceiver(receiver);
                order.setSender(sender);
                order.setRider(rider);
                order.setState(state);
                order.setProduct(product);

                //add to firebase
                viewModel.createOrder(order, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "create order: success");
                        Intent intent = new Intent(AddOrderActivity.this, OrdersActivity.class);

                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "create order: failure", e);
                        final AlertDialog alertDialog = new AlertDialog.Builder(AddOrderActivity.this).create();
                        alertDialog.setTitle("Can not save");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Cannot add this order");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                        alertDialog.show();
                    }
                });
            }
        });
    }

    //Inflate menu icons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Actions toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                Intent intentHome = new Intent(this, AddOrderActivity.class);
                startActivity(intentHome);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}