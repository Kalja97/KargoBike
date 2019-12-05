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
    public LiveData<Checkpoint> getCheckpoint(final String checkPointID, final String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("checkpoints")
                .child(checkPointID)
                .child("checkpoints")
                .child(id);
        return new CheckpointLiveData(reference);
    }

    //Query: get all checkpoints of a order
    public LiveData<List<Checkpoint>> getCheckpointsByOrder(final String checkPointID) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(checkPointID)
                .child("checkpoints");
        return new CheckpointListLiveData(reference, checkPointID);
    }

    //Query: get all checkpoints
//    public LiveData<List<Checkpoint>> getCheckpoints(final String checkPointID) {
//        DatabaseReference reference = FirebaseDatabase.getInstance()
//                .getReference("checkpoints")
//                .child(checkPointID)
//                .child("checkpoints");
//        return new CheckpointListLiveData(reference, checkPointID);
//    }

    //Query: get all checkpoints
    public LiveData<List<Checkpoint>> getCheckpoints() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("checkpoints");
        return new CheckpointListLiveData(reference);
    }

    //Query: insert a checkpoint
    public void insert(final Checkpoint checkpoint, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("checkpoints").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("checkpoints")
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
                .getReference("checkpoints")
                .child(checkpoint.getcheckPointID())
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
                .getReference("checkpoints")
                .child(checkpoint.getcheckPointID())
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