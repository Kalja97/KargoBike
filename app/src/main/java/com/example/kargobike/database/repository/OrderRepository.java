package com.example.kargobike.database.repository;

import com.example.kargobike.database.entity.Order;
import com.example.kargobike.firebase.OrderListFilteredLiveData;
import com.example.kargobike.firebase.OrderListLiveData;
import com.example.kargobike.firebase.OrderLiveData;
import com.example.kargobike.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
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

    //Query: Get a order
    public LiveData<Order> getOrder(final String orderNr) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(orderNr);
        return new OrderLiveData(reference);
    }

    //Query: Get all Orders
    public LiveData<List<Order>> getAllOrders() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("orders");
        return new OrderListLiveData(reference);
    }

    //Query: Get all orders by date and user
    public LiveData<List<Order>> getOrdersDateUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("orders");
        return new OrderListFilteredLiveData(reference);
    }


    //Query: Insert an order
    public void insert(final Order order, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("orders").push().getKey();
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

    //Query: Update an order
    public void update(final Order order, final OnAsyncEventListener callback) {
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

    //Query: Delete an order
    public void delete(final Order order, OnAsyncEventListener callback) {
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



