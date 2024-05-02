package com.IDL2.childcontrolapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ControlConfigReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if the broadcast is for new data from the server
        if ("com.example.action.NEW_DATA".equals(intent.getAction())) {
            // Start a background service to fetch the list of app names
            Intent serviceIntent = new Intent(context, ConfigFetchingService.class);
            context.startService(serviceIntent);

        }
    }
}