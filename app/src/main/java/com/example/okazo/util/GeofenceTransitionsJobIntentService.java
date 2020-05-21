package com.example.okazo.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.GeoFenceActivity;
import com.example.okazo.MainActivity;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_USER_ID;

public class GeofenceTransitionsJobIntentService extends JobIntentService {
    private static final int JOB_ID = 573;
    private static final String TAG = "GeofenceTransitionsIS";
    private ApiInterface apiInterface;

    private static final String CHANNEL_ID = "channel_01";

    private String userId,userEmail;
    private EventDetail eventDetail;
    private String eventId;
    private ArrayList<String> arrayListGeofenceId=new ArrayList<>(),arrayListEventId=new ArrayList<>(),arrayListReward=new ArrayList<>(),arrayListTitle=new ArrayList<>(),
            arrayListLatitude=new ArrayList<>(),arrayListLongitude=new ArrayList<>();
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, GeofenceTransitionsJobIntentService.class, JOB_ID, intent);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            return;

        }
        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(constants.KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences1.getString("user_id","")!=null && !sharedPreferences1.getString("user_id","").isEmpty()) {
            userId = sharedPreferences1.getString("user_id", "");

        }
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.getGeofenceStatus(userId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse=response.body();
                    if(!apiResponse.getError()){

                        //get data from api return
                        arrayListEventId=apiResponse.getEvent().getEventIdArray();
                        arrayListTitle=apiResponse.getEvent().getTitleArray();
                        arrayListReward=apiResponse.getEvent().getRewardArray();
                        //get trnasition type
                        int geofenceTransition=geofencingEvent.getGeofenceTransition();
                        //test that the reported transition was of interest
                        if(geofenceTransition== Geofence.GEOFENCE_TRANSITION_ENTER||geofenceTransition==Geofence.GEOFENCE_TRANSITION_EXIT){
                            //get the geofences that were triggered. a single event can trigger
                            //multiple geofences.
                            List<Geofence> triggeringGeofences=geofencingEvent.getTriggeringGeofences();

                            for (Geofence geofence:triggeringGeofences){
                                String geoRequest=geofence.getRequestId();
                                String title,reward;
                                        for(int i=0;i<arrayListEventId.size();i++){
                                           // Log.d("CHECKEHCK",detail+"||"+geoRequest);
                                            title=arrayListTitle.get(i);
                                            reward=arrayListReward.get(i);
                                            if(arrayListEventId.get(i).equals(geoRequest)){

                                               eventId=arrayListEventId.get(i);
                                                sendNotification(title,reward,eventId);

                                            }
                                        }
                                for (String detail:arrayListEventId
                                     ) {

                                }

//            Log.d("GEOID",geofence.getRequestId());
                            }
                            //get the transition detals as a string
                            //this to convert coorditnate to locaiton name
                           // String geofenceTransitionDetails=getGeofenceTransitionDetails(geofenceTransition,triggeringGeofences);

                            //send notification and log the transition details


                        }else {
                            //log the error
                            Log.d("broadcasterror1", String.valueOf(geofenceTransition));
                        }
                    }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

    }
    //private void sendNotification(String geofenceTransitionDetails) {
    private void sendNotification(String title,String reward,String eventId) {



        Intent resultIntent=new Intent(this, NotificationReceiver.class);
//        resultIntent.setAction("YES");
        resultIntent.putExtra(KEY_EVENT_ID,eventId);
        resultIntent.putExtra(KEY_USER_ID,userId);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );


        //  Log.d("broadcastnotificatipn", String.valueOf(geofenceTransitionDetails));
        String CHANNEL_ID = "Zoftino";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("chaneel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);


        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText("Collect your reward for "+title+": "+reward+" T money")
                .setAutoCancel(false)

                .setContentIntent(notifyPendingIntent)
                .setSmallIcon(R.mipmap.ic_okazo_logo);

        Notification notification=builder.build();
        notification.flags= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(new Random().nextInt(),notification);
    }
    private String getGeofenceTransitionDetails( int geofenceTransition, List<Geofence> triggeringGeofences) {
        ArrayList<String> locationNames=new ArrayList<>();
        String geofenceTransitionString=getTransitionString(geofenceTransition);
        for (Geofence geofence:triggeringGeofences){
            locationNames.add(getLocationName(geofence.getRequestId()));
//            Log.d("GEOID",geofence.getRequestId());
        }
        String triggeringGeofencesString= TextUtils.join(",",locationNames);
        return geofenceTransitionString+" "+triggeringGeofencesString;
    }

    private String getTransitionString(int geofenceTransition) {
        switch (geofenceTransition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);
            default:
                return getString(R.string.unknown_geofence_transition);
        }
    }

    private String getLocationName(String key) {
        String[] strs = key.split("-");

        String locationName = null;
        if (strs != null && strs.length == 2) {
            double lat = Double.parseDouble(strs[0]);
            double lng = Double.parseDouble(strs[1]);

            locationName = getLocationNameGeocoder(lat, lng);
        }
        if (locationName != null) {
            return locationName;
        } else {
            return key;
        }
    }
    private String getLocationNameGeocoder(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (Exception ioException) {
            Log.e("", "Error in getting location name for the location");
        }

        if (addresses == null || addresses.size() == 0) {
            Log.d("", "no location name");
            return null;
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressInfo = new ArrayList<>();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressInfo.add(address.getAddressLine(i));
            }

            return TextUtils.join(System.getProperty("line.separator"), addressInfo);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("geofence","destroyed");

    }
}
