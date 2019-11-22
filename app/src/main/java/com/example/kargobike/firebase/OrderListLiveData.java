package com.example.kargobike.firebase;

import android.util.Log;

import com.example.kargobike.database.entity.OrderF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class OrderListLiveData extends LiveData<List<OrderF>> {

    private static final String TAG = "OrderListLiveData";

    //Attributes
    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    //Constructor
    public OrderListLiveData(DatabaseReference ref) {
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
            setValue(toOrders(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    //fill the arraylist with the orders
    private List<OrderF> toOrders(DataSnapshot snapshot) {
        List<OrderF> orders = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {

            OrderF entity = childSnapshot.getValue(OrderF.class);
            entity.setOrderNr(childSnapshot.getKey());
            orders.add(entity);
        }
        return orders;
    }
}
