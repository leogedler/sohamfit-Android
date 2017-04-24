package com.sohamfit.sohamfitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class Login extends AppCompatActivity {

    public static final String TAG = Login.class.getSimpleName();

    // Views
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mSignInButton;
    private Button mFacebookButton;
    private TextView mRegisterText;

    private String mEmail;
    private String mFirstName;
    private String mLastName;
    private String mFacebookId;

    protected Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mRegisterText = (TextView) findViewById(R.id.registerText);
        mFacebookButton = (Button) findViewById(R.id.facebook_register_button) ;

        // Sign in button
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // Register button
        mRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        // Facebook button
        mFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFacebookButtonClick();
            }
        });
    }

    private void attemptLogin() {

        mEmail = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mEmail = mEmail.trim();
        password = password.trim();

        if (mEmail.isEmpty() || password.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setMessage(R.string.login_error_message)
                    .setTitle(R.string.login_error_tittle)
                    .setPositiveButton(android.R.string.ok, null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            Login.this.progressDialog = ProgressDialog.show(Login.this, "", "Ingresando...", true);

            // Login a user using Parse SDK
            ParseUser.logInInBackground(mEmail, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {

                    if (e == null) {
                        //Start an intent for the Dispatch in activity
                        enterIntoDispatchActivity();
                    } else {

                        Login.this.progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage(R.string.login_error_credentials)
                                .setTitle(R.string.login_error_tittle)
                                .setPositiveButton(android.R.string.ok, null);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    // Login or sign-up using Facebook and parse SDK.
    private void onFacebookButtonClick() {


        Login.this.progressDialog = ProgressDialog.show(
                Login.this, "", "Ingresando...", true);
        List<String> permissions = Arrays.asList("public_profile", "user_about_me",
                "user_relationships", "user_birthday", "user_location", "email");


        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions,  new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Login.this.progressDialog.dismiss();
                    Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(TAG, "User signed up and logged in through Facebook!");

                    makeMeRequest();

                } else {
                    Log.d(TAG, "User logged in through Facebook!");
                    enterIntoDispatchActivity();
                }
            }
        });

    }


    private void makeMeRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (user != null) {

                            mFacebookId = user.optString("id");
                            mFirstName = user.optString("first_name");
                            mLastName = user.optString("last_name");
                            mEmail = user.optString("email");

                            // Up date current user information
                            ParseUser.getCurrentUser().put(Constants.FACEBOOK_ID, mFacebookId);
                            if (mEmail != null){
                                ParseUser.getCurrentUser().put(Constants.USERNAME, mEmail);
                                ParseUser.getCurrentUser().put(Constants.USER_EMAIL, mEmail);
                            }else {
                                ParseUser.getCurrentUser().put(Constants.USERNAME, mFacebookId);
                            }
                            if (mFirstName != null) {
                                ParseUser.getCurrentUser().put(Constants.USER_FIST_NAME, mFirstName);
                            }
                            if (mLastName != null) {
                                ParseUser.getCurrentUser().put(Constants.USER_LAST_NAME, mLastName);
                            }


                            Login.this.progressDialog.dismiss();

                            Login.this.progressDialog = ProgressDialog.show(Login.this, "", "Ingresando...", true);
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        enterIntoDispatchActivity();
                                    } else {
                                        Login.this.progressDialog.dismiss();
                                        Log.d("Error updating user", e.getMessage());
                                        Toast.makeText(Login.this, R.string.error_updating_user_info, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.LOGIN_RECOVERABLE)
                                    || (response.getError().getCategory() == FacebookRequestError.Category.LOGIN_RECOVERABLE)) {
                                Log.d(TAG, "The facebook session was invalidated.");

                                ParseUser.logOut();
                                Intent intent = new Intent(Login.this, Login.class);
                                startActivity(intent);

                            } else {
                                Log.d(TAG, "Some other error: " + response.getError().getErrorMessage()
                                );
                            }
                        }
                    }
                }
        );
        request.executeAsync();
    }

    private void enterIntoDispatchActivity() {
        Intent intent = new Intent(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
