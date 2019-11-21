package com.example.kargobike;

import android.app.Application;

import com.example.kargobike.database.AppDatabase;
import com.example.kargobike.repository.OrderRepository;
import com.example.kargobike.repository.CheckpointRepository;

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*
    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

     */

    public OrderRepository getOrderRepository() {
        return OrderRepository.getInstance();
    }

    public CheckpointRepository getCheckpointRepository() {
        return CheckpointRepository.getInstance();
    }
}

