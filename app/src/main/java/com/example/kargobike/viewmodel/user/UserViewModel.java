package com.example.kargobike.viewmodel.user;

import android.app.Application;

import com.example.kargobike.BaseApp;
import com.example.kargobike.database.entity.User;
import com.example.kargobike.database.repository.UserRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<User> observableUser;

    public UserViewModel(@NonNull Application application,
                          final String email, UserRepository userRepository) {

        super(application);

        repository = userRepository;

        observableUser = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableUser.setValue(null);


        LiveData<User> user = repository.getUser(email);


        //observe the changes of the client entity from the database and forward them
        observableUser.addSource(user, observableUser::setValue);

    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String email;
        private final UserRepository repository;

        public Factory(@NonNull Application application, String email) {
            this.application = application;
            this.email = email;
            this.repository = ((BaseApp) application).getUserRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new UserViewModel(application, email, repository);
        }
    }

    //get a user
    public LiveData<User> getUser() {
        return observableUser;
    }

    //create user
    public void createUser(User user, OnAsyncEventListener callback) {
        UserRepository.getInstance().insert(user, callback);
    }

    //update user
    public void updateUser(User user, OnAsyncEventListener callback) {
        UserRepository.getInstance().update(user, callback);
    }

    /*
    //delete user
    public void deleteUser(User user, OnAsyncEventListener callback) {
        UserRepository.getInstance().delete(user, callback);
    }
    */

}