package com.IDL2.childcontrolapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ConfigFetchingService extends Service {

    private LocalStorage localStorage;

    private DatabaseReference databaseReference;

    private ValueEventListener valueEventListener;

    public ConfigFetchingService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localStorage = new LocalStorage(this); // Initialize the LocalStorage instance
        databaseReference = FirebaseDatabase.getInstance("https://control-app-auth-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String,Long> appMap = dataSnapshot.getValue(Map.class);
                    Map<String, Long> modifiedMap = new HashMap<>();
                    for (Map.Entry<String, Long> entry : appMap.entrySet()) {
                        String key = entry.getKey();
                        switch (key) {
                            case "Youtube": key = "com.google.android.youtube"; break;
                            case "Twitch": key = "tv.twitch.android.app"; break;
                            case "Facebook": key = "com.facebook.katana"; break;
                            case "Jetpack": key = "com.halfbrick.jetpackjoyride"; break;
                        }
                        modifiedMap.put(key, entry.getValue());
                    }
                    Log.d("ConfigFetchingService", modifiedMap.toString());
                    localStorage.saveMap(appMap);
                }
                else Log.d("ConfigFetchingService", "No data available!");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e("ConfigFetchingService", "Error: " + error.getMessage());
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Do other work here...

        return START_STICKY;
    }

}