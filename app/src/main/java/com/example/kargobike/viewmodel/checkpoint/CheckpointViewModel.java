package com.example.kargobike.viewmodel.checkpoint;

import android.app.Application;

import com.example.kargobike.BaseApp;
import com.example.kargobike.database.entity.Checkpoint;
import com.example.kargobike.database.repository.CheckpointRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CheckpointViewModel extends AndroidViewModel {

    private static final String TAG = "CheckpointViewModel";

    private CheckpointRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<Checkpoint> mObservableCheckpoint;

    public CheckpointViewModel(@NonNull Application application,/* final String orderNr,*/ final String id,
                         CheckpointRepository checkpointRepository) {
        super(application);

        repository = checkpointRepository;
        mObservableCheckpoint = new MediatorLiveData<>();

        // set by default null, until we get data from the database.
        mObservableCheckpoint.setValue(null);

        LiveData<Checkpoint> checkpoint = repository.getCheckpoint(/*orderNr,*/ id);

        // observe the changes of the client entity from the database and forward them
        mObservableCheckpoint.addSource(checkpoint, mObservableCheckpoint::setValue);
    }

    //Factory of the checkpoint view model
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        //private final String orderNr;
        private final String id;
        private final CheckpointRepository repository;

        public Factory(@NonNull Application application, /*String orderNr,*/ String id) {
            this.application = application;
            //this.orderNr = orderNr;
            this.id = id;
            repository = CheckpointRepository.getInstance();;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CheckpointViewModel(application,/* orderNr,*/ id, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntity query so the UI can observe it.
     */
    //get a checkpoint
    public LiveData<Checkpoint> getCheckpoint() {
        return mObservableCheckpoint;
    }

    //create a checkpoint
    public void createCheckpoint(Checkpoint checkpoint, OnAsyncEventListener callback) {
        CheckpointRepository.getInstance().insert(checkpoint, callback);
    }

    //update a checkpoint
    public void updateCheckpoint(Checkpoint checkpoint, OnAsyncEventListener callback) {
        CheckpointRepository.getInstance().update(checkpoint, callback);
    }

    //delete a checkpoint
    public void deleteCheckpoint(Checkpoint checkpoint, OnAsyncEventListener callback) {
        CheckpointRepository.getInstance().delete(checkpoint, callback);
    }
}

