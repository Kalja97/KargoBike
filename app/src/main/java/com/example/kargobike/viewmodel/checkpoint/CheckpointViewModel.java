package com.example.kargobike.viewmodel.checkpoint;

import android.app.Application;

import com.example.kargobike.BaseApp;
import com.example.kargobike.Entities.Checkpoint;
import com.example.kargobike.repository.CheckpointRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CheckpointViewModel  extends AndroidViewModel {

    private Application application;
    private CheckpointRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<Checkpoint> observableCheckpoint;

    public CheckpointViewModel(@NonNull Application application,
                         final String checkpointname, CheckpointRepository checkpointRepository) {
        super(application);

        this.application = application;
        this.repository = checkpointRepository;

        observableCheckpoint = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableCheckpoint.setValue(null);

        /*
        LiveData<Checkpoint> checkpoint = repository.getCheckpoint(checkpointname, application);

        //observe the changes of the account entity from the database and forward them
        observableCheckpoint.addSource(checkpoint, observableCheckpoint::setValue);

         */
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String checkpointname;

        private final CheckpointRepository repository;

        public Factory(@NonNull Application application, String checkpointname) {
            this.application = application;
            this.checkpointname = checkpointname;
            repository = ((BaseApp) application).getCheckpointRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CheckpointViewModel(application, checkpointname, repository);
        }
    }

    /**
     * Expose the LiveData Checkpoint query so the UI can observe it.
     */
    public LiveData<Checkpoint> getCheckpoint() {
        return observableCheckpoint;
    }

    public void createCheckpoint(Checkpoint checkpoint, OnAsyncEventListener callback) {
        repository.insert(checkpoint, callback, application);
    }

    public void updateCheckpoint(Checkpoint checkpoint, OnAsyncEventListener callback) {
        repository.update(checkpoint, callback, application);
    }

    public void deleteCheckpoint(Checkpoint checkpoint, OnAsyncEventListener callback) {
        repository.delete(checkpoint, callback, application);
    }
}