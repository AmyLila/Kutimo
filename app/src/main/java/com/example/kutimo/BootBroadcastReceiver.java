package com.example.kutimo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Sends a reminder notification outside of app's UI once boot is complete.
 *
 * @author Megan De Leon
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Prints reminder message as toast
        Toast.makeText(context, "Don't forget to read your scriptures", Toast.LENGTH_LONG).show();
    }
}