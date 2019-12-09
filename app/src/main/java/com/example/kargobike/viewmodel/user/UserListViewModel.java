package com.example.kargobike.viewmodel.user;

import android.app.Application;

import com.example.kargobike.database.entity.User;
import com.example.kargobike.database.repository.UserRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserListViewModel extends AndroidViewModel {

    private UserRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<User>> observableUsers;

    public UserListViewModel(@NonNull Application application, UserRepository userRepository) {
        super(application);

        repository = userRepository;

        observableUsers = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableUsers.setValue(null);

        LiveData<List<User>> users = repository.getAllUsers();

        // observe the changes of the entities from the database and forward them
        observableUsers.addSource(users, observableUsers::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final UserRepository userRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            userRepository = UserRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new UserListViewModel(application, userRepository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<User>> getUsers() {
        return observableUsers;
    }

    public void deleteUser(User user) {
        repository.delete(user, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFailure(Exception e) {}
        });
    }
}
