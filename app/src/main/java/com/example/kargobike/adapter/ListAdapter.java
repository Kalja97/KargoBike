package com.example.kargobike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kargobike.R;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ListAdapter<T> extends ArrayAdapter<T> {
    private int resource;
    private List<T> data;


    public ListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> data) {
        super(context, resource, data);
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public T getItem(int position) {
        return data.get(position);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.RowList);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        T item = getItem(position);
        if (item != null) {
            viewHolder.itemView.setText(item.toString());
        }
        return convertView;
    }

    public void updateData(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView itemView;
    }
}