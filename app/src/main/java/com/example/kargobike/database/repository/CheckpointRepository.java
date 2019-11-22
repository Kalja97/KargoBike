package com.example.kargobike.database.repository;


import com.example.kargobike.database.entity.Checkpoint;
import com.example.kargobike.firebase.CheckpointListLiveData;
import com.example.kargobike.firebase.CheckpointLiveData;
import com.example.kargobike.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CheckpointRepository {
    private static final String TAG = "CheckpointRepository";

    private static CheckpointRepository instance;

    public CheckpointRepository() {
    }

    //Constructor
    public static CheckpointRepository getInstance() {
        if (instance == null) {
            synchronized (CheckpointRepository.class) {
                if (instance == null) {
                    instance = new CheckpointRepository();
                }
            }
        }
        return instance;
    }

    //Query: get a checkpoint
    public LiveData<Checkpoint> getCheckpoint(final String orderNr, final String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(orderNr)
                .child("checkpoints")
                .child(id);
        return new CheckpointLiveData(reference);
    }

    //Query: get all checkpoints of a order
    public LiveData<List<Checkpoint>> getcheckpointsByCountry(final String orderNr) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(orderNr)
                .child("checkpoints");
        return new CheckpointListLiveData(reference, orderNr);
    }

    //Query: insert a checkpoint
    public void insert(final Checkpoint checkpoint, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("orders").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(checkpoint.getOrderNr())
                .child("checkpoints")
                .child(id)
                .setValue(checkpoint, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: update a checkpoint
    public void update(final Checkpoint checkpoint, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(checkpoint.getOrderNr())
                .child("checkpoints")
                .child(checkpoint.getId())
                .updateChildren(checkpoint.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: delete a checkpoint
    public void delete(final Checkpoint checkpoint, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(checkpoint.getOrderNr())
                .child("checkpoints")
                .child(checkpoint.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
