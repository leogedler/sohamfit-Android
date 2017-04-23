package com.sohamfit.sohamfitapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class DispatchActivity extends AppCompatActivity {

    public static final String TAG = DispatchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNetworkAvailable()){
            if (ParseUser.getCurrentUser() != null) {
                ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                        if (e == null) {

                            //Start an intent for the MainActivity in activity
                            Intent intent = new Intent(DispatchActivity.this, VideosList.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }else {

                            Log.d(TAG, e.getMessage());
                            ParseUser.logOut();
                            navigateToPreLaunch();

                        }
                    }
                });


            }else {
                navigateToPreLaunch();
            }

        }else {

            Log.e("No network", "sin internet");
            // No internet connection message
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setTitle(R.string.network_unavailable_tittle);
            dialog.setMessage(R.string.network_unavailable_message);
            dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();
        }

    }

    private void navigateToPreLaunch(){

        // Start and intent for the PreLaunch out activity
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

}
