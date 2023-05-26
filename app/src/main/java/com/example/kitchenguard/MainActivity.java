package com.example.kitchenguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.kitchenguard.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent joinIntent = new Intent(MainActivity.this, AlertsActivity.class);

        SharedPreferences sharedPreferences = getSharedPreferences("userHouseId", MODE_PRIVATE);

        if (sharedPreferences.getString("houseId", null) != null) {
            startActivity(joinIntent);
        }

        binding.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String houseId = binding.houseId.getText().toString();
                if (houseId.isEmpty() || houseId.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter your house ID.", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("houseId", houseId);
                    editor.apply();

                    startActivity(joinIntent);
                }
            }
        });
    }
}