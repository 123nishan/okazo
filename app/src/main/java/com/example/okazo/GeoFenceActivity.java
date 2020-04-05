package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.util.GeofenceBroadcastReceiver;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.util.Log.d;
import static com.example.okazo.util.constants.ERROR_DIALOG_REQUEST;
import static com.example.okazo.util.constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class GeoFenceActivity extends FragmentActivity implements OnMapReadyCallback, GeoQueryEventListener {

    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private LocationCallback locationCallBack;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentUser;
    private boolean mLocationPermissionGranted=false;
    private DatabaseReference myLocationRef;
    private GeoFire geoFire;
    private List<LatLng> area;
    private  PendingIntent geofencePendingIntent;
    private GeofencingClient geofencingClient;
    private ApiInterface apiInterface;
    ArrayList<Geofence> geofenceslist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.getGeofenceStatus().enqueue(new Callback<ArrayList<com.example.okazo.Model.Geofence>>() {
            @Override
            public void onResponse(Call<ArrayList<com.example.okazo.Model.Geofence>> call, Response<ArrayList<com.example.okazo.Model.Geofence>> response) {
                ArrayList<com.example.okazo.Model.Geofence> geofences=response.body();

                for ( com.example.okazo.Model.Geofence status:geofences
                     ) {
                    if(status.getStatus()==0){
                        Toast.makeText(GeoFenceActivity.this, "GEO OFF", Toast.LENGTH_SHORT).show();
                    }else
                    {
                       // Toast.makeText(GeoFenceActivity.this, "GGEO ON", Toast.LENGTH_SHORT).show();
                        geofencingClient=LocationServices.getGeofencingClient(GeoFenceActivity.this);
                        String key=""+27.680438+"-"+85.335270;
                        geofencePendingIntent=null;
                        geofenceslist=new ArrayList<>();
                        //Geofence geofence=getGeofence(27.680438,85.335270,key);
                        geofenceslist.add(new Geofence.Builder()
                                .setRequestId(""+27.680438+"-"+85.335270)
                                .setCircularRegion(
                                        27.680438,85.335270,100
                                )
                                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_DWELL)
                                //.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                                .setLoiteringDelay(10000)
                                .build()
                        );
                        geofencingClient.addGeofences(getGeofencingRequest(geofenceslist),getGeofencePendingIntent())
                                .addOnSuccessListener(GeoFenceActivity.this, new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("hello","geoAdded");
                                    }
                                }).addOnFailureListener(GeoFenceActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GeoFenceActivity.this, "Failed to add GEO FENCE"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        //TODO permission check for android 10 or greater OF ACCESS_BACKGROUND_LOCATION
                        //addGeofence(geofencingClient,geofenceslist);
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<com.example.okazo.Model.Geofence>> call, Throwable t) {

            }
        });

        if(isServicesOK()) {

//            if(mLocationPermissionGranted){
//
//
//            }else {
//                getLocationPermission();
//            }


            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
//                            buildLocationRequest();
//                            buildLocationCallBack();
                            // Toast.makeText(GeoFenceActivity.this, "Granted", Toast.LENGTH_SHORT).show();
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GeoFenceActivity.this);
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(GeoFenceActivity.this);
//                            initializeArea();
//                            settingGeoFire();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if(response.isPermanentlyDenied()){
                                showSettingDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


    }
    private void addGeofence(GeofencingClient geofencingClient, ArrayList<Geofence> geofenceslist){
        geofencingClient.addGeofences(getGeofencingRequest(geofenceslist),getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("hello","geoAdded");
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GeoFenceActivity.this, "Failed to add GEO FENCE"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Geofence getGeofence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, 100)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)
                .build();
    }
    // for geofence

    private GeofencingRequest getGeofencingRequest(ArrayList<Geofence> geofenceslist){
        GeofencingRequest.Builder builder=new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceslist);
        return builder.build();

    }
    private PendingIntent getGeofencePendingIntent(){
        if(geofencePendingIntent!=null){
            return geofencePendingIntent;
        }

        Intent intent=new Intent(GeoFenceActivity.this, GeofenceBroadcastReceiver.class);
        geofencePendingIntent= PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;

    }
    //for geofencing

    private void initializeArea() {
        area=new ArrayList<>();
        area.add(new LatLng(27.679585,85.334419));
        area.add(new LatLng(27.680003,85.337136));
    }

    private void settingGeoFire() {
        myLocationRef= FirebaseDatabase.getInstance().getReference("MyLocation");
        geoFire=new GeoFire(myLocationRef);
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GeoFenceActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
//            getChatrooms();
            //getLastKnowLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void buildLocationRequest(){
        locationRequest=new LocationRequest()
;
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    locationRequest.setInterval(5000);
    locationRequest.setFastestInterval(3000);
    locationRequest.setSmallestDisplacement(10f);
    }
    private void buildLocationCallBack(){
        locationCallBack=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(mMap!=null){

                geoFire.setLocation("you", new GeoLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if(currentUser!=null)currentUser.remove();
                        currentUser=mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()))
                                .title("YOU"));
                        mMap.animateCamera(CameraUpdateFactory
                                .newLatLngZoom(currentUser.getPosition(),12.0f));
                    }
                })

                ;
                }
            }
        };

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

//        if (fusedLocationProviderClient != null)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//                    return;
//                }
//
////        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            }
//        assert fusedLocationProviderClient != null;
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper());
//        //add circle for geofence
//        for(LatLng latLng:area){
//            mMap.addCircle(new CircleOptions().center(latLng).radius(500)//500m
//                    .strokeColor(Color.BLUE)
//                    .fillColor(0x220000FF)
//                    .strokeWidth(5.0f) );
//            //create geoquery when user is in the location
//            GeoQuery geoQuery=geoFire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude),0.5f);
//            geoQuery.addGeoQueryEventListener(GeoFenceActivity.this);
//        }
    }

    @Override
    protected void onStop() {
       // fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
        super.onStop();
    }
    public boolean isServicesOK(){
        d("map", "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(GeoFenceActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            d("map", "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            d("map", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(GeoFenceActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        sendNotification("hello",String.format("is entered",key));
    }

    @Override
    public void onKeyExited(String key) {
        sendNotification("hello",String.format(" left",key));
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        //sendNotification("hello",String.format(" moving",key));
    }

    private void sendNotification(String title, String content) {
        String NOTIFICATION_CHANNEL_ID="location";
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("chaneel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);


        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        Notification notification=builder.build();
        notificationManager.notify(new Random().nextInt(),notification);

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        mLocationPermissionGranted = false;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mLocationPermissionGranted = true;
//                }
//            }
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        d("map", "onActivityResult: called.");
//        switch (requestCode) {
//            case PERMISSION_REQUEST_ENABLE_GPS: {
//                if(mLocationPermissionGranted){
////                    getChatrooms();
//                    //getLastKnowLocation();
//                    buildLocationRequest();
//                    buildLocationCallBack();
//                    // Toast.makeText(GeoFenceActivity.this, "Granted", Toast.LENGTH_SHORT).show();
//                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GeoFenceActivity.this);
//                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                            .findFragmentById(R.id.map);
//                    mapFragment.getMapAsync(GeoFenceActivity.this);
//                }
//                else{
//                    getLocationPermission();
//                }
//            }
//        }
//
//    }
}
