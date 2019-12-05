package com.example.kargobike.firebase;

import android.util.Log;

import com.example.kargobike.database.entity.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ProductListLiveData extends LiveData<List<Product>> {

    private static final String TAG = "ProductListLiveData";

    //Attributes
    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    //Constructor
    public ProductListLiveData(DatabaseReference ref) {
        reference = ref;
    }

    //on active method
    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    //on inactive method
    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toProducts(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    //fill the arraylist with the products
    private List<Product> toProducts(DataSnapshot snapshot) {
        List<Product> products = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            Product entity = childSnapshot.getValue(Product.class);
            entity.setProductNumber(childSnapshot.getKey());
            products.add(entity);
        }
        return products;
    }
}
