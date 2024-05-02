package com.IDL2.childcontrolapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.List;

public class ConfigFetchingService extends IntentService {

    private LocalStorage localStorage;

    public ConfigFetchingService() {
        super("ConfigFetchingService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localStorage = new LocalStorage(this); // Initialize the LocalStorage instance
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Fetch the list of app names from the server
        List<String> appNames = fetchAppNamesFromServer();

        // Store the list of app names locally (e.g., in SharedPreferences or a local database)
        saveAppNamesLocally(appNames);
    }

    private void saveAppNamesLocally(List<String> appNames) {
    }

    private List<String> fetchAppNamesFromServer() {
        return null;
    }
}