package com.example.ger534.interfazpruebas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;


public class MainActivity extends AppCompatActivity {

    //Ion.
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationManager locationManager;
    private LocationListener listener;
    private Location mCurrentLocation;
    private Task<LocationSettingsResponse> task;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = false;
    private TextView mTextMessage;
    private Switch mSwitch;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("ResourceType")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mSwitch.setVisibility(View.VISIBLE);
                    mTextMessage.setText("Concursos");
                    String[] myDataset = {"Costa Rica", "Nicaragua", "Panama"};
                    String[] myDataset2 = {"San Jose", "Cartago", "Heredia"};
                    mAdapter = new MyAdapter(myDataset);
                    mRecyclerView.setAdapter(mAdapter);
                    if(mSwitch.isChecked()){
                        mAdapter = new MyAdapter(myDataset);
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        mAdapter = new MyAdapter(myDataset2);
                        mRecyclerView.setAdapter(mAdapter);

                    }

                    return true;
                case R.id.navigation_notifications:
                    mSwitch.setVisibility(View.INVISIBLE);
                    mTextMessage.setText("Mensajes");
                    String[] myDataset3 = {"Segundo acertijo", "Primera pista de Primer certijo", "Primer acertijo"};
                    mAdapter = new MyAdapter(myDataset3);
                    mRecyclerView.setAdapter(mAdapter);
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);

        mSwitch = (Switch) findViewById(R.id.switchFiltro);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final String[] myDataset = {"Costa Rica", "Nicaragua", "Panama"};
        final String[] myDataset2 = {"San Jose", "Cartago", "Heredia"};

        mRecyclerView = (RecyclerView) findViewById(R.id.consursos_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        // TODO: NO DEBEN SER FINAL!!
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(mSwitch.isChecked()){
                    mAdapter = new MyAdapter(myDataset);
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter = new MyAdapter(myDataset2);
                    mRecyclerView.setAdapter(mAdapter);

                }
            }
        });


        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                try {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                    if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                        int position = recyclerView.getChildAdapterPosition(child);

                        Toast.makeText(MainActivity.this,"The Item Clicked is: "+ position ,Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        if(mTextMessage.getText().equals("Mensajes")){
                            builder.setMessage("lol")
                                    .setPositiveButton("lel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // FIRE ZE MISSILES!
                                        }
                                    })
                                    .setNegativeButton("lol no", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                        }else{
                            builder.setMessage("Personas trás de X acertijo: 3" +'\n' + "Personas que abandonaron el concurso: 4" +'\n')
                                    .setPositiveButton("Unirse al concurso", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // FIRE ZE MISSILES!
                                        }
                                    })
                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                        }
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();

                        return true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        final double[] y = new double[1];
        final double[] x = new double[1];
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    mCurrentLocation = location;
                    x[0] = location.getLongitude();
                    y[0] = location.getLatitude();
                    // Logic to handle location object
                    Toast.makeText(getApplicationContext(),
                            "Longitude: " + String.valueOf(location.getLongitude()) + " latitude: " + String.valueOf(location.getLatitude()),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        Ion.with(getApplicationContext())
                .load("http://172.19.50.141:3000/api/employees") //localhost:3000/db/ciudad?opc=1
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        // do stuff with the result or error
                        try {
                            Toast.makeText(getApplicationContext(),
                                    "salida de ion (server): " + result.toString(),
                                    Toast.LENGTH_LONG).show();
                            //result.get(0).toString();
                        }catch (Exception er){
                            System.out.println(result);
                            Toast.makeText(getApplicationContext(),
                                    "mae si vio esto, mamó: " +result,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(this);
        task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                builder.addLocationRequest(createLocationRequest());
                mRequestingLocationUpdates = true;
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(getApplicationContext(),
                            "wrong",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Toast.makeText(getApplicationContext(),
                            "longitude: " + String.valueOf(location.getLongitude()) + " latitude: " + String.valueOf(location.getLatitude()),
                            Toast.LENGTH_LONG).show();
                }
            };
        };
    }
    protected LocationRequest createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),
                "fuck "+mRequestingLocationUpdates,
                Toast.LENGTH_LONG).show();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null);
    }
    //https://www.movable-type.co.uk/scripts/latlong.html
    private void calculateRange(double lat1,double lat2,double lon1,double lon2){
        double R = 6371e3; // metres
        double phi1 = lat1 * 180/Math.PI;
        double phi2 = lat2 * 180/Math.PI;
        double deltaPhi = (lat2-lat1) * 180/Math.PI;
        double deltaLambda = (lon2-lon1) * 180/Math.PI;

        double a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaPhi/2) * Math.sin(deltaLambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = R * c;
    }



}


