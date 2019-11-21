package com.example.kargobike.async.order;

import android.app.Application;
import android.os.AsyncTask;

import com.example.kargobike.BaseApp;
import com.example.kargobike.Entities.Order;
import com.example.kargobike.util.OnAsyncEventListener;

public class CreateOrder extends AsyncTask<Order, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    //constructor
    public CreateOrder(Application application, OnAsyncEventListener callback) {
        this.application = application;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Order... params) {
        try {
            for (Order order : params) {
                //Create order

                /*
                ((BaseApp) application).getDatabase().orderDao()
                        .insert(order);
                                         */
            }
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (callback != null) {
            if (exception == null) {
                callback.onSuccess();
            } else {
                callback.onFailure(exception);
            }
        }
    }
}