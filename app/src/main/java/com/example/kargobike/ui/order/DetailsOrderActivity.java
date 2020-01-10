package com.example.kargobike.ui.order;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.kargobike.R;
import com.example.kargobike.adapter.ListAdapter;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.database.entity.Product;
import com.example.kargobike.database.entity.User;
import com.example.kargobike.ui.LogActivity;
import com.example.kargobike.ui.SettingsActivity;
import com.example.kargobike.ui.checkpoint.CheckpointsActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.product.ProductListViewModel;
import com.example.kargobike.viewmodel.user.UserListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.kargobike.viewmodel.order.OrderViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailsOrderActivity extends AppCompatActivity {

    private static final String TAG = "orderDetails";

    private static final int CREATE_ORDER = 0 ;
    private static final int EDIT_ORDER = 1 ;
    private static final int DELETE_ORDER = 2 ;

    private OrderViewModel viewModel ;

    private Toast toast ;

    //private TextView tvOrderNr;
    private EditText etDateDeliv;
    private EditText etTimeDelivery;
    private EditText etFromAddress;
    private EditText etToAddress;
    private Spinner spRider;
    private EditText etCustomer;
    private Spinner spStatus;
    private Spinner spProduct;
    private EditText etProductQty;

    private boolean isEditable;

    private Order order ;

    private String user_restriction;

    FloatingActionButton fab ;
    private Toolbar toolbar;

    //NEW
    private DatePickerDialog.OnDateSetListener DateSetListener;
    private TimePickerDialog.OnTimeSetListener TimeSetListener;

    // Needed for Productlist
    private ProductListViewModel productListViewModel;
    private ListAdapter adapterProductsList;

    // Needed for Riders List
    private UserListViewModel ridersListViewModel;
    private ListAdapter adapterRidersList;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_orders);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String orderNr = getIntent().getStringExtra("OrderNr");
        user_restriction = getIntent().getStringExtra("user_restriction");

        OrderViewModel.Factory factory = new OrderViewModel.Factory(getApplication(), orderNr);
        viewModel = ViewModelProviders.of(this, factory).get(OrderViewModel.class);
        viewModel.getOrder().observe(this, Order -> {
            if(Order != null){
                order = Order ;
                updateContent();
            }
        });

        initiateView();

        fab = findViewById(R.id.checkpointsButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(DetailsOrderActivity.this, CheckpointsActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION
            );
            intent.putExtra("OrderNr", orderNr);
            intent.putExtra("Order", order);
            startActivity(intent);
        });

        if(orderNr != null){
            setTitle(R.string.title_order_details);
        }else {
            setTitle(R.string.title_order_create);
            switchToEdit();
        }

        //NEW

        //OnClickListener für Date Delivery
        etDateDeliv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DetailsOrderActivity.this,
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
                        DetailsOrderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        TimeSetListener, hours, minutes, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //DateSetListener for Date Delivery
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                etDateDeliv.setText(date);


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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        //String orderNr = getIntent().getStringExtra("OrderNr");

        setTitle("KargoBike - Orders");
        toolbar.setTitleTextColor(Color.WHITE);

        // Set toolbar clickable to go to the orderLsit quickly
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsOrderActivity.this, OrdersActivity.class);
                intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            }
        });

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        if(order != null){
            menu.add(0, EDIT_ORDER, Menu.NONE, getString(R.string.action_edit))
                    .setIcon(R.drawable.ic_edit_white)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            if (user_restriction.equals("true")) {

                menu.add(0, DELETE_ORDER, Menu.NONE, getString(R.string.action_delete))
                        .setIcon(R.drawable.ic_delete_white)
                        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }else{
            menu.add(0, CREATE_ORDER, Menu.NONE, getString(R.string.action_create_order))
                    .setIcon(R.drawable.ic_add_white)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == EDIT_ORDER){
            if(isEditable){
                item.setIcon(R.drawable.ic_edit_white);
                switchToEdit();
            }else{
                item.setIcon(R.drawable.ic_done);
                switchToEdit();
            }
        }
        if(item.getItemId() == DELETE_ORDER){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.action_delete));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.message_DELETE_ORDER));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_delete), (dialog, which) -> {
                viewModel.deleteOrder(order, new OnAsyncEventListener(){

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "deleteClient: Success");
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "deleteClient: Failure", e);
                    }
                });
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        }
        if(item.getItemId() == CREATE_ORDER){
/*
            if(etFloor.getText().toString().isEmpty()){
                createOrder(etName.getText().toString(),
                        1000000000,
                        etSector.getText().toString(),
                        etCity.getText().toString(),
                        etCountry.getText().toString());
            }else{
                createOrder(etName.getText().toString(),
                        Integer.parseInt(etFloor.getText().toString()),
                        etSector.getText().toString(),
                        etCity.getText().toString(),
                        etCountry.getText().toString());
            }*/
        }

        if(item.getItemId() == R.id.action_logout){
            Intent intentHome = new Intent(this, LogActivity.class);
            startActivity(intentHome);
        }

        if(item.getItemId() == R.id.action_logout){
            Intent intentSettings = new Intent(this, SettingsActivity.class);
            startActivity(intentSettings);
        }

            return super.onOptionsItemSelected(item);
    }

    private void initiateView(){
        isEditable = false;

        //tvOrderNr = findViewById(R.id.orderNumber);
        etCustomer = findViewById(R.id.etCustomer);
        spProduct = findViewById(R.id.spProduct);
        etProductQty = findViewById(R.id.ethowManyPackages);
        etTimeDelivery = findViewById(R.id.etTimeDelivery);
        etDateDeliv = findViewById(R.id.etDateDelivery);
        spStatus = findViewById(R.id.spState);
        spRider = findViewById(R.id.spRider);
        etFromAddress = findViewById(R.id.etFromAddress);
        etToAddress = findViewById(R.id.etToAddress);


        adapterProductsList = new ListAdapter<>(DetailsOrderActivity.this, R.layout.list_row, new ArrayList<>());
        spProduct.setAdapter(adapterProductsList);
        setSelectedProduct();

        adapterRidersList = new ListAdapter<>(DetailsOrderActivity.this, R.layout.list_row, new ArrayList<>());
        spRider.setAdapter(adapterRidersList);
        setSelectedRider();

        etCustomer.setFocusable(false);
        etCustomer.setEnabled(false);
        spProduct.setFocusable(false);
        spProduct.setEnabled(false);
        etProductQty.setFocusable(false);
        etProductQty.setEnabled(false);
        etTimeDelivery.setFocusable(false);
        etTimeDelivery.setEnabled(false);
        etDateDeliv.setFocusable(false);
        etDateDeliv.setEnabled(false);
        spStatus.setFocusable(false);
        spStatus.setEnabled(false);
        spRider.setFocusable(false);
        spRider.setEnabled(false);
        etFromAddress.setFocusable(false);
        etFromAddress.setEnabled(false);
        etToAddress.setFocusable(false);
        etToAddress.setEnabled(false);
    }

    private void setSelectedProduct() {
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

                int selectedIndex = 0;

                for (int i = 0; i < productStrings.size(); i++) {
                    if (productStrings.get(i).equals(order.getProduct())){
                        selectedIndex = i;
                        break;
                    }
                }

                adapterProductsList.updateData(new ArrayList<>(productStrings));
                spProduct.setSelection(selectedIndex);
            }
        });
    }

    private void setSelectedRider() {
        UserListViewModel.Factory factory = new UserListViewModel.Factory(
                getApplication());
        ridersListViewModel = ViewModelProviders.of(this, factory)
                .get(UserListViewModel.class);

        ridersListViewModel.getUsers().observe(this, riders -> {
            if (riders != null) {

                ArrayList<String> riderStrings = new ArrayList<String>();
                for (User rider : riders
                ) {
                    riderStrings.add(rider.toString());
                }

                int selectedIndex = 0;

                for (int i = 0; i < riderStrings.size(); i++) {
                    if (riderStrings.get(i).equals(order.getRider())){
                        selectedIndex = i;
                        break;
                    }
                }

                adapterRidersList.updateData(new ArrayList<>(riderStrings));
                spRider.setSelection(selectedIndex);
            }
        });
    }

    private void switchToEdit(){
        if(!isEditable){
            fab.hide();

            etCustomer.setFocusableInTouchMode(true);
            etCustomer.setFocusable(true);
            etCustomer.setEnabled(true);
            spProduct.setFocusableInTouchMode(true);
            spProduct.setFocusable(false);
            spProduct.setEnabled(true);
            etProductQty.setFocusableInTouchMode(true);
            etProductQty.setFocusable(true);
            etProductQty.setEnabled(true);
            etTimeDelivery.setFocusableInTouchMode(true);
            etTimeDelivery.setFocusable(false);
            etTimeDelivery.setEnabled(true);
            etDateDeliv.setFocusableInTouchMode(true);
            etDateDeliv.setFocusable(false);
            etDateDeliv.setEnabled(true);
            spStatus.setFocusableInTouchMode(true);
            spStatus.setFocusable(false);
            spStatus.setEnabled(true);
            spRider.setFocusableInTouchMode(true);
            spRider.setFocusable(true);
            spRider.setEnabled(true);
            etFromAddress.setFocusableInTouchMode(true);
            etFromAddress.setFocusable(true);
            etFromAddress.setEnabled(true);
            etToAddress.setFocusableInTouchMode(true);
            etToAddress.setFocusable(true);
            etToAddress.setEnabled(true);

        }else{
            List<String> checkpointsIds = new ArrayList<>();
            saveChanges(
                    etCustomer.getText().toString(),
                    etFromAddress.getText().toString(),
                    etToAddress.getText().toString(),
                    etTimeDelivery.getText().toString(),
                    etDateDeliv.getText().toString(),
                    spStatus.getSelectedItem().toString(),
                    spRider.getSelectedItem().toString(),
                    spProduct.getSelectedItem().toString(),
                    Integer.parseInt(etProductQty.getText().toString())
            );
            fab.hide();

            etCustomer.setFocusable(false);
            etCustomer.setEnabled(false);
            spProduct.setFocusable(false);
            spProduct.setEnabled(false);
            etProductQty.setFocusable(false);
            etProductQty.setEnabled(false);
            etTimeDelivery.setFocusable(false);
            etTimeDelivery.setEnabled(false);
            etDateDeliv.setFocusable(false);
            etDateDeliv.setEnabled(false);
            spStatus.setFocusable(false);
            spStatus.setEnabled(false);
            spRider.setFocusable(false);
            spRider.setEnabled(false);
        }
        isEditable = !isEditable;
    }

    /*
    private void createOrder(String sender, String receiver, String product,
                             int productQty, String datePickup, String dateDeliv, String status, String rider){

        if(sender.isEmpty()){
            etCustomer.setError(getString(R.string.order_error_sender));
            return;
        }
        if(receiver.isEmpty()){
            etReceiver.setError(getString(R.string.order_error_receiver));
            return;
        }

        if(datePickup.isEmpty()){
            etTimeDelivery.setError(getString(R.string.order_error_datePickup));
            return;
        }
        if(dateDeliv.isEmpty()){
            etTimeDelivery.setError(getString(R.string.order_error_dateDeliv));
            return;
        }

        order = new Order();
        order.setCustomer(sender);
        order.setProduct(product);
        order.setHowMany(productQty);
        order.setDatePickup(datePickup);
        order.setDateDelivery(dateDeliv);
        order.setState(status);
        order.setRider(rider);

        viewModel.createOrder(order, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "CreateClient: Success");
                onBackPressed();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "CreateClient: Failure", e);
            }
        });

    }

*/

    public void saveChanges(String customer, String fromAddress, String toAddress, String timeDelivery,
                            String dateDelivery, String state, String rider, String product, int howMany){

        order.setDateDelivery(dateDelivery);
        order.setTimeDelivery(timeDelivery);
        order.setFromAddress(fromAddress);
        order.setHowMany(howMany);
        order.setToAddress(toAddress);
        order.setCustomer(customer);
        order.setRider(rider);
        order.setState(state);
        order.setProduct(product);

        viewModel.updateOrder(order, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "updateOrder: Success");
                setResponse(true);
                onBackPressed();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateOrder: Failure", e);
                setResponse(false);
                onBackPressed();
            }
        });
    }

    private void setResponse(Boolean response) {
        if (response) {
            toast = Toast.makeText(this, getString(R.string.action_edited_order), Toast.LENGTH_LONG);
            toast.show();
        } else {
            toast = Toast.makeText(this, getString(R.string.action_error_order), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void updateContent(){
        if(order != null){
            etCustomer.setText(order.getCustomer());
            etTimeDelivery.setText(order.getTimeDelivery());
            etDateDeliv.setText(order.getDateDelivery());
            etFromAddress.setText(order.getFromAddress());
            etToAddress.setText(order.getToAddress());
            etProductQty.setText(""+order.getHowMany());
        }
    }
}
