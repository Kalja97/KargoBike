package com.example.kargobike.repository;

import android.app.Application;

import com.example.kargobike.Entities.Checkpoint;
import com.example.kargobike.async.checkpoint.CreateCheckpoint;
import com.example.kargobike.async.checkpoint.DeleteCheckpoint;
import com.example.kargobike.async.checkpoint.UpdateCheckpoint;
import com.example.kargobike.util.OnAsyncEventListener;

public class CheckpointRepository {
    private static CheckpointRepository instance;

    //constructor
    private CheckpointRepository() {

    }

    //create instance
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

    //call all query methods

    /*
    public LiveData<Checkpoint> getCheckpoint(final String order, Application application) {
        return ((BaseApp) application).getDatabase().checkpointDao().getByOrder(order);
    }

    public LiveData<List<Checkpoint>> getCheckpoints(final String order, Application application) {
        return ((BaseApp) application).getDatabase().checkpointDao().getAllCheckpoints(order);
    }

     */

    public void insert(final Checkpoint checkpoint, OnAsyncEventListener callback,
                       Application application) {
        new CreateCheckpoint(application, callback).execute(checkpoint);
    }

    public void update(final Checkpoint checkpoint, OnAsyncEventListener callback,
                       Application application) {
        new UpdateCheckpoint(application, callback).execute(checkpoint);
    }

    public void delete(final Checkpoint checkpoint, OnAsyncEventListener callback,
                       Application application) {
        new DeleteCheckpoint(application, callback).execute(checkpoint);
    }
}
