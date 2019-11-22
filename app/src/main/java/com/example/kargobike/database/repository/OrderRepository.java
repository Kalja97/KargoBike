package com.example.kargobike.database.repository;

import com.example.kargobike.database.entity.OrderF;
import com.example.kargobike.firebase.OrderListLiveData;
import com.example.kargobike.firebase.OrderLiveData;
import com.example.kargobike.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class OrderRepository {

    private static final String TAG = "OrderRepository";

    private static OrderRepository instance;

    public OrderRepository() {
    }

    public static OrderRepository getInstance() {
        if (instance == null) {
            synchronized (OrderRepository.class) {
                if (instance == null) {
                    instance = new OrderRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<OrderF> getLocation(final String orderNr) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("countries")
                .child(orderNr);
        return new OrderLiveData(reference);
    }

    public LiveData<List<OrderF>> getAllOrders() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("orders");
        return new OrderListLiveData(reference);
    }

    public void insert(final OrderF order, final OnAsyncEventListener callback) {
        String id = order.getOrderNr();
        FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(id)
                .setValue(order, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final OrderF order, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(order.getOrderNr())
                .updateChildren(order.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final OrderF order, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(order.getOrderNr())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}



