package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Calendar_checkmarks extends AppCompatActivity {
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_checkmarks);
        final String CHANNEL_ID = "";

button = findViewById(R.id.button_Iread);
button.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {
        HeadsUpNotification();
    }
});

    }

    private void HeadsUpNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
        .setContentText("Kutimo Scriptures")
                .setSmallIcon(R.drawable.kutimo)
                .setAutoCancel(true)
                .setContentText("You haven't been reading");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(9, builder.build());



    }
}