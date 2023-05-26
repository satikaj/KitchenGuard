package com.example.kitchenguard.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlertDao {
    @Insert
    public void insertAlert(Alert alert);

    @Query("SELECT * FROM Alert WHERE houseId = :houseId")
    public LiveData<List<Alert>> getAlerts(String houseId);
}
