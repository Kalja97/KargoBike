package com.example.kargobike.database.repository;

import com.example.kargobike.database.entity.Product;
import com.example.kargobike.firebase.ProductListLiveData;
import com.example.kargobike.firebase.ProductLiveData;
import com.example.kargobike.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ProductRepository {

    private static final String TAG = "ProductRepository";
    private static ProductRepository instance;

    public ProductRepository() {
    }

    public static ProductRepository getInstance() {
        if (instance == null) {
            synchronized (ProductRepository.class) {
                if (instance == null) {
                    instance = new ProductRepository();
                }
            }
        }
        return instance;
    }

    //Query: Get a Product
    public LiveData<Product> getProduct(final String productNr) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("products")
                .child(productNr);
        return new ProductLiveData(reference);
    }

    //Query: Get all Products
    public LiveData<List<Product>> getAllProducts() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("products");
        return new ProductListLiveData(reference);
    }

    //Query: Insert a product
    public void insert(final Product product, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("products").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("products")
                .child(id)
                .setValue(product, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: Update a product
    public void update(final Product product, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("products")
                .child(product.getProductNumber())
                .updateChildren(product.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: Delete a prduct
    public void delete(final Product product, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("products")
                .child(product.getProductNumber())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}



