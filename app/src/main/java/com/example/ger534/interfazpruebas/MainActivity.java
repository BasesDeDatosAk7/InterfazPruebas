package com.example.ger534.interfazpruebas;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class MainActivity extends AppCompatActivity {

    //Ion.
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManager2;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationManager locationManager;
    private LocationListener listener;
    private Location mCurrentLocation;
    private Task<LocationSettingsResponse> task;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = false;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] myDataset = {"Cheese", "Pepperoni", "Black Olives"};
        String[] myDataset2 = {"Fredo", "sos", "Groso"};

        mRecyclerView = (RecyclerView) findViewById(R.id.consursos_list);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.mensajes_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView2.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManager2 = new LinearLayoutManager(this);
        mRecyclerView2.setLayoutManager(mLayoutManager2);


        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter2 = new MyAdapter(myDataset2);
        mRecyclerView2.setAdapter(mAdapter2);


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
        //Toast.makeText(getApplicationContext(), "Longitude: " + String.valueOf(x[0]) + " latitude: " + String.valueOf(y[0]), Toast.LENGTH_LONG).show();

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
                    // Update UI with location data
                    // ...
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


