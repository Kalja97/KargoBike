package com.example.kargobike.dao;

import android.database.sqlite.SQLiteConstraintException;


import com.example.kargobike.Entities.Order;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class OrderDao {

    @Query("Select * FROM orders WHERE orderNr = :orderNr")
    public abstract LiveData<Order> getByPK(String orderNr);

    @Query("Select * From orders")
    public abstract LiveData<List<Order>> getAllOrders();

    @Insert
    public abstract long insert(Order order) throws SQLiteConstraintException;

    @Update
    public abstract void update(Order order);

    @Delete
    public abstract void delete(Order order);

    @Query("DELETE FROM orders")
    public abstract void deleteAll();
}

