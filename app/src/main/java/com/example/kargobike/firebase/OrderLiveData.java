package com.example.kargobike.firebase;

import android.util.Log;

import com.example.kargobike.database.entity.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class OrderLiveData extends LiveData<Order> {
    private static final String TAG = "OrderLiveData";

    //Attributes
    private final DatabaseReference reference;
    private final OrderLiveData.MyValueEventListener listener = new OrderLiveData.MyValueEventListener();

    //Constructor
    public OrderLiveData(DatabaseReference ref) {
        this.reference = ref;
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
            Order entity = dataSnapshot.getValue(Order.class);
            if (entity != null) {
                entity.setOrderNr(dataSnapshot.getKey());
                setValue(entity);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

}

