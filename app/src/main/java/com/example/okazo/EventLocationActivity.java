package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;

import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

import static com.example.okazo.util.constants.KEY_EVENT_TICKET_STATUS;


public class EventLocationActivity extends AppCompatActivity {


private ImageView buttonChangeLocation;
EditText editTextSelectedLocation,editTextSelectedCountry;
Double Lat,Lng;
ImageView buttonNext;
    private Bundle bundle;
    String ticketStatus;
    ImageButton pointerTicket;
    private static final int REQUEST_CODE = 5678;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_event_location);
        Toolbar toolbar=findViewById(R.id.toolbar_event_location);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
         bundle=intent.getBundleExtra("bundle");
         ticketStatus=bundle.getString(KEY_EVENT_TICKET_STATUS);
        buttonChangeLocation=findViewById(R.id.change_event_location);
        editTextSelectedLocation=findViewById(R.id.event_selected_location);
        editTextSelectedCountry=findViewById(R.id.event_selected_country);
        pointerTicket=findViewById(R.id.pointer_ticket_location);
        if(ticketStatus.toLowerCase().equals("private")){
            pointerTicket.setVisibility(View.VISIBLE);
        }else {
            pointerTicket.setVisibility(View.GONE);
        }
            buttonNext=findViewById(R.id.toolbar_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1;
                if(ticketStatus.toLowerCase().equals("private")) {
                    intent1 = new Intent(EventLocationActivity.this,TicketDetailActivity.class );

                    //bundle.putString();

                    startActivity(intent1);
                }
            }
        });

        goToPickerActivity();
        buttonChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPickerActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
// Show the button and set the OnClickListener()

            buttonChangeLocation.setVisibility(View.VISIBLE);
            buttonChangeLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToPickerActivity();
                }
            });
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
// Retrieve the information from the selected location's CarmenFeature
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);

// Set the TextView text to the entire CarmenFeature. The CarmenFeature
// also be parsed through to grab and display certain information such as
// its placeName, text, or coordinates.
            if (carmenFeature != null) {


                Lat = carmenFeature.center().latitude();
                 Lng=carmenFeature.center().longitude();
                List<String> placeList;

                String address=" ",addressDetail=" ";
                int temp=1;
                String  placeTitle = carmenFeature.placeName();
                   String[] element=placeTitle.split("\\s*,\\s*");
                    int addressCounter=1,addressDetailCounter=1;
                   placeList=Arrays.asList(element);
                   int placeListCounter=placeList.size();
                   for (String value:placeList){
                      if(temp==placeListCounter || temp==placeListCounter-1){
                          if(addressDetailCounter==1){
                              addressDetail=value;
                          }else {
                              addressDetail = addressDetail + ", " + value;

                          }
                          addressDetailCounter+=1;
                      }else {
                          if(addressCounter==1){
                              address=value;
                          }else {
                              address = address + ", " + value;
                          }
                          addressCounter+=1;
                      }


                      temp+=1;

                   }







                editTextSelectedLocation.setText(address);
                editTextSelectedCountry.setText(addressDetail);


              //Log.d("check:",carmenFeature.toJson()+"");
                //Toast.makeText(this, "location: "+carmenFeature.toJson(), Toast.LENGTH_SHORT).show();

//                selectedLocationTextView.setText(String.format(
//                        getString(R.string.selected_place_info), carmenFeature.toJson()));
            }else
            {
                Toast.makeText(this, "Could not fetch location information, Please try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }
//TODO ADD SEARCH BAR IN PLACE PICKER

    private void goToPickerActivity() {
        startActivityForResult(
                new PlacePicker.IntentBuilder()
                        .accessToken(getString(R.string.mapbox_access_token))
                        .placeOptions(PlacePickerOptions.builder().includeDeviceLocationButton(true)
                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(27.7172, 85.3240)).zoom(8).build())
                                .build())
                        .build(this), REQUEST_CODE);
    }








}

