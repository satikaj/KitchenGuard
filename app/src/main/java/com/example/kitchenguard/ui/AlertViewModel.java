package com.example.kitchenguard.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.kitchenguard.data.Alert;
import com.example.kitchenguard.data.AppRepository;

import java.util.List;

public class AlertViewModel extends AndroidViewModel {

    private final AppRepository repository;

    public AlertViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public void insertAlert(Alert alert) {
        repository.insertAlert(alert);
    }

    public LiveData<List<Alert>> getAlerts(String houseId) {
        return repository.getAlerts(houseId);
    }
}
