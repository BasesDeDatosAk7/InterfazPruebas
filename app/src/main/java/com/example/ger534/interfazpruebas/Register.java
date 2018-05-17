package com.example.ger534.interfazpruebas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mCountryView;
    private EditText mCityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the register form.
        mEmailView = (EditText) findViewById(R.id.emailRegistro);
        mPasswordView = (EditText) findViewById(R.id.passwordRegistro);
        mCountryView = (EditText) findViewById(R.id.countryRegistro);
        mCityView = (EditText) findViewById(R.id.cityRegistro);

        findViewById(R.id.buttonSendRegister).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(view.getContext(), MainActivity.class);
                //startActivityForResult(intent, 0);
                attemptRegister();
            }
        });

    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            //showProgress(true);

            UserRegisterTask(email, password);

        }
    }

    void UserRegisterTask(String email, String password) {
        String message;
        JsonObject json = new JsonObject();
        try {
            json.addProperty("email", email);
            json.addProperty("password", password);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        message = json.toString();
        Toast.makeText(getApplicationContext(),
                "json: " +message,
                Toast.LENGTH_LONG).show();

        Ion.with(getApplicationContext())
                //.load("http://172.19.50.141:3000/api/employees")
                .load("http://172.19.50.141:3000/api/usuario")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        try {
                            Toast.makeText(getApplicationContext(),
                                    "salida de ion (server): " + result.toString(),
                                    Toast.LENGTH_LONG).show();
                        }catch (Exception er){
                            System.out.println(result);
                            Toast.makeText(getApplicationContext(),
                                    "mae si vio esto, mamó: " +result,
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
}
