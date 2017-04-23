package com.sohamfit.sohamfitapp;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by leonardogedler on 4/23/17.
 */

public class SohamfitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(Constants.APP_ID)
                .server(Constants.SERVER_URL)
                .build()
        );
    }
}
