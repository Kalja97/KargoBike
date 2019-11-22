package com.example.kargobike.dao;

import com.example.kargobike.Entities.Checkpoint;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CheckpointDao {

    @Query("SELECT * FROM checkpoints WHERE orderNr = :orderNr")
    LiveData<List<Checkpoint>> getAllCheckpoints(String orderNr);

    @Query("Select * FROM checkpoints WHERE checkpointname = :checkpointname")
    LiveData<Checkpoint> getByOrder(String checkpointname);

    @Delete
    void delete(Checkpoint checkpoint);

    @Insert
    long insert(Checkpoint checkpoint);

    @Update
    void update(Checkpoint checkpoint);

    @Query("DELETE FROM checkpoints")
    void deleteAll();
}
