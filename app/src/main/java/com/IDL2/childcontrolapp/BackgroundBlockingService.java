package com.IDL2.childcontrolapp;


import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class BackgroundBlockingService extends AccessibilityService {

    private LocalStorage localStorage;

    private UsageStatsManager usageStatsManager;

    public BackgroundBlockingService() {
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        localStorage = new LocalStorage(this);
        usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Log.d("BlockingService", "BackgroundBlocking service connected to system!");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Check if the event corresponds to a foreground application change
            Log.d("BlockingService", "Accessibility event triggered");
            // Get the package name of the current foreground application
            String packageName = event.getPackageName().toString();

            // Retrieve the list of application names and usage time from local storage
            Map<String, Long> appData = localStorage.getMap();
            Log.d("BlockingService", appData.toString());

            // Check if the current hour is later than the bedtime key value in the list
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (appData.containsKey("bedtime") && hour >= appData.get("bedtime")) {
                Log.d("BlockingService", "Bedtime Reached!");
                performCloseAction();
            }

            // Check if the current application is in the list and meets the usage time criteria
            if (appData.containsKey(packageName) && meetsUsageTimeCriteria(packageName, appData.get(packageName))) {
                Log.d("BlockingService", "Allowed usage time for "+ packageName +" Reached!");
                performCloseAction();
            }
    }

    private boolean meetsUsageTimeCriteria(String packageName, long usageCap) {
        // Implement your criteria, such as checking if the usage time exceeds a certain threshold
        return usageCap <= this.getUsageTime(packageName);
    }

    private long getUsageTime(String packageName) {
        long currentTime = System.currentTimeMillis();

        // Set the start and end time for the usage stats query
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1); // Get usage stats for the past 24 hours
        long startTime = calendar.getTimeInMillis();

        // Get the list of usage stats for the specified time range
        List<UsageStats> usageStatsList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, currentTime);
        } else {
            usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, currentTime);
        }

        // Iterate through the usage stats to find the one corresponding to the specified package name
        if (usageStatsList != null) {
            for (UsageStats usageStats : usageStatsList) {
                if (packageName.equals(usageStats.getPackageName())) {
                    return usageStats.getTotalTimeInForeground();
                }
            }
        }

        return 0; // Return 0 if usage stats are not available for the specified package name
    }

    @Override
    public void onInterrupt() {
        // Log information about the interruption
        Log.d("BlockingService", "Accessibility service interrupted");
    }

    private void performCloseAction() {
        // Simulate a "back" key press to close the current activity
        Log.d("BlockingService", "BackgroundBlockingService Triggered!");
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start any initialization code or background tasks here
        return START_STICKY; // Or other appropriate return value
    }

}