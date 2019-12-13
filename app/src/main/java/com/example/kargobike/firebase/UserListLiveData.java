package com.example.kargobike.firebase;

import android.util.Log;

import com.example.kargobike.database.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class UserListLiveData extends LiveData<List<User>> {

    private static final String TAG = "UserListLiveData";

    //Attributes
    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    //Constructor
    public UserListLiveData(DatabaseReference ref) {
        reference = ref;
    }

    //on active method
    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    //on inactive method
    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toUsers(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    //fill the arraylist with the users
    private List<User> toUsers(DataSnapshot snapshot) {
        List<User> users = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            User entity = childSnapshot.getValue(User.class);
            entity.setIdNumber(childSnapshot.getKey());
            if (entity.getActive()==true) {
                users.add(entity);
            }
        }
        return users;
    }
}
