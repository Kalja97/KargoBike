package com.example.kargobike.async.checkpoint;

import android.app.Application;
import android.os.AsyncTask;

import com.example.kargobike.Entities.Checkpoint;
import com.example.kargobike.util.OnAsyncEventListener;

public class UpdateCheckpoint extends AsyncTask<Checkpoint, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    //constructor
    public UpdateCheckpoint(Application application, OnAsyncEventListener callback) {
        this.application = application;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Checkpoint... params) {
        try {
            for (Checkpoint Checkpoint : params) {
                //update Checkpoint

                /*
                ((BaseApp) application).getDatabase().checkpointDao()
                        .update(Checkpoint);

                 */
            }
        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (callback != null) {
            if (exception == null) {
                callback.onSuccess();
            } else {
                callback.onFailure(exception);
            }
        }
    }
}