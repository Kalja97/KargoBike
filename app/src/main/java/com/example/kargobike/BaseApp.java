package com.example.kargobike;

import android.app.Application;

import com.example.kargobike.database.repository.OrderRepository;
import com.example.kargobike.database.repository.CheckpointRepository;

public class BaseApp extends Application {
    public OrderRepository getOrderRepository() {
        return OrderRepository.getInstance();
    }

    public CheckpointRepository getCheckpointRepository() {
        return CheckpointRepository.getInstance();
    }
}

