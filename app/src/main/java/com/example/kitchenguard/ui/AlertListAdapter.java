package com.example.kitchenguard.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.kitchenguard.data.Alert;

public class AlertListAdapter extends ListAdapter<Alert, AlertViewHolder> {

    public AlertListAdapter(@NonNull DiffUtil.ItemCallback<Alert> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AlertViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(AlertViewHolder holder, int position) {
        Alert current = getItem(position);
        holder.bind(current.getMessage(), current.getDatetime());
    }

    public static class AlertDiff extends DiffUtil.ItemCallback<Alert> {

        @Override
        public boolean areItemsTheSame(@NonNull Alert oldItem, @NonNull Alert newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Alert oldItem, @NonNull Alert newItem) {
            return oldItem.getMessage().equals(newItem.getMessage());
        }
    }
}
