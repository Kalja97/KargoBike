package com.example.kargobike.viewmodel.checkpoint;

import android.app.Application;

import com.example.kargobike.BaseApp;
import com.example.kargobike.database.entity.Checkpoint;
import com.example.kargobike.database.repository.CheckpointRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CheckpointListViewModel extends AndroidViewModel {

    private static final String TAG = "CheckpointListViewModel";

    private CheckpointRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Checkpoint>> observableCheckpoints;
    private final MediatorLiveData<List<Checkpoint>> observableCheckpointsByOrder;

    public CheckpointListViewModel(@NonNull Application application,
                             final String orderNr, CheckpointRepository checkpointRepository) {
        super(application);

        repository = checkpointRepository;

        observableCheckpoints = new MediatorLiveData<>();
        observableCheckpointsByOrder = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableCheckpoints.setValue(null);
        observableCheckpointsByOrder.setValue(null);

        LiveData<List<Checkpoint>> checkpoints = new LiveData<List<Checkpoint>>() {
        };
        LiveData<List<Checkpoint>> checkpointsByOrder = new LiveData<List<Checkpoint>>() {
        };

        if(orderNr != null)
        {
            checkpointsByOrder = repository.getCheckpointsByOrder(orderNr);
        }


        checkpoints = repository.getCheckpoints();

        // observe the changes of the entities from the database and forward them
        observableCheckpoints.addSource(checkpoints, observableCheckpoints::setValue);
        observableCheckpointsByOrder.addSource(checkpointsByOrder, observableCheckpointsByOrder::setValue);
    }

    //Factory of the checkpoint list view model
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final CheckpointRepository checkpointRepository;
        private final String orderNr;

        public Factory(@NonNull Application application, String orderNr) {
            this.application = application;
            this.orderNr = orderNr;
            checkpointRepository =  CheckpointRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CheckpointListViewModel(application, orderNr, checkpointRepository);
        }
    }

    //Get all checkpoints
    public LiveData<List<Checkpoint>> getCheckpoints() {
        return observableCheckpoints;
    }

    //Get the checkpoints of an order
    public LiveData<List<Checkpoint>> getCheckpointsByOrder() {
        return observableCheckpointsByOrder;
    }

}

