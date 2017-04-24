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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Register extends AppCompatActivity {

    // Views
    protected EditText mFistNameView;
    protected EditText mLastNameView;
    protected EditText mPasswordView;
    protected EditText mEmailView;
    protected Button mRegisterButton;
    protected String mPassword;
    protected String mEmail;
    protected Dialog progressDialog;
    protected String mFacebookId;
    protected String mFirstName;
    protected String mLastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Views
        mPasswordView = (EditText)findViewById(R.id.password);
        mEmailView = (EditText)findViewById(R.id.email);
        mFistNameView = (EditText) findViewById(R.id.firstName);
        mLastNameView = (EditText) findViewById(R.id.lastName);
        mRegisterButton = (Button)findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirstName = mFistNameView.getText().toString();
                mLastName = mLastNameView.getText().toString();
                mEmail = mEmailView.getText().toString();
                mPassword = mPasswordView.getText().toString();

                mPassword = mPassword.trim();
                mEmail = mEmail.trim();
                mFirstName = mFirstName.toLowerCase().trim();
                mLastName = mLastName.toLowerCase().trim();

                if (mPassword.isEmpty() || mEmail.isEmpty() || mFirstName.isEmpty() || mLastName.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage(R.string.sign_up_error_message_data)
                            .setTitle(R.string.sign_up_error_tittle)
                            .setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {


                    Register.this.progressDialog = ProgressDialog.show(Register.this, "", "Registrando...", true);

                    // Create the new user and sign-up using Parse SDK
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(mEmail);
                    newUser.setPassword(mPassword);
                    newUser.setEmail(mEmail);
                    newUser.put(Constants.USER_FIST_NAME, mFirstName);
                    newUser.put(Constants.USER_LAST_NAME, mLastName);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null){

                                // Start an intent for the Dispatch in activity
                                enterIntoDispatchActivity();
                            }
                            else {

                                Register.this.progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.sign_up_error_tittle)
                                        .setPositiveButton(android.R.string.ok, null);

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        }
                    });

                }

            }
        });

    }

    private void enterIntoDispatchActivity() {
        Intent intent = new Intent(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
