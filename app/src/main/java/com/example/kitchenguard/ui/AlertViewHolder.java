package com.example.kitchenguard.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kitchenguard.R;

class AlertViewHolder extends RecyclerView.ViewHolder {
    private final TextView message;
    private final TextView datetime;

    private AlertViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
        datetime = itemView.findViewById(R.id.datetime);
    }

    public void bind(String message, String datetime) {
        this.message.setText(message);
        this.datetime.setText(datetime);
    }

    static AlertViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert_list, parent, false);
        return new AlertViewHolder(view);
    }
}
