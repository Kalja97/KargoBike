package com.example.kargobike.repository;

import android.app.Application;

import com.example.kargobike.BaseApp;
import com.example.kargobike.Entities.Order;
import com.example.kargobike.async.order.CreateOrder;
import com.example.kargobike.async.order.DeleteOrder;
import com.example.kargobike.async.order.UpdateOrder;
import com.example.kargobike.util.OnAsyncEventListener;

import java.util.List;

import androidx.lifecycle.LiveData;

public class OrderRepository {

        private static OrderRepository instance;

        //constructor
        public OrderRepository() {

        }

        //create instance
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

        //Call all query methods

    /*
        public LiveData<Order> getOrder(final String order, Application application) {
            return ((BaseApp) application).getDatabase().orderDao().getByPK(order);
        }

        public LiveData<List<Order>> getOrders(Application application) {
            return ((BaseApp) application).getDatabase().orderDao().getAllOrders();
        }

     */

        public void insert(final Order order, OnAsyncEventListener callback,
                           Application application) {
            new CreateOrder(application, callback).execute(order);
        }

        public void update(final Order order, OnAsyncEventListener callback,
                           Application application) {
            new UpdateOrder(application, callback).execute(order);
        }

        public void delete(final Order order, OnAsyncEventListener callback,
                           Application application) {
            new DeleteOrder(application, callback).execute(order);
        }
}



