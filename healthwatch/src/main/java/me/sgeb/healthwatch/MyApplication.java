package me.sgeb.healthwatch;

import android.app.Application;

import com.testflightapp.lib.TestFlight;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize TestFlight with app token
        TestFlight.takeOff(this, new MySecretKeys().getTestFlightAppToken());
        TestFlight.passCheckpoint(MyCheckpoints.APP_STARTED);
    }

}
