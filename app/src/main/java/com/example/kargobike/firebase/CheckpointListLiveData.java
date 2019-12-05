package com.example.kargobike.firebase;

import android.util.Log;

import com.example.kargobike.database.entity.Checkpoint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class CheckpointListLiveData extends LiveData<List<Checkpoint>> {

    private static final String TAG = "CheckpointListLiveData";

    //Attributes
    private final DatabaseReference reference;
    //private final String orderNr;
    private final MyValueEventListener listener = new MyValueEventListener();

    //Constructor
    public CheckpointListLiveData(DatabaseReference ref, String orderNr) {
        reference = ref;
        //this.orderNr = orderNr;
    }

    public CheckpointListLiveData(DatabaseReference ref) {
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
            setValue(toCheckpoints(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    //fill the arraylist with the checkpoints
    private List<Checkpoint> toCheckpoints(DataSnapshot snapshot) {
        List<Checkpoint> checkpoints = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {

            Checkpoint entity = childSnapshot.getValue(Checkpoint.class);
            entity.setcheckPointID(childSnapshot.getKey());
            //entity.setOrderNr(orderNr);
            checkpoints.add(entity);
        }
        return checkpoints;
    }

}
