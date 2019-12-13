package com.example.kargobike.ui.order;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import com.example.kargobike.R;
import com.example.kargobike.adapter.ListAdapter;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.database.entity.Product;
import com.example.kargobike.database.entity.User;
import com.example.kargobike.ui.MainActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.order.OrderViewModel;
import com.example.kargobike.viewmodel.product.ProductListViewModel;
import com.example.kargobike.viewmodel.user.UserListViewModel;

import java.util.ArrayList;
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
    private String timeDelivery;
    private String fromAddress;
    private String toAddress;
    private String rider;
    private String customer;
    private String state;
    private String product;
    private String howManyPackages;
    private int howMany;

    private OrderViewModel orderViewModel;

    // Needed for Product choice
    private Spinner spProducts;
    private ProductListViewModel productListViewModel;
    private ListAdapter adapterProductsList;

    // Needed for Rider choice
    private Spinner spRiders;
    private UserListViewModel riderListViewModel;
    private ListAdapter adapterRidersList;

    private Toolbar toolbar;

    private DatePickerDialog.OnDateSetListener DateSetListener;
    private TimePickerDialog.OnTimeSetListener TimeSetListener;

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
        orderViewModel = ViewModelProviders.of(this, factory).get(OrderViewModel.class);

        //change title in toolbar and it's color
        setTitle("KargoBike - Orders");
        toolbar.setTitleTextColor(Color.WHITE);

        //Initialize editTexts
        EditText etHowManyPackages = (EditText) findViewById(R.id.howManyPackages);
        EditText etToAddress = (EditText) findViewById(R.id.toAddress);
        EditText etCustomer = (EditText) findViewById(R.id.customer);
        Spinner spState = (Spinner) findViewById(R.id.state);
        EditText etDateDelivery = (EditText) findViewById(R.id.dateDelivery);
        EditText etTimeDelivery = (EditText) findViewById(R.id.timeDelivery);
        EditText etFromAddress = (EditText) findViewById(R.id.fromAddress);

        //Spinner for Products
        spProducts = (Spinner) findViewById(R.id.productlist);
        adapterProductsList = new ListAdapter<>(AddOrderActivity.this, R.layout.list_row, new ArrayList<>());
        spProducts.setAdapter(adapterProductsList);
        fillProductList();

        //Spinner for Products
        spRiders = (Spinner) findViewById(R.id.ridersList);
        adapterRidersList = new ListAdapter<>(AddOrderActivity.this, R.layout.list_row, new ArrayList<>());
        spRiders.setAdapter(adapterRidersList);
        fillUserList();

        etDateDelivery.setFocusable(false);
        etTimeDelivery.setFocusable(false);

        //OnClickListener for Date
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
                        DateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //OnClickListener for Time
        etTimeDelivery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        AddOrderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        TimeSetListener, hours, minutes, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //DateSetListener for Date
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                etDateDelivery.setText(date);
            }
        };

        //TimeSetListener for Time
        TimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                Log.d(TAG, "onTimeSet: hh:mm :" + hours + ":" + minutes);

                String time = hours + ":" + minutes;
                etTimeDelivery.setText(time);
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
                timeDelivery = etTimeDelivery.getText().toString().trim();
                fromAddress = etFromAddress.getText().toString().trim();
                howManyPackages = etHowManyPackages.getText().toString().trim();
                toAddress = etToAddress.getText().toString().trim();
                customer = etCustomer.getText().toString().trim();
                rider = spRiders.getSelectedItem().toString();
                state = spState.getSelectedItem().toString();
                product = spProducts.getSelectedItem().toString();

                //Check if all filed are filled in
                if(dateDelivery.isEmpty() || timeDelivery.isEmpty() || fromAddress.isEmpty() || howManyPackages.isEmpty() || toAddress.isEmpty() ||
                customer.isEmpty() || rider.isEmpty() || state.isEmpty() || product.isEmpty()){
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
                order.setTimeDelivery(timeDelivery);
                order.setFromAddress(fromAddress);
                order.setHowMany(howMany);
                order.setToAddress(toAddress);
                order.setCustomer(customer);
                order.setRider(rider);
                order.setState(state);
                order.setProduct(product);

                //add to firebase
                orderViewModel.createOrder(order, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "create order: success");
                        Intent intent = new Intent(AddOrderActivity.this, MainActivity.class);

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

    // Fill the Product Dropdown with data from firebase DB
    private void fillProductList() {
        //Receive all product names from DB
        ProductListViewModel.Factory factory = new ProductListViewModel.Factory(
                getApplication());
        productListViewModel = ViewModelProviders.of(this, factory)
                .get(ProductListViewModel.class);

        productListViewModel.getProducts().observe(this, products -> {
            if (products != null) {

                ArrayList<String> productStrings = new ArrayList<String>();
                for (Product p : products
                ) {
                    productStrings.add(p.toString());
                }

                adapterProductsList.updateData(new ArrayList<>(productStrings));
            }
        });
    }

    private void fillUserList() {
        UserListViewModel.Factory factory = new UserListViewModel.Factory(
                getApplication());
        riderListViewModel = ViewModelProviders.of(this, factory)
                .get(UserListViewModel.class);

        riderListViewModel.getUsers().observe(this, riders -> {
            if (riders != null) {

                ArrayList<String> userStrings = new ArrayList<String>();
                for (User rider: riders
                ) {
                    userStrings.add(rider.toString());
                }

                adapterRidersList.updateData(new ArrayList<>(userStrings));
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