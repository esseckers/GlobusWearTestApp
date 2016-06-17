package com.danilov.ivan.commonlibrary;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;


/**
 * Created by Ivan Danilov on 17.06.2016.
 * Email: i.danilov@globus-ltd.com
 */
public class TheApplication extends Application {
    private static final String IS_ROUND = "is_round";
    private static TheApplication sApplication;
    private GoogleApiClient googleClient;
    private SharedPreferences sharedPreferences;

    public static TheApplication getInstance() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public SharedPreferences getPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        return sharedPreferences;
    }

    public GoogleApiClient getGoogleClient() {
        if (googleClient == null) {
            // Build a new GoogleApiClient that includes the Wearable API
            googleClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)

                    .build();
            googleClient.connect();
        }
        return googleClient;
    }

    public boolean isRound() {
        return getPreferences().getBoolean(IS_ROUND, false);
    }

    public void setRound(boolean userName) {
        getPreferences().edit().putBoolean(IS_ROUND, userName).apply();
    }
}
