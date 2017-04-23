package com.sohamfit.sohamfitapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mSignInButtonView;

    protected Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mSignInButtonView = (Button) findViewById(R.id.email_sign_in_button);

        // Sign in button
        mSignInButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        email = email.trim();
        password = password.trim();

        if (email.isEmpty() || password.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setMessage(R.string.login_error_message)
                    .setTitle(R.string.login_error_tittle)
                    .setPositiveButton(android.R.string.ok, null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            Login.this.progressDialog = ProgressDialog.show(Login.this, "", "Ingresando...", true);
            // login a user
            ParseUser.logInInBackground(email, password, new LogInCallback() {
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

    private void enterIntoDispatchActivity() {
        Intent intent = new Intent(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
