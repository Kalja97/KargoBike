package com.example.kargobike.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.Checkpoint;
import com.example.kargobike.util.RecyclerViewItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CheckPointAdapter<T> extends RecyclerView.Adapter<CheckPointAdapter.CheckPointViewHolder> {
    private RecyclerViewItemClickListener mylistener;
    private List<T> data;

    public CheckPointAdapter(RecyclerViewItemClickListener listener) {
        mylistener = listener;
    }

    @Override
    public CheckPointAdapter.CheckPointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerviewitem_checkpoint, parent, false);

        final TextView twName = v.findViewById(R.id.cpName);
        final TextView twRider = v.findViewById(R.id.cpRider);
        final TextView twDateTime = v.findViewById(R.id.cpDate);
        final TextView twGps = v.findViewById(R.id.cpLat);
        final TextView twType = v.findViewById(R.id.cpType);

        final CheckPointViewHolder CheckPointViewHolder = new CheckPointViewHolder(v, twName, twDateTime, twGps, twType, twRider);
        v.setOnClickListener(view -> mylistener.onItemClick(view, CheckPointViewHolder.getAdapterPosition()));
        v.setOnClickListener(view -> {
            mylistener.onItemLongClick(view, CheckPointViewHolder.getAdapterPosition());
        });
        return CheckPointViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckPointAdapter.CheckPointViewHolder holder, int position) {
        T item = data.get(position);
        holder.name.setText(((Checkpoint) item).getCheckpointName());
        holder.DateTime.setText(((Checkpoint) item).getDatetime());
        holder.gps.setText(((Checkpoint) item).getGps());
        holder.type.setText(((Checkpoint) item).getType());
        holder.rider.setText(((Checkpoint) item).getRider());

    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<T> checkpoints) {
        if (this.data == null) {
            this.data = checkpoints;
            notifyItemRangeInserted(0, checkpoints.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return data.size();
                }

                @Override
                public int getNewListSize() {
                    return checkpoints.size();
                }

                @Override
                public boolean areItemsTheSame(int i, int i1) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int i, int i1) {
                    return false;
                }
            });
            data = checkpoints;
            result.dispatchUpdatesTo(this);
        }
    }

    static class CheckPointViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView rider;
        private final TextView DateTime;
        private final TextView gps;
        private final TextView type;

        private CheckPointViewHolder(View view, TextView name, TextView DateTime, TextView gps,
                                     TextView type, TextView rider) {
            super(view);
            this.name = name;
            this.rider = rider;
            this.DateTime = DateTime;
            this.gps = gps;
            this.type = type;
        }
    }
}
