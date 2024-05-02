package com.IDL2.childcontrolapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Start your service when device boots up
            Intent serviceIntent = new Intent(context, BackgroundBlockingService.class);
            Log.d("BootReceiver", "BackgroundBlockingService Service started!");
            context.startService(serviceIntent);
        }
    }
}

