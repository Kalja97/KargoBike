package com.example.kargobike.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kargobike.R;
import com.example.kargobike.util.RecyclerViewItemClickListener;

import org.w3c.dom.Text;

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

        private final TextView product;
        private final TextView productQty;

        //private final TextView rider;
        private final TextView datePickup;
        private final TextView dateDeliv;

        private final TextView status;


        private OrderViewHolder(View view, TextView sender, TextView receiver, TextView product,
                                TextView productQty,TextView datePickup, TextView dateDeliv, TextView status) {
            super(view);
            this.sender = sender;
            this.receiver = receiver;
            this.status = status;
            this.datePickup = datePickup;
            this.dateDeliv = dateDeliv;
            this.product = product;
            this.productQty = productQty;
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerviewitem_order, parent, false);

//        private String orderNr;
//
//        private String sender;
//        private String receiver;
//
//        private String product;
//        private int howMany;
//
//        private String rider;
//        private String datePickup;
//        private String dateDelivery;
//
//        private String state;

        final TextView twSender = v.findViewById(R.id.sender);
        final TextView twreceiver = v.findViewById(R.id.receiver);
        final TextView twProduct = v.findViewById(R.id.oProduct);
        final TextView twQty =  v.findViewById(R.id.oQty);
        final TextView twDatePick = v.findViewById(R.id.oDatePickup);
        final TextView twDateDeliv = v.findViewById(R.id.oDateDeliv);
        final TextView twStatus = v.findViewById(R.id.oStatus);

        final OrderViewHolder OrderViewHolder = new OrderViewHolder(v, twSender, twreceiver, twProduct, twQty, twDatePick, twDateDeliv, twStatus);
        v.setOnClickListener(view -> mylistener.onItemClick(view, OrderViewHolder.getAdapterPosition()));
        v.setOnClickListener(view -> {
            mylistener.onItemLongClick(view, OrderViewHolder.getAdapterPosition());
        });
        return OrderViewHolder;
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
