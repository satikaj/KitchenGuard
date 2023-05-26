package com.example.kitchenguard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kitchenguard.databinding.ActivityResetBinding;

public class ResetActivity extends AppCompatActivity {

    ActivityResetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetServiceIntent = new Intent(ResetActivity.this, MQTTService.class);
                resetServiceIntent.setAction("RESET_SYSTEM");
                startService(resetServiceIntent);
                finish();
            }
        });
    }
}