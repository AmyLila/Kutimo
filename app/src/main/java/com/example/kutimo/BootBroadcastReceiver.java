package com.example.kutimo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast for message once boot is complete.
        Toast.makeText(context, "Don't forget to read your scriptures", Toast.LENGTH_LONG).show();


    }
}