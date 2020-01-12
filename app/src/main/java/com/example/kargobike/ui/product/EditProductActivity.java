package com.example.kargobike.ui.product;

import android.app.AlertDialog;
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
import com.example.kargobike.ui.MainActivity;
import com.example.kargobike.ui.order.OrdersActivity;
import com.example.kargobike.util.OnAsyncEventListener;
import com.example.kargobike.viewmodel.product.ProductViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

public class EditProductActivity extends AppCompatActivity {

    private static final String TAG = "EditProduct";

    //Edit texts of the layout
    private EditText etProductName;
    private EditText etPrice;

    private String productName;
    private double price;

    private ProductViewModel viewModel;
    private Product product;

    //Button
    private Button btnChange;

    //Toolbar
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        String prod = getIntent().getStringExtra("productId");

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //change title in toolbar and it's color
        setTitle("KargoBike - Products");
        toolbar.setTitleTextColor(Color.WHITE);

        // Set toolbar clickable to go to the orderLsit quickly
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProductActivity.this, OrdersActivity.class);
                intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                startActivity(intent);
            }
        });

        initiateView();

        ProductViewModel.Factory factory = new ProductViewModel.Factory(getApplication(), prod);

        viewModel = ViewModelProviders.of(this, factory).get(ProductViewModel.class);
        viewModel.getProduct().observe(this, productEntity -> {
            if (productEntity != null) {
                product = productEntity;
                updateContent();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = etProductName.getText().toString().trim();
                String priceS = etPrice.getText().toString().trim();

                //call method for checking if required fields are filled
                if (checkInputField(priceS)) {
                    // Cast String to Double
                    price = Double.valueOf(priceS);
                    //call method for saving checkpoint
                    saveChanges();
                }
                return;
            }
        });
    }

    //initialize the edit texts
    private void initiateView() {
        etProductName = findViewById(R.id.show_productName);
        etProductName.setEnabled(false);
        etPrice = findViewById(R.id.change_price);
        btnChange = findViewById(R.id.btnChangeProduct);
    }

    //update text in the view
    private void updateContent() {
        if (product != null) {
            etProductName.setText(product.getName());
            etPrice.setText(product.getPrice().toString());
        }
    }

    //save the changes into the database
    private void saveChanges() {
        product.setName(productName);
        product.setPrice(price);

        viewModel.updateProduct(product, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "updateProduct: success");
                Intent intent = new Intent(EditProductActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateProduct: failure", e);
                final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(EditProductActivity.this).create();
                alertDialog.setTitle("Can not save");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Cannot edit this product");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();
            }
        });
    }

    private boolean checkInputField(String price) {
        //Check if all filed are filled in
        if (productName.isEmpty() || price.isEmpty()) {
            final AlertDialog alertDialog = new AlertDialog.Builder(EditProductActivity.this).create();
            alertDialog.setTitle("Not all fields filled in");
            alertDialog.setCancelable(true);
            alertDialog.setMessage("Please fill in all fields first");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 2, Menu.NONE, getString(R.string.action_delete))
                .setIcon(R.drawable.ic_delete_white)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 2) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.action_delete));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.message_DELETE_PRODUCT));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_delete), (dialog, which) -> {
                deleteProduct();
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteProduct() {
        product.setActive(false);

        viewModel.updateProduct(product, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "deleteProduct: success");
                Intent intent = new Intent(EditProductActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "deleteProduct: failure", e);
                final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(EditProductActivity.this).create();
                alertDialog.setTitle("Can not save");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Cannot edit this product");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "ok", (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();
            }
        });
    }
}