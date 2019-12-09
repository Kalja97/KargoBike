package com.example.kargobike.database.repository;

import com.example.kargobike.database.entity.User;
import com.example.kargobike.firebase.UserListLiveData;
import com.example.kargobike.firebase.UserLiveData;
import com.example.kargobike.util.OnAsyncEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private static UserRepository instance;

    public UserRepository() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
    }

    //Query: Get a User
    public LiveData<User> getUser(final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId);
        return new UserLiveData(reference);
    }

    //Query: Get all Users
    public LiveData<List<User>> getAllUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users");
        return new UserListLiveData(reference);
    }

    //Query: Insert a User
    public void insert(final User user, final OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("users").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(id)
                .setValue(user, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: Update a User
    public void update(final User user, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getIdNumber())
                .updateChildren(user.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    //Query: Delete a User
    public void delete(final User user, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getIdNumber())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}



