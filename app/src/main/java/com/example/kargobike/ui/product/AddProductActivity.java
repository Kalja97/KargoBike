package com.example.kargobike.ui.product;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.Product;
import com.example.kargobike.database.entity.Product;
import com.example.kargobike.ui.MainActivity;
import com.example.kargobike.ui.order.OrdersActivity;
import com.example.kargobike.ui.product.AddProductActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.product.ProductViewModel;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = "AddProduct";

    //Attributes
    private String productName;
    private String price;
    private Double priceDouble;

    private ProductViewModel viewModel;
    private Toolbar toolbar;

    EditText etProductName;
    EditText etPrice;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String prod = "";

        //initialize viewmodel
        ProductViewModel.Factory factory = new ProductViewModel.Factory(getApplication(), prod);
        viewModel = ViewModelProviders.of(this, factory).get(ProductViewModel.class);

        //change title in toolbar and it's color
        setTitle("KargoBike - Products");
        toolbar.setTitleTextColor(Color.WHITE);

        // Set toolbar clickable to go to the orderLsit quickly
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProductActivity.this, OrdersActivity.class);
                intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            }
        });

        etProductName = (EditText) findViewById(R.id.productName);
        etPrice = (EditText) findViewById(R.id.productPrice);

        //Buttons
        btnAdd = findViewById(R.id.btnaddproduct);

        //Add product to the database
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                productName = etProductName.getText().toString().trim();
                price = etPrice.getText().toString().trim();

                //Check if all filed are filled in
                if(productName.isEmpty() || price.isEmpty()){
                    final AlertDialog alertDialog = new AlertDialog.Builder(AddProductActivity.this).create();
                    alertDialog.setTitle("Not all fields filled in");
                    alertDialog.setCancelable(true);
                    alertDialog.setMessage("Please fill in all fields first");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                    alertDialog.show();
                    return;
                }

                //cast String to Double
                priceDouble = Double.valueOf(price.toString());

                //create new location object
                Product product = new Product();
                product.setName(productName);
                product.setPrice(priceDouble);

                //add to firebase
                viewModel.createProduct(product, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "create product: success");
                        Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "create product: failure", e);
                        final AlertDialog alertDialog = new AlertDialog.Builder(AddProductActivity.this).create();
                        alertDialog.setTitle("Can not save");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Cannot add this product");
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                        alertDialog.show();
                    }
                });
            }
        });
    }
}