package com.example.kitchenguard.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppRepository {
    private final AlertDao alertDao;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        alertDao = db.alertDao();
    }

    public void insertAlert(Alert alert) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            alertDao.insertAlert(alert);
        });
    }

    public LiveData<List<Alert>> getAlerts(String houseId) {
        return alertDao.getAlerts(houseId);
    }
}
