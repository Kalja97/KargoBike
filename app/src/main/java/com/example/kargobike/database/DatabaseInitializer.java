package com.example.kargobike.database;

import android.os.AsyncTask;
import android.util.Log;

import com.example.kargobike.Entities.Checkpoint;
import com.example.kargobike.Entities.Order;

public class DatabaseInitializer {

    public static final String TAG = "DatabaseInitializer";

    public static void populateDatabase(final AppDatabase db){
        Log.i(TAG, "Inserting demo data.");
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void addOrder(final AppDatabase db, final String orderNr, final String sender, final String receiver,
                                 final String product, final int howMany, final String rider, final String datePickup,
                                 final String dateDelivery, final String state) {
        Order order = new Order(orderNr, howMany, product, sender, receiver, rider, datePickup, dateDelivery, state);
        db.orderDao().insert(order);
    }

    private static void addCheckpoint(final AppDatabase db, final String orderNr, final String checkpointName,
                                      final String type, final String gps, final String datetime, final String rider) {
        Checkpoint checkpoint = new Checkpoint(orderNr, checkpointName, type, gps, datetime, rider);
        db.checkpointDao().insert(checkpoint);
    }

    private static void populateWithTestData(AppDatabase db) {
        db.orderDao().deleteAll();

        addOrder(db,
                "11111", "Jannis K.", "Samuel P.", "Ramassage courrier (10.-)", 1, "Rider #1", "02.02.2020", "03.02.2020", "not started"
        );
        addOrder(db,
                "11112", "Caroline M.", "Patrick G.", "Ramassage courrier (5.-)", 3, "Rider #2", "20.11.2019", "20.11.2019", "delivered"
        );
        addOrder(db,
                "11113", "Alexandre C.", "Michael S.", "Ramassage courrier (10.-)", 1, "Rider #3", "21.11.2019", "23.11.2019", "in progress"
        );


        try {
            // Let's ensure that the clients are already stored in the database before we continue.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addCheckpoint(db,
                "11112", "Start address", "Pickup","Coordinates XXX" , "20.11.2019" , "Rider #2"
        );
        addCheckpoint(db,
                "11112", "Target address", "Delivery","Coordinates YYY" , "20.11.2019" , "Rider #2"
        );
        addCheckpoint(db,
                "11113", "Start address", "Pickup","Coordinates ZZZ" , "21.11.2019" , "Rider #3"
        );
        addCheckpoint(db,
                "11113", "Stock Sierre", "in Stock","Coordinates WWW" , "22.11.2019" , "Rider #3"
        );
    }


    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase database;

        PopulateDbAsync(AppDatabase db) {
            database = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(database);
            return null;
        }

    }
}
