package com.example.kargobike.async.order;

import android.app.Application;
import android.os.AsyncTask;

import com.example.kargobike.BaseApp;
import com.example.kargobike.Entities.Order;
import com.example.kargobike.util.OnAsyncEventListener;

public class UpdateOrder  extends AsyncTask<Order, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    //constructor
    public UpdateOrder(Application application, OnAsyncEventListener callback) {
        this.application = application;
        callback = callback;
    }

    @Override
    protected Void doInBackground(Order... params) {
        try {
            for (Order order : params) {
                //update order

                /*
                ((BaseApp) application).getDatabase().orderDao()
                        .update(order);

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