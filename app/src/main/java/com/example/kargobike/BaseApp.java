package com.example.kargobike;

import android.app.Application;

import com.example.kargobike.database.repository.OrderRepository;
import com.example.kargobike.database.repository.CheckpointRepository;
import com.example.kargobike.database.repository.ProductRepository;
import com.example.kargobike.database.repository.UserRepository;

public class BaseApp extends Application {
    public OrderRepository getOrderRepository() {
        return OrderRepository.getInstance();
    }

    public CheckpointRepository getCheckpointRepository() {
        return CheckpointRepository.getInstance();
    }

    public ProductRepository getProductRepository() {
        return ProductRepository.getInstance();
    }

    public UserRepository getUserRepository() {
        return UserRepository.getInstance();
    }
}

