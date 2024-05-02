package com.IDL2.childcontrolapp;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import java.util.Calendar;
import java.util.List;

public class UsageStatsHelper {

    public static long getUsageTimeInLast24Hours(Context context, String packageName) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager == null) {
            return 0; // UsageStatsManager is not available
        }

        // Get the beginning of the day in milliseconds (midnight)
        long startTime = getTodayMidnight();

        // Get the current time in milliseconds
        long endTime = System.currentTimeMillis();

        // Query usage statistics for the given time range
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        if (stats == null || stats.isEmpty()) {
            return 0; // No usage stats available
        }

        long totalUsageTime = 0;
        for (UsageStats usageStats : stats) {
            if (usageStats.getPackageName().equals(packageName)) {
                totalUsageTime += usageStats.getTotalTimeInForeground();
            }
        }

        return totalUsageTime;
    }

    private static long getTodayMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
