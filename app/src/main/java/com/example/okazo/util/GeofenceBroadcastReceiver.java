package com.example.okazo.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;


import android.location.Geocoder;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.okazo.GeoFenceActivity;
import com.example.okazo.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static androidx.core.content.ContextCompat.getSystemService;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("hello","working");
        GeofenceTransitionsJobIntentService.enqueueWork(context,intent);
//        this.context=context;
//        //GeofenceTransitionsJobIntentService.enqueueWork(context, intent);
//        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);
//        if(geofencingEvent.hasError()){
//           return;
//        }
//        //get trnasition type
//        int geofenceTransition=geofencingEvent.getGeofenceTransition();
//        //test that the reported transition was of interest
//        if(geofenceTransition== Geofence.GEOFENCE_TRANSITION_ENTER||geofenceTransition==Geofence.GEOFENCE_TRANSITION_EXIT){
//            //get the geofences that were triggered. a single event can trigger
//            //multiple geofences.
//            List<Geofence> triggeringGeofences=geofencingEvent.getTriggeringGeofences();
//            //get the transition detals as a string
//            String geofenceTransitionDetails=getGeofenceTransitionDetails(this,geofenceTransition,triggeringGeofences);
//            //send notification and log the transition details
//            sendNotification(geofenceTransitionDetails);
//
//        }else {
//            //log the error
//            Log.d("broadcasterror1", String.valueOf(geofenceTransition));
//        }
    }

    private void sendNotification(String geofenceTransitionDetails) {
        Log.d("broadcasterror1", String.valueOf(geofenceTransitionDetails));
        String CHANNEL_ID = "Zoftino";
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("chaneel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);


        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,CHANNEL_ID);
        builder.setContentTitle("Hello")
                .setContentText("locationDetails")
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher);

        Notification notification=builder.build();
        notificationManager.notify(new Random().nextInt(),notification);



    }

    private String getGeofenceTransitionDetails(GeofenceBroadcastReceiver geofenceBroadcastReceiver, int geofenceTransition, List<Geofence> triggeringGeofences) {
        ArrayList<String> locationNames=new ArrayList<>();
        for (Geofence geofence:triggeringGeofences){
            locationNames.add(getLocationName(geofence.getRequestId()));
        }
        String triggeringGeofencesString= TextUtils.join(",",locationNames);
        return triggeringGeofencesString;
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
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
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
}
