package com.example.kargobike.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kargobike.R;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.util.RecyclerViewItemClickListener;

import java.util.List;
/**
 * Author: Samuel Pinto Da Silva
 * Creation date: 25.11.2019
 * Last update date: 10.12.2019 ( by Patrick Garbely)
 */
public class OrderAdapter<T> extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>
{
    private RecyclerViewItemClickListener mylistener;
    private List<T> data;

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView customer;
        private final TextView fromAddress;
        private final TextView toAdress;
        //private final TextView product;
        private final TextView productQty;
        private final TextView dateDeliv;
        private final TextView timeDeliv;
        private final TextView status;


        private OrderViewHolder(View view, TextView customer, TextView fromAddress, /*TextView product*/
                                TextView productQty, TextView toAdress, TextView dateDeliv ,TextView timeDeliv, TextView status) {
            super(view);
            this.customer = customer;
            this.fromAddress = fromAddress;
            this.toAdress = toAdress;
            //this.product = product;
            this.productQty = productQty;
            this.status = status;
            this.dateDeliv = dateDeliv;
            this.timeDeliv = timeDeliv;

        }
    }

    public OrderAdapter(RecyclerViewItemClickListener listener) {
        mylistener = listener;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerviewitem_order, parent, false);

        final TextView twCustomer = v.findViewById(R.id.oCustomer);
        final TextView twFromAdress = v.findViewById(R.id.fromAddress);
        final TextView twToAdress = v.findViewById(R.id.toAddress);
        //final TextView twProduct = v.findViewById(R.id.oProduct);
        final TextView twQty =  v.findViewById(R.id.oQty);
        final TextView twDateDelivery = v.findViewById(R.id.oDateDelivery);
        final TextView twTimeDelivery = v.findViewById(R.id.oTimeDelivery);
        final TextView twStatus = v.findViewById(R.id.oStatus);

        final OrderViewHolder OrderViewHolder = new OrderViewHolder(v, twCustomer, twFromAdress, /*twProduct*/ twQty, twToAdress,twDateDelivery, twTimeDelivery, twStatus);
        v.setOnClickListener(view -> mylistener.onItemClick(view, OrderViewHolder.getAdapterPosition()));
        v.setOnClickListener(view -> {
            mylistener.onItemLongClick(view, OrderViewHolder.getAdapterPosition());
        });
        return OrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        T item = data.get(position);
        holder.customer.setText(((Order) item).getCustomer());
        holder.fromAddress.setText(((Order) item).getFromAddress());
        holder.toAdress.setText(((Order) item).getToAddress());
        //holder.product.setText(((Order) item).getProduct());
        holder.productQty.setText(""+((Order) item).getHowMany());
        holder.dateDeliv.setText(((Order) item).getDateDelivery());
        holder.timeDeliv.setText(((Order) item).getTimeDelivery());
        holder.status.setText(((Order) item).getState());
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
