package com.example.kitchenguard.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Alert {
    @PrimaryKey(autoGenerate = true)
    private int alertId;
    private String houseId;
    private String message;
    private String datetime;

    public Alert(String houseId, String message, String datetime) {
        this.houseId = houseId;
        this.message = message;
        this.datetime = datetime;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public String getHouseId() {
        return houseId;
    }

    public String getMessage() {
        return message;
    }

    public String getDatetime() {
        return datetime;
    }
}
