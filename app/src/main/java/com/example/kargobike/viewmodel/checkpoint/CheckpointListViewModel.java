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

    private CheckpointRepository repository;
    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Checkpoint>> observableOrders;

    public CheckpointListViewModel(@NonNull Application application,
                             CheckpointRepository checkpointRepository, String order) {
        super(application);

        this.application = application;
        this.repository = checkpointRepository;

        observableOrders = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableOrders.setValue(null);

        /*
        LiveData<List<Checkpoint>> checkpoints = repository.getCheckpoints(order, application);

        // observe the changes of the client entity from the database and forward them
        observableOrders.addSource(checkpoints, observableOrders::setValue);

         */
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String order;
        private final CheckpointRepository repository;

        public Factory(@NonNull Application application, String order) {
            this.application = application;
            this.order = order;
            repository = ((BaseApp) application).getCheckpointRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CheckpointListViewModel(application, repository, order);
        }
    }

    /**
     * Expose the LiveData ClientEntity query so the UI can observe it.
     */
    public LiveData<List<Checkpoint>> getCheckpoints() {
        return observableOrders;
    }
}