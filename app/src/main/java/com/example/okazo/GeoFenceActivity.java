package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.GeofenceBroadcastReceiver;
import com.example.okazo.util.RewardAdapter;
import com.example.okazo.util.constants;
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
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.util.Log.d;
import static com.example.okazo.util.constants.ERROR_DIALOG_REQUEST;
import static com.example.okazo.util.constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class GeoFenceActivity extends FragmentActivity implements GeoQueryEventListener {

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
    private String userId,userEmail;
    private EventDetail eventDetail;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ArrayList<String> arrayListGeofenceId=new ArrayList<>(),arrayListEventId=new ArrayList<>(),arrayListReward=new ArrayList<>(),arrayListTitle=new ArrayList<>(),
        arrayListLatitude=new ArrayList<>(),arrayListLongitude=new ArrayList<>();
    private ArrayList<String> rewardImage,rewardTitle,rewardDate,rewardAmount;
    private RecyclerView recyclerView;
    private TextView textViewError;
    private RewardAdapter rewardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence);
        toolbar= findViewById(R.id.geofence_toolbar);
        recyclerView=findViewById(R.id.geofence_recyclerview);
        textViewError=findViewById(R.id.geofence_error);
        toolbar.setTitle("Rewards");
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(constants.KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences1.getString("user_id","")!=null && !sharedPreferences1.getString("user_id","").isEmpty()) {
            userId = sharedPreferences1.getString("user_id", "");


            getAllReward(userId);


            apiInterface.getGeofenceStatus(userId).enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){
                        eventDetail=apiResponse.getEvent();
                        arrayListEventId=eventDetail.getEventIdArray();
                        arrayListTitle=eventDetail.getTitleArray();
                        arrayListGeofenceId=eventDetail.getGeoIdArray();
                        arrayListLatitude=eventDetail.getLatitudeArray();
                        arrayListLongitude=eventDetail.getLongitudeArray();
                        arrayListReward=eventDetail.getRewardArray();

                        geofencingClient=LocationServices.getGeofencingClient(GeoFenceActivity.this);
                        geofenceslist=new ArrayList<>();
                        geofencePendingIntent=null;
                        for(int i=0;i<arrayListGeofenceId.size();i++){
                            //create geofence

                            String key=arrayListEventId.get(i);
                            Log.d("KEY",key);
                            geofenceslist.add(new Geofence.Builder()
                                    .setRequestId(key)
                                    .setCircularRegion(
                                            Double.valueOf(arrayListLatitude.get(i)),Double.valueOf(arrayListLongitude.get(i)),300
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
                                            Log.d("GEOCHECK","geoAdded");
                                        }
                                    }).addOnFailureListener(GeoFenceActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GeoFenceActivity.this, "Failed to add GEO FENCE"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            geofenceslist.clear();
                        }

                        // Log.d("geofence",eventDetail.getGeoIdArray().get(0));
                    }else {
                        if(apiResponse.getErrorMsg().equals("No Reward")){
                            DynamicToast.makeWarning(getApplicationContext(),"you dont have any reward to claim").show();
                        }else {
                            DynamicToast.makeError(getApplicationContext(),apiResponse.getErrorMsg()).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
        }else {
            DynamicToast.makeError(getApplicationContext(),"Something went wrong,Please login again").show();
            SharedPreferences.Editor editor = sharedPreferences1.edit();
//            editor.putString("user_email","");
//            editor.putString("user_id","");
            editor.remove("user_email");
            editor.remove("user_id");
            editor.commit();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }



                // Obtain the SupportMapFragment and get notified when the map is ready to be used.


    }

    private void getAllReward(String userId) {
        apiInterface.getAllReward(userId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    rewardAmount=apiResponse.getEvent().getRewardArray();
                    rewardDate=apiResponse.getEvent().getStartDateArray();
                    rewardImage=apiResponse.getEvent().getImageArray();
                    rewardTitle=apiResponse.getEvent().getTitleArray();
                    rewardAdapter=new RewardAdapter(rewardImage,rewardTitle,rewardDate,rewardAmount,getApplicationContext());
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(rewardAdapter);

                }else {
                    textViewError.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
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
        Log.d("RETURN","HERER");
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








    @Override
    protected void onStop() {
       // fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
        super.onStop();
    }


    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        sendNotification("GG",String.format("is entered",key));
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
        Log.d("NOTIFIACTION",content);
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

}
