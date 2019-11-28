package com.example.kargobike.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kargobike.util.RecyclerViewItemClickListener;

import java.util.List;
/**
 * Author: Samuel Pinto Da Silva
 * Creation date:
 * Last update date: 25.11.2019
 */
public class OrderAdapter<T> extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>
{
    private RecyclerViewItemClickListener mylistener;
    private List<T> data;

    public OrderAdapter(RecyclerViewItemClickListener listener) {
        mylistener = listener;
    }


    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView sender;
        private final TextView receiver;
        private final TextView status;
        private final TextView datePickup;
        private final TextView dateDeliv;
        private final TextView product;
        private final TextView productQty;
        private final TextView rider;


        private OrderViewHolder(View view, TextView sender, TextView receiver, TextView status, TextView datePickup,
                                TextView dateDeliv, TextView product, TextView productQty, TextView rider) {
            super(view);
            this.sender = sender;
            this.receiver = receiver;
            this.status = status;
            this.datePickup = datePickup;
            this.dateDeliv = dateDeliv;
            this.product = product;
            this.productQty = productQty;
            this.rider = rider;
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<T> orders){
        if(this.data == null){
            this.data = orders ;
            notifyItemRangeInserted(0, orders.size());
        }else{
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return data.size();
                }

                @Override
                public int getNewListSize() {
                    return orders.size();
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
            data = orders ;
            result.dispatchUpdatesTo(this);
        }
    }


}
