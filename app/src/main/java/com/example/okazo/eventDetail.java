package com.example.okazo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.DateTimePicker;
import com.example.okazo.util.EventTypeAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.example.okazo.util.constants.KEY_BUNDLE_EVENT_DETAIL;
import static com.example.okazo.util.constants.KEY_EVENT_DESCRIPTION;
import static com.example.okazo.util.constants.KEY_EVENT_END_DATE;
import static com.example.okazo.util.constants.KEY_EVENT_END_TIME;
import static com.example.okazo.util.constants.KEY_EVENT_SELECTED_LOCATION;
import static com.example.okazo.util.constants.KEY_EVENT_START_DATE;
import static com.example.okazo.util.constants.KEY_EVENT_START_TIME;
import static com.example.okazo.util.constants.KEY_EVENT_TICKET_STATUS;

import static com.example.okazo.util.constants.KEY_EVENT_TITLE;
import static com.example.okazo.util.constants.KEY_LATITUDE;
import static com.example.okazo.util.constants.KEY_LONGITUDE;
import static com.example.okazo.util.constants.KEY_PAGE_STATUS;
import static com.example.okazo.util.constants.KEY_TAG_ARRAY;

public class eventDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
SearchableSpinner eventTypeSpinner;
    private Double Lat,Lng;
    private ApiInterface apiInterface;
    private  ArrayList<String> arraySpinner;
    private ArrayList<EventDetail> selectedEventType=new ArrayList<EventDetail>();
    private HashSet<String> set=new HashSet<>();
    private EventTypeAdapter adapter;
    private ImageView buttonNext;
    RecyclerView recyclerView;

    ImageButton imageButtonPointerTicket;
    private static final int REQUEST_CODE = 5675;
    private TextView textViewEventNameError,textViewEventStartDateError,textViewEventEndDateError,
                        textViewEventStartTimeError,textViewEventEndTimeError,textViewEventLocationError,
                            textViewEventDescriptionError,textViewEventTagError;
    private TextView spinnerError;
    private Boolean dateStatus=false,timeStatus=false,spinnerStatus=false,titleStatus=false,endTimeStatus=false,endDateStatus=false,locationStatus=false,descriptionStatus=false;
    private TextInputEditText inputEditTextEventName,inputEditTextEventStartDate,
            inputEditTextEventStartTime,inputEditTextLocation,
            inputEditTextEventEndDate,inputEditTextEventEndTime,inputEditTextEventDescription;
    Switch aSwitch,switchTicket;

    private CarmenFeature home;
    private CarmenFeature work;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar=findViewById(R.id.toolbar_event_detail);
        setSupportActionBar(toolbar);
        eventTypeSpinner=findViewById(R.id.event_detail_event_type_spinner);
       apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
       buttonNext=findViewById(R.id.toolbar_next);
        inputEditTextEventStartDate=findViewById(R.id.event_detail_event_start_date);
        inputEditTextEventStartTime=findViewById(R.id.event_detail_event_start_time);
        inputEditTextEventEndDate=findViewById(R.id.event_detail_event_end_date);
        inputEditTextEventEndTime=findViewById(R.id.event_detail_event_end_time);
        inputEditTextEventDescription=findViewById(R.id.event_detail_description);
        textViewEventDescriptionError=findViewById(R.id.event_detail_event_description_error);
        textViewEventEndDateError=findViewById(R.id.event_detail_event_end_error);
        textViewEventNameError=findViewById(R.id.event_name_error);
        textViewEventStartDateError=findViewById(R.id.event_detail_event_start_date_error);
        textViewEventStartTimeError=findViewById(R.id.event_detail_event_start_time_error);
        textViewEventEndTimeError=findViewById(R.id.event_detail_event_end_time_error);
        textViewEventLocationError=findViewById(R.id.event_detail_event_location_error);
        textViewEventTagError=findViewById(R.id.event_detail_event_tag_error);

        inputEditTextLocation=findViewById(R.id.event_detail_location);
        inputEditTextEventName=findViewById(R.id.event_name);
        switchTicket=findViewById(R.id.event_ticket_status);
        imageButtonPointerTicket=findViewById(R.id.pointer_ticket);
        imageButtonPointerTicket.setVisibility(View.GONE);
        spinnerError=(TextView)eventTypeSpinner.getSelectedView();

        aSwitch=findViewById(R.id.event_page_status);
        inputEditTextLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_UP==event.getAction()) {
                    initSearchFab();

                }
                return false;
            }
        });
        switchTicket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imageButtonPointerTicket.setVisibility(View.VISIBLE);
                }else {
                    imageButtonPointerTicket.setVisibility(View.GONE);
                }
            }
        });
        inputEditTextEventEndTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP==event.getAction()){
                    DateTimePicker dateTimePicker=new DateTimePicker();
                    dateTimePicker.showDatePickerDialog(eventDetail.this,inputEditTextEventEndTime,"time");
                }
                return false;
            }
        });
        inputEditTextEventEndDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP==event.getAction()){
                    DateTimePicker dateTimePicker=new DateTimePicker();
                    dateTimePicker.showDatePickerDialog(eventDetail.this,inputEditTextEventEndDate,"date");
                }

                return false;
            }
        });
        inputEditTextEventStartDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP==event.getAction()){
                    DateTimePicker dateTimePicker=new DateTimePicker();
                    dateTimePicker.showDatePickerDialog(eventDetail.this,inputEditTextEventStartDate,"date");
                }

                return false;
            }
        });
        inputEditTextEventStartTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP==event.getAction()){
                    DateTimePicker dateTimePicker=new DateTimePicker();
                    dateTimePicker.showDatePickerDialog(eventDetail.this,inputEditTextEventStartTime,"time");
                }
                return false;
            }
        });







       buttonNext.setOnClickListener(new View.OnClickListener() {
           @Override

           public void onClick(View v) {
               boolean valid = true;
               int counter=0;
               String pageStatus, ticketStatus;
               if (aSwitch.isChecked()) {
                   pageStatus = aSwitch.getTextOn().toString();
               } else {
                   pageStatus = aSwitch.getTextOff().toString();
               }
               if (switchTicket.isChecked()) {
                   ticketStatus = switchTicket.getTextOn().toString();
               } else {
                   ticketStatus = switchTicket.getTextOff().toString();
               }
               Drawable errorIcon=getResources().getDrawable(R.drawable.ic_error);
               //validation
               if (inputEditTextEventName.getText().toString().equals("")) {
                   valid = false;
                   inputEditTextEventName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,errorIcon,null);
                   textViewEventNameError.setText("Please Enter event title");
                   textViewEventNameError.setTextColor(Color.RED);

               } else {
                   valid = true;
                   counter+=1;
                   inputEditTextEventName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                   textViewEventNameError.setText("");

               }
               //start Date
               if (inputEditTextEventStartDate.getText().toString().equals("")) {
                   valid = false;
                   inputEditTextEventStartDate.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,errorIcon,null);
                   textViewEventStartDateError.setText("Please Select start Date");
               } else {
                   valid = true;
                   counter+=1;
                   inputEditTextEventStartDate.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                   textViewEventStartDateError.setText("");

               }
               //end Date
               if (inputEditTextEventEndDate.getText().toString().equals("")) {
                   valid = false;
                   inputEditTextEventEndDate.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,errorIcon,null);
                   textViewEventEndDateError.setText("Please Select End Date");
               } else {
                   valid = true;
                   counter+=1;
                   inputEditTextEventEndDate.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                   textViewEventEndDateError.setText("");

               }
               //start Time
               if (inputEditTextEventStartTime.getText().toString().equals("")) {
                   valid = false;
                   inputEditTextEventStartTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,errorIcon,null);
                   textViewEventStartTimeError.setText("Please Select Start Time");
               } else {
                   valid = true;
                   counter+=1;
                   inputEditTextEventStartTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                   textViewEventStartTimeError.setText("");
               }
               //End Time
               if (inputEditTextEventEndTime.getText().toString().equals("")) {
                   valid = false;
                   inputEditTextEventEndTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,errorIcon,null);
                   textViewEventEndTimeError.setText("Please Select End Time");
               } else {
                   valid = true;
                   counter+=1;
                   inputEditTextEventEndTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                   textViewEventEndTimeError.setText("");

               }
               //location
               if (inputEditTextLocation.getText().toString().equals("")) {
                   valid = false;
                   inputEditTextLocation.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,errorIcon,null);
                   textViewEventLocationError.setText("Please Select locaiton");
               } else {
                   valid = true;
                   counter+=1;
                   inputEditTextLocation.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                   textViewEventLocationError.setText("");

               }
               //description
               if (inputEditTextEventDescription.getText().toString().equals("")) {
                   valid = false;
                   inputEditTextEventDescription.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,errorIcon,null);
                   textViewEventDescriptionError.setText("PLease Enter event description");
               } else {
                   valid = true;
                   counter+=1;
                   inputEditTextEventDescription.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                   textViewEventDescriptionError.setText("");
               }
               if (adapter.getItemCount() == 0) {
                   valid = false;
                   textViewEventTagError.setText("Please Select at least one");

               } else {
                   valid = true;
                   counter+=1;
                   textViewEventTagError.setText("");
               }

               if (counter==8) {
                   Bundle bundle = new Bundle();
                   bundle.putSerializable(KEY_TAG_ARRAY, selectedEventType);
                   bundle.putString(KEY_EVENT_TITLE, inputEditTextEventName.getText().toString());
                   bundle.putString(KEY_EVENT_START_DATE, inputEditTextEventStartDate.getText().toString());
                   bundle.putString(KEY_EVENT_START_TIME, inputEditTextEventStartTime.getText().toString());
                   bundle.putString(KEY_EVENT_END_DATE,inputEditTextEventEndDate.getText().toString());
                   bundle.putString(KEY_EVENT_END_TIME,inputEditTextEventEndTime.getText().toString());
                   bundle.putString(KEY_EVENT_SELECTED_LOCATION,inputEditTextLocation.getText().toString());
                   bundle.putString(KEY_EVENT_DESCRIPTION,inputEditTextEventDescription.getText().toString());
                   bundle.putString(KEY_LATITUDE,String.valueOf(Lat));
                   bundle.putString(KEY_LONGITUDE,String.valueOf(Lng));
                   bundle.putString(KEY_EVENT_TICKET_STATUS, ticketStatus);
                   bundle.putString(KEY_PAGE_STATUS, pageStatus);
                   if (ticketStatus.toLowerCase().equals("public")) {
//                       Intent intent = new Intent(eventDetail.this, EventDetailPreviewActivity.class);
//                       intent.putExtra( KEY_BUNDLE_EVENT_DETAIL,bundle);
//                       startActivity(intent);
                       Toast.makeText(eventDetail.this, "No TIcket"+ticketStatus, Toast.LENGTH_SHORT).show();

                   } else {
                       //Toast.makeText(eventDetail.this, ""+pageStatus, Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(eventDetail.this, TicketDetailActivity.class);
                       intent.putExtras( bundle);
//               intent.putParcelableArrayListExtra(KEY_TAG_ARRAY,selectedEventType);

                       startActivity(intent);

                   }


                }}});


      apiInterface.getEventType().enqueue(new Callback<ArrayList<EventDetail>>() {
          @Override
          public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
              ArrayList<EventDetail> eventDetail=response.body();
          arraySpinner=new ArrayList<>();
            arraySpinner.add("Select an tag");
              for (EventDetail eventType:eventDetail
                   ) {

                  arraySpinner.add(eventType.getEventType());
                  Log.d("eventType",eventType.getEventType());
              }
              ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,arraySpinner);
              adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
              eventTypeSpinner.setAdapter(adapter);
          }

          @Override
          public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {
              Toast.makeText(eventDetail.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
          }
      });
      String parentClass="eventDetail";
        recyclerView=findViewById(R.id.event_detail_recycler_view);
        adapter=new EventTypeAdapter(selectedEventType,parentClass);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        eventTypeSpinner.setOnItemSelectedListener(this);
        adapter.setRemoveClickListner(new EventTypeAdapter.OnRemoveClickListner() {
             @Override
                 public void onRemoveClick(int position, ArrayList<EventDetail> eventDetails) {

                 set.remove(eventDetails.get(position).getEventType());

                  selectedEventType.remove(position);

                  if(recyclerView.getChildCount()==1){
                      spinnerStatus=false;


                      spinnerError=(TextView)eventTypeSpinner.getSelectedView();
                      spinnerError.setTextColor(Color.RED);



                    //

                  }else {
                      spinnerStatus=true;
                      if(dateStatus && timeStatus && spinnerStatus && titleStatus){

                      }
                  }

//                 Log.d("setis: ", String.valueOf(selectedEventType.get(position).getEventType()));
      adapter.notifyDataSetChanged();
            }
         });




    }
    private void initSearchFab() {

        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)

                        .build(PlaceOptions.MODE_CARDS))
                .build(eventDetail.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position>0) {

            if (selectedEventType.size() >= 5) {
                Toast.makeText(this, "you cant select more than 4", Toast.LENGTH_SHORT).show();
            } else {

                String item = parent.getItemAtPosition(position).toString();
                spinnerStatus=true;
                EventDetail eventDetail = new EventDetail(item);

                if(dateStatus && timeStatus && spinnerStatus && titleStatus){

                }
                if (!set.contains(item)) {
                    set.add(item);

                    selectedEventType.add(eventDetail);


                    adapter.notifyDataSetChanged();

                    //
                }
                eventTypeSpinner.setSelection(0);
            }
        }

        }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && requestCode==REQUEST_CODE_AUTOCOMPLETE){

            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            startActivityForResult(
                    new PlacePicker.IntentBuilder()
                            .accessToken(getString(R.string.mapbox_access_token))
                            .placeOptions(PlacePickerOptions.builder().includeDeviceLocationButton(true)
                                    .statingCameraPosition(new CameraPosition.Builder()
                                            .target(new LatLng(((Point)selectedCarmenFeature.geometry()).latitude(), ((Point)selectedCarmenFeature.geometry()).longitude())).zoom(14).build())
                                    .build())
                            .build(eventDetail.this), REQUEST_CODE);
        }
        if (resultCode == RESULT_CANCELED) {
// Show the button and set the OnClickListener()


        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
// Retrieve the information from the selected location's CarmenFeature
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);

// Set the TextView text to the entire CarmenFeature. The CarmenFeature
// also be parsed through to grab and display certain information such as
// its placeName, text, or coordinates.
            if (carmenFeature != null) {


                Lat = carmenFeature.center().latitude();
                Lng=carmenFeature.center().longitude();




                String  placeTitle = carmenFeature.placeName();
                inputEditTextLocation.setText(placeTitle);
//                String[] element=placeTitle.split("\\s*,\\s*");
//                int addressCounter=1,addressDetailCounter=1;
//                placeList= Arrays.asList(element);
//                int placeListCounter=placeList.size();
//                for (String value:placeList){
//                    if(temp==placeListCounter || temp==placeListCounter-1){
//                        if(addressDetailCounter==1){
//                            addressDetail=value;
//                        }else {
//                            addressDetail = addressDetail + ", " + value;
//
//                        }
//                        addressDetailCounter+=1;
//                    }else {
//                        if(addressCounter==1){
//                            address=value;
//                        }else {
//                            address = address + ", " + value;
//                        }
//                        addressCounter+=1;
//                    }
//
//
//                    temp+=1;
//
//                }

            }else
            {
                Toast.makeText(this, "Could not fetch location information, Please try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
