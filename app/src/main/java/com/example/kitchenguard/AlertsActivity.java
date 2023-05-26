package com.example.kitchenguard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.kitchenguard.databinding.ActivityAlertsBinding;
import com.example.kitchenguard.ui.AlertListAdapter;
import com.example.kitchenguard.ui.AlertViewModel;

public class AlertsActivity extends AppCompatActivity {

    ActivityAlertsBinding binding;
    String houseId;
    AlertViewModel alertViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlertsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("userHouseId", MODE_PRIVATE);
        houseId = sharedPreferences.getString("houseId", "");

        RecyclerView recyclerView = binding.alertRV;
        AlertListAdapter adapter = new AlertListAdapter(new AlertListAdapter.AlertDiff());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        alertViewModel = new ViewModelProvider(this).get(AlertViewModel.class);
        alertViewModel.getAlerts(houseId).observe(this, alerts -> {
            adapter.submitList(alerts);
        });

        Intent mqttServiceIntent = new Intent(AlertsActivity.this, MQTTService.class);
        startService(mqttServiceIntent);

        binding.leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(mqttServiceIntent);
                sharedPreferences.edit().clear().apply();
                finish();
            }
        });
    }
}