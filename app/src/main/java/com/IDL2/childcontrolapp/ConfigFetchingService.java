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
        databaseReference = FirebaseDatabase.getInstance().getReference("app_config");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String,Long> appMap = dataSnapshot.getValue(Map.class);
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