package com.example.kargobike.ui.order;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.ui.LogActivity;
import com.example.kargobike.ui.SettingsActivity;
import com.example.kargobike.ui.checkpoint.CheckpointsActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.kargobike.viewmodel.order.OrderViewModel;

import java.util.Calendar;

public class DetailsOrderActivity extends AppCompatActivity {

    private static final String TAG = "orderDetails";

    private static final int CREATE_ORDER = 0 ;
    private static final int EDIT_ORDER = 1 ;
    private static final int DELETE_ORDER = 2 ;

    private OrderViewModel viewModel ;

    private Toast toast ;

    //private TextView tvOrderNr;
    private EditText etSender;
    private EditText etReceiver;
    private Spinner spProduct;
    private EditText etProductQty;
    private EditText etDatePickup;
    private EditText etDateDeliv;
    private Spinner spStatus;
    private EditText etRider;

    private boolean isEditable;

    private Order order ;

    FloatingActionButton fab ;
    private Toolbar toolbar;

    //NEW
    private DatePickerDialog.OnDateSetListener DateSetListenerDelivery;
    private DatePickerDialog.OnDateSetListener DateSetListenerPickup;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
/*
        // Check the theme
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            // set darkTheme if the button is checked
            setTheme(R.style.DarkTheme);
        }
        else
            // set darkTheme if the button isn't checked
            setTheme(R.style.AppTheme);
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_orders);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String OrderNr = getIntent().getStringExtra("OrderNr");

        initiateView();

        OrderViewModel.Factory factory = new OrderViewModel.Factory(getApplication(), OrderNr);
        viewModel = ViewModelProviders.of(this, factory).get(OrderViewModel.class);
        viewModel.getOrder().observe(this, Order -> {
            if(Order != null){
                order = Order ;
                updateContent();
            }
        });

        fab = findViewById(R.id.checkpointsButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(DetailsOrderActivity.this, CheckpointsActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION
            );
            intent.putExtra("OrderNr", order.getOrderNr());
            startActivity(intent);
        });

        if(OrderNr != null){
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
                        DetailsOrderActivity.this,
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
                etDateDeliv.setText(date);


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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        //String orderNr = getIntent().getStringExtra("OrderNr");

        setTitle("KargoBike - Order Details");
        toolbar.setTitleTextColor(Color.WHITE);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        if(order != null){
            menu.add(0, EDIT_ORDER, Menu.NONE, getString(R.string.action_edit))
                    .setIcon(R.drawable.ic_edit_white)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, DELETE_ORDER, Menu.NONE, getString(R.string.action_delete))
                    .setIcon(R.drawable.ic_delete_white)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
        etSender = findViewById(R.id.sender);
        etReceiver = findViewById(R.id.receiver);
        spProduct = findViewById(R.id.product);
        etProductQty = findViewById(R.id.howManyPackages);
        etDatePickup = findViewById(R.id.datePickup);
        etDateDeliv = findViewById(R.id.dateDelivery);
        spStatus = findViewById(R.id.state);
        etRider = findViewById(R.id.rider);

        etReceiver.setFocusable(false);
        etReceiver.setEnabled(false);
        etSender.setFocusable(false);
        etSender.setEnabled(false);
        spProduct.setFocusable(false);
        spProduct.setEnabled(false);
        etProductQty.setFocusable(false);
        etProductQty.setEnabled(false);
        etDatePickup.setFocusable(false);
        etDatePickup.setEnabled(false);
        etDateDeliv.setFocusable(false);
        etDateDeliv.setEnabled(false);
        spStatus.setFocusable(false);
        spStatus.setEnabled(false);
        etRider.setFocusable(false);
        etRider.setEnabled(false);

    }

    private void switchToEdit(){
        if(!isEditable){
            fab.hide();


            etReceiver.setFocusableInTouchMode(true);
            etReceiver.setFocusable(true);
            etReceiver.setEnabled(true);
            etSender.setFocusableInTouchMode(true);
            etSender.setFocusable(true);
            etSender.setEnabled(true);
            spProduct.setFocusableInTouchMode(true);
            spProduct.setFocusable(false);
            spProduct.setEnabled(true);
            etProductQty.setFocusableInTouchMode(true);
            etProductQty.setFocusable(true);
            etProductQty.setEnabled(true);
            etDatePickup.setFocusableInTouchMode(true);
            etDatePickup.setFocusable(false);
            etDatePickup.setEnabled(true);
            etDateDeliv.setFocusableInTouchMode(true);
            etDateDeliv.setFocusable(false);
            etDateDeliv.setEnabled(true);
            spStatus.setFocusableInTouchMode(true);
            spStatus.setFocusable(false);
            spStatus.setEnabled(true);
            etRider.setFocusableInTouchMode(true);
            etRider.setFocusable(true);
            etRider.setEnabled(true);




        }else{
            saveChanges(
                    etSender.getText().toString(),
                    etReceiver.getText().toString(),
                    spProduct.getSelectedItem().toString(),
                    Integer.parseInt(etProductQty.getText().toString()),
                    etDatePickup.getText().toString(),
                    etDateDeliv.getText().toString(),
                    spStatus.getSelectedItem().toString(),
                    etRider.getText().toString()
            );
            fab.hide();
            etReceiver.setFocusable(false);
            etReceiver.setEnabled(false);
            etSender.setFocusable(false);
            etSender.setEnabled(false);
            spProduct.setFocusable(false);
            spProduct.setEnabled(false);
            etProductQty.setFocusable(false);
            etProductQty.setEnabled(false);
            etDatePickup.setFocusable(false);
            etDatePickup.setEnabled(false);
            etDateDeliv.setFocusable(false);
            etDateDeliv.setEnabled(false);
            spStatus.setFocusable(false);
            spStatus.setEnabled(false);
            etRider.setFocusable(false);
            etRider.setEnabled(false);
        }
        isEditable = !isEditable;
    }

    private void createOrder(String sender, String receiver, String product,
                             int productQty, String datePickup, String dateDeliv, String status, String rider){


        if(sender.isEmpty()){
            etSender.setError(getString(R.string.order_error_sender));
            return;
        }
        if(receiver.isEmpty()){
            etReceiver.setError(getString(R.string.order_error_receiver));
            return;
        }

        if(datePickup.isEmpty()){
            etDatePickup.setError(getString(R.string.order_error_datePickup));
            return;
        }
        if(dateDeliv.isEmpty()){
            etDatePickup.setError(getString(R.string.order_error_dateDeliv));
            return;
        }

        //!!! For the moment we don't check if the rider is in the database!!!
        if(rider.isEmpty()){
            etRider.setError(getString(R.string.order_error_rider));
            return;
        }

        order = new Order();
        order.setSender(sender);
        order.setReceiver(receiver);
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

    public void saveChanges(String sender, String receiver, String product,
                            int productQty, String datePickup, String dateDeliv, String status, String rider){

        //order.setOrderNr(""+R.id.orderNumber);
        order.setSender(sender);
        order.setReceiver(receiver);
        order.setProduct(product);
        order.setHowMany(productQty);
        order.setDatePickup(datePickup);
        order.setDateDelivery(dateDeliv);
        order.setState(status);
        order.setRider(rider);

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


            etSender.setText(order.getSender());
            etReceiver.setText(order.getReceiver());
            spProduct.setSelection(0);
            etProductQty.setText(""+order.getHowMany());
            etDatePickup.setText(order.getDatePickup());
            etDateDeliv.setText(order.getDateDelivery());
            spStatus.setSelection(0);
            etRider.setText(order.getRider());

        }
    }


}
