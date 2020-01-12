package com.example.kargobike.ui.order;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.database.entity.Product;
import com.example.kargobike.viewmodel.order.OrderListViewModel;
import com.example.kargobike.viewmodel.product.ProductListViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

public class ExportActivity extends AppCompatActivity {

    private static final String TAG = "ExportOrders";

    //Attributes
    private String dateFrom;
    private String dateTo;

    private EditText etDateFrom;
    private EditText etDateTo;

    private Button btnExport;
    private ProgressBar pb;

    private List<Order> orders;
    private OrderListViewModel orderListViewModel;

    private List<Product> products;
    private ProductListViewModel productListViewModel;

    private Toolbar toolbar;

    private DatePickerDialog.OnDateSetListener DateSetListenerFrom;
    private DatePickerDialog.OnDateSetListener DateSetListenerTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //change title in toolbar and it's color
        setTitle("KargoBike - Orders");
        toolbar.setTitleTextColor(Color.WHITE);

        // Set toolbar clickable to go to the orderLsit quickly
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExportActivity.this, OrdersActivity.class);
                intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            }
        });

        initializeForm();

        btnExport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Get text from inputs
                dateFrom = etDateFrom.getText().toString().trim();
                dateTo = etDateTo.getText().toString().trim();

                if (dateFrom.isEmpty() || dateTo.isEmpty()) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(ExportActivity.this).create();
                    alertDialog.setTitle("Not all fields filled in");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Please fill in two dates");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                    alertDialog.show();
                    return;
                }

                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.VISIBLE);
                    }
                });

                getOrders();
                getProducts();

                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            export();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 5000);
            }
        });
    }

    private void getOrders() {
        orders = new ArrayList<>();
        OrderListViewModel.Factory factory = new OrderListViewModel.Factory(getApplication());
        orderListViewModel = ViewModelProviders.of(this, factory).get(OrderListViewModel.class);
        orderListViewModel.getOrders().observe(this, OrderEntities -> {
            if (OrderEntities != null) {
                orders = OrderEntities;
            }
        });
    }

    private void getProducts() {
        products = new ArrayList<>();
        ProductListViewModel.Factory factory = new ProductListViewModel.Factory(getApplication());
        productListViewModel = ViewModelProviders.of(this, factory).get(ProductListViewModel.class);
        productListViewModel.getProducts().observe(this, ProductEntities -> {
            if (ProductEntities != null) {
                products = ProductEntities;
            }
        });
    }

    private String getTotalPrice(Order o) {
        Double price;

        for (int i = 0; i < products.size(); i++) {
            if (o.getProduct().equals(products.get(i).toString())) {
                price = products.get(i).getPrice() * o.getHowMany();
                return price.toString();
            }
        }
        return "";
    }

    private void export() throws IOException, ParseException {

        Date from = new SimpleDateFormat("dd/MM/yyyy").parse(dateFrom);
        Date to = new SimpleDateFormat("dd/MM/yyyy").parse(dateTo);

        if (from.compareTo(to) > 0) {
            pb.setVisibility(View.GONE);
            final AlertDialog alertDialog = new AlertDialog.Builder(ExportActivity.this).create();
            alertDialog.setTitle("Date error");
            alertDialog.setCancelable(true);
            alertDialog.setMessage("From date has to be earlier than To Date");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
            return;
        }

        StringBuilder data = new StringBuilder();
        data.append("Date; Customer; Rider; Product; Amount; Price");

        Date inQuestion;
        String totalPrice;

        for (int i = 0; i < orders.size(); i++) {

            inQuestion = new SimpleDateFormat("dd/MM/yyyy").parse(orders.get(i).getDateDelivery());

            if (from.compareTo(inQuestion) * inQuestion.compareTo(to) >= 0) {
                totalPrice = getTotalPrice(orders.get(i));
                data.append("\n" + orders.get(i).getExportString() + "; " + totalPrice);
            }
        }

        pb.setVisibility(View.GONE);

        try {
            //saving the file into device
            FileOutputStream out = openFileOutput("order-report.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "order-report.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.kargobike.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeForm() {
        btnExport = findViewById(R.id.btnExport);
        pb = findViewById(R.id.pb);
        etDateFrom = findViewById(R.id.etDateForm);
        etDateTo = findViewById(R.id.etDateTo);

        etDateFrom.setFocusable(false);
        etDateTo.setFocusable(false);

        createDatepickers();
    }

    private void createDatepickers() {
        etDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ExportActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListenerFrom,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        etDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ExportActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListenerTo,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        DateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                etDateFrom.setText(date);
            }
        };

        DateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                etDateTo.setText(date);
            }
        };
    }
}
