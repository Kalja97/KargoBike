package com.example.kargobike.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kargobike.util.RecyclerViewItemClickListener;

import java.util.List;

/**
 * Author: Samuel Pinto Da Silva
 * Creation date:
 * Last update date: 25.11.2019
 */
public class CheckPointAdapter<T> extends RecyclerView.Adapter<CheckPointAdapter.CheckPointViewHolder>
{
    private RecyclerViewItemClickListener mylistener;
    private List<T> data;

    static class CheckPointViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView rider;
        private final TextView DateTime;
        private final TextView longitude;
        private final TextView latitude;
        private final TextView type;


        private CheckPointViewHolder(View view, TextView name, TextView DateTime, TextView longitude, TextView latitude,
                                TextView type, TextView product, TextView productQty, TextView rider) {
            super(view);
            this.name = name;
            this.rider = rider;
            this.DateTime = DateTime;
            this.longitude = longitude;
            this.latitude = latitude;
            this.type = type;
        }
    }

    @NonNull
    @Override
    public CheckPointAdapter.CheckPointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckPointAdapter.CheckPointViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
