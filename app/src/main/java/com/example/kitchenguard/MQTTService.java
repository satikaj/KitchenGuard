package com.example.kitchenguard;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.kitchenguard.data.Alert;
import com.example.kitchenguard.data.AppRepository;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTService extends Service {
    String houseId;
    AppRepository repository;
    MqttAndroidClient client;
    String mqttTopic;
    NotificationManagerCompat notificationManager;
    String CHANNEL_ID = "KG_ALERTS";

    public MQTTService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences("userHouseId", MODE_PRIVATE);
        houseId = sharedPreferences.getString("houseId", "");
        mqttTopic = "KitchenGuard/" + houseId;
        connect();
        createNotificationChannel();

        repository = new AppRepository(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("RESET_SYSTEM")) {
                publish(mqttTopic, "System reset by user.");
                notificationManager.cancel(1);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    public void connect() {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this, "tcp://broker.emqx.io:1883", clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    subscribe(mqttTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @SuppressLint("MissingPermission")
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = new String(message.getPayload());
                repository.insertAlert(new Alert(
                        houseId,
                        msg,
                        new java.util.Date().toString()
                ));

                Intent intent = new Intent(getApplicationContext(), AlertsActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("Kitchen Guard Alert")
                        .setContentText(msg)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                if (msg.equals("Sensor failure. Restart system now.")) {
                    Intent resetActivityIntent = new Intent(getApplicationContext(), ResetActivity.class);
                    PendingIntent resetActivityPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resetActivityIntent, PendingIntent.FLAG_IMMUTABLE);
                    builder.setContentIntent(resetActivityPendingIntent);

                    Intent resetServiceIntent = new Intent(getApplicationContext(), MQTTService.class);
                    resetServiceIntent.setAction("RESET_SYSTEM");
                    PendingIntent resetServicePendingIntent = PendingIntent.getService(getApplicationContext(), 0, resetServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.addAction(android.R.drawable.stat_notify_sync, "RESET", resetServicePendingIntent);
                }

                notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(1, builder.build());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        try {
            client.publish(topic, message.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Alerts", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("KitchenGuard alerts");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}