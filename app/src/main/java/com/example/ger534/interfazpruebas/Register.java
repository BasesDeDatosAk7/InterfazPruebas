package com.example.ger534.interfazpruebas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Register extends AppCompatActivity {
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;
    private String mCountry;
    private String mCity;
    private RecyclerView.Adapter mAdapter;
    boolean spinnerTouched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the register form.
        mEmailView = (EditText) findViewById(R.id.emailRegistro);
        mPasswordView = (EditText) findViewById(R.id.passwordRegistro);
        mNameView = (EditText) findViewById(R.id.nameRegistro);

        findViewById(R.id.buttonSendRegister).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
                }
        });

        final List myDataset = new ArrayList();
        myDataset.add("País");

        Ion.with(getApplicationContext())
                .load("http://192.168.100.17:3000/db/getPaises")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error
                        try {

                            Toast.makeText(getApplicationContext(),
                                    "salida de ion (server): " + result.getAsJsonArray().size(),
                                    Toast.LENGTH_LONG).show();
                            JsonArray json = result.getAsJsonArray();
                            for(int i = 0; i < result.getAsJsonArray().size(); i++){
                                String pais = result.getAsJsonArray().get(i).getAsJsonObject().get("COLUMN_VALUE").toString();
                                pais = pais.substring(1);
                                pais = pais.substring(0, pais.length() - 1);
                                myDataset.add(pais);
                            }

                        }catch (Exception er){
                            Toast.makeText(getApplicationContext(),
                                    "mae si vio esto, mamó: " +result,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

        final List myDataset2 = new ArrayList();
        myDataset2.add("Ciudad");




        Spinner spinner = (Spinner) findViewById(R.id.spinnerCountries);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, myDataset);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerTouched = true; // User DID touched the spinner!
                }

                return false;
            }
        });
        
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (spinnerTouched) {
                    // Do something
                    Toast.makeText(getApplicationContext(),
                            "country: " + myDataset.get(position),
                            Toast.LENGTH_LONG).show();
                    mCountry = myDataset.get(position).toString();

                    JsonObject json = new JsonObject();
                    try {
                        json.addProperty("pais", mCountry);
                    } catch (JsonIOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(json);

                    Ion.with(getApplicationContext())
                            .load("http://192.168.100.17:3000/db/getCiudades")
                            .setJsonObjectBody(json)
                            .asJsonArray()
                            .setCallback(new FutureCallback<JsonArray>() {
                                @Override
                                public void onCompleted(Exception e, JsonArray result) {
                                    // do stuff with the result or error
                                    try {
                                        for(int i = 0; i < result.getAsJsonArray().size(); i++) {
                                            String ciudad = result.getAsJsonArray().get(i).getAsJsonObject().get("COLUMN_VALUE").toString();
                                            ciudad = ciudad.substring(1);
                                            ciudad = ciudad.substring(0, ciudad.length() - 1);
                                            myDataset2.add(ciudad);
                                        }
                                    }catch (Exception er){
                                        System.out.println(er);
                                        Toast.makeText(getApplicationContext(),
                                                "No hay conexión",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            });

                }
                else {
                    // Do something else
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerCities);


        spinner2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerTouched = true; // User DID touched the spinner!
                }

                return false;
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (spinnerTouched) {
                    // Do something
                    Toast.makeText(getApplicationContext(),
                            "city: " + myDataset2.get(position),
                            Toast.LENGTH_LONG).show();
                    mCity = myDataset2.get(position).toString();
                }
                else {
                    // Do something else
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, myDataset2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);


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
        String name = mNameView.getText().toString();
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

            //UserRegisterTask(name, email, password, mCountry, mCity);
            UserRegisterTask(name, email, password, mCountry, mCity);


        }
    }

    void UserRegisterTask(String name, String email, String password,String country,String city) {
        String message;
        JsonObject json = new JsonObject();
        try {
            json.addProperty("nombre", name);
            json.addProperty("email", email);
            json.addProperty("clave", password);
            json.addProperty("ciudad", city);

        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        message = json.toString();
        Toast.makeText(getApplicationContext(),
                "json: " +message,
                Toast.LENGTH_LONG).show();

        Ion.with(getApplicationContext())
                .load("http://192.168.100.17:3000/db/newUsuario")
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
                            if(result.get("retorno").toString().equals("\"true\"")) {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
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

