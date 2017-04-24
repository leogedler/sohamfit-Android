package com.sohamfit.sohamfitapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by leonardogedler on 4/23/17.
 */

public class SohamfitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Parse SDK
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(Constants.APP_ID)
                .server(Constants.SERVER_URL)
                .build()
        );
        ParseFacebookUtils.initialize(this);
    }
}
