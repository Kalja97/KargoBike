package com.example.kargobike.firebase;

import android.util.Log;

import com.example.kargobike.database.entity.Checkpoint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CheckpointLiveData extends LiveData<Checkpoint> {
    private static final String TAG = "CheckpointLiveData";

    //Attributes
    private final DatabaseReference reference;
    private final CheckpointLiveData.MyValueEventListener listener = new CheckpointLiveData.MyValueEventListener();

    //Constructor
    public CheckpointLiveData(DatabaseReference ref) {
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
            Checkpoint entity = dataSnapshot.getValue(Checkpoint.class);
            if (entity != null) {
                entity.setId(dataSnapshot.getKey());
                setValue(entity);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
