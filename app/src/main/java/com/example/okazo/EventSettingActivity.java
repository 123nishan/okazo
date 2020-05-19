package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.ConfirmationDialog;
import com.example.okazo.util.DateTimePicker;
import com.example.okazo.util.EditTicketAdapter;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.common.api.Api;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_USER_ROLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class EventSettingActivity extends AppCompatActivity implements ConfirmationDialog.orderConfirmationListener {
    private FloatingActionButton fabProgressCircle;
    private FABProgressCircle progressCircle;
    private ImageView imageViewProfile,imageViewChange;
    private ExpandableCardView expandableCardViewEventDetail,expandableCardViewTicketDetail,expandableCardViewEventDate,expandableCardViewEventLocation;
    private String userRole,path,eventId,ticketStatus,pagePrivate,ticketCount="0",currentLatitude,currentLongitude;
    private Double latitude,longitude;

    private Switch switchPrivate,switchTicket;
    private int changeCounter=0;
    private static final int CHOOSE_IMAGE = 505;
    private ApiInterface apiInterface;
    private Uri uriProfileImage;
    private TextInputEditText eventDetailtitle,eventDetailDescription,eventDetailStartdate,eventDetailEndDate,
            eventDetailStartTime,eventDetailEndTime,eventDetailLocation,eventDetailSingleTicketPrice,eventDetailSingleTicketQuantity,textInputEditTextQuantity,textInputEditTextPrice;
    private RelativeLayout relativeLayout;
    private TextView textViewTagHeading,textViewTicketSwitchMessage,textViewAddType,textViewRemoveType;
    private EventDetail detail;
    private RecyclerView recyclerViewTag;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private RecyclerView recyclerView;
    private String title,description,startDate,endDate,startTime,endTime,ticketNoTypePrice,ticketNoTypeQuantity;
    private Boolean aBooleanTitle=false,aBooleanDescription=false,aBooleanStartDate=false,aBooleanStartTime=false
            ,aBooleanEndTime=false,aBooleanEndDate=false,aBooleanPageStatus=false,aBooleanTicketStatus=false
            ,aBooleanNoTypeTicketPrice=false,aBooleanNoTypeTicketQuantity=false,aBooleanImage=false,aBooleanLocation=false;
    private String changeTitle,changeDescription,changeStartDate,changeEndDate,changeStartTime,
            changeEndTime,changeLocation,changePageStatus,changeTicketStatus,changeTicketPrice,
            changeTicketQuantity,changeLatitude,changeLongitude,changeTicketId,changeTicketName;
    private ArrayList<String> arrayListPrice=new ArrayList<>(),arrayListQuantity=new ArrayList<>(),arrayListId= new ArrayList<>(),arrayListName=new ArrayList<>(),arrayListFromAdapter= new ArrayList<>();
    private EditTicketAdapter adapter,adapter1;
    private TextView buttonAddMore;
    private LinearLayout linearLayoutNoTicket,linearLayout,linearLayout2,linearLayout1;
    private int expandCount=0;
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final int REQUEST_CODE = 5675;
    private Bundle savedInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance=savedInstanceState;
//        getSupportActionBar().hide();
       // getActionBar().hide();
        setContentView(R.layout.activity_event_setting);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
//        arrayListId=new ArrayList<>();
//        arrayListName=new ArrayList<>();
//        arrayListPrice=new ArrayList<>();
//        arrayListQuantity=new ArrayList<>();
//        arrayListFromAdapter=new ArrayList<>();

        fabProgressCircle=findViewById(R.id.event_setting_activity_fabToolBar);
        progressCircle=findViewById(R.id.event_setting_activity_fabProgressCircleToolBar);
        imageViewProfile=findViewById(R.id.event_setting_activity_image);
        imageViewChange=findViewById(R.id.event_setting_activity_change);
        expandableCardViewEventDate=findViewById(R.id.event_setting_activity_expandable_event_detail_date);
        expandableCardViewEventDetail=findViewById(R.id.event_setting_activity_expandable_event_detail);
        expandableCardViewEventLocation=findViewById(R.id.event_setting_activity_expandable_event_detail_location);
        expandableCardViewTicketDetail=findViewById(R.id.event_setting_activity_expandable_ticket);
        switchPrivate=findViewById(R.id.event_setting_activity_private_switch);
        //expandable event detail
        eventDetailtitle=findViewById(R.id.expand_event_title);
        eventDetailDescription=findViewById(R.id.expand_event_description);
        textViewTagHeading=findViewById(R.id.expand_detail_tag_heading);
        textViewTagHeading.setVisibility(View.GONE);
        recyclerViewTag=findViewById(R.id.expand_event_tag_recylerview);
        recyclerViewTag.setVisibility(View.GONE);
        //expandable event date
        eventDetailStartdate=findViewById(R.id.expand_event_detail_event_start_date);
        eventDetailEndDate=findViewById(R.id.expand_event_detail_event_end_date);
        eventDetailStartTime=findViewById(R.id.expand_event_detail_event_start_time);
        eventDetailEndTime=findViewById(R.id.expand_event_detail_event_end_time);
        //expandable event location
        eventDetailLocation=findViewById(R.id.expand_event_location_detail);
        //expandable event ticket
        eventDetailSingleTicketPrice=findViewById(R.id.expand_ticekt_price);
        eventDetailSingleTicketQuantity=findViewById(R.id.expand_event_total_ticket);
        switchTicket=findViewById(R.id.expand_ticket_switch);
        relativeLayout=findViewById(R.id.expand_ticket_switch_layout);
        relativeLayout.setVisibility(View.VISIBLE);
         textInputEditTextQuantity=findViewById(R.id.expand_event_total_ticket);
        textInputEditTextPrice=findViewById(R.id.expand_ticekt_price);
        textViewTicketSwitchMessage=findViewById(R.id.expand_event_ticket_switch_message);
        buttonAddMore=findViewById(R.id.expand_event_ticket_add_ticket);
        linearLayoutNoTicket=findViewById(R.id.expand_ticket_layout_yes);
         linearLayout=findViewById(R.id.expand_ticket_single_layout);
         linearLayout1=findViewById(R.id.expand_yes_ticket_type);
         linearLayout2=findViewById(R.id.expand_no_ticket_type);
         textViewAddType=findViewById(R.id.expand_event_ticket_add_type);
        textViewRemoveType=findViewById(R.id.expand_event_ticket_remove_type);
     //   fabProgressCircle.setVisibility(View.GONE);


        Bundle bundle=getIntent().getExtras();
        userRole=bundle.getString(KEY_USER_ROLE);
        eventId=bundle.getString(KEY_EVENT_ID);

        recyclerView=findViewById(R.id.expand_ticket_types_recycler_view);
        adapter=new EditTicketAdapter(arrayListId,arrayListName,arrayListPrice,arrayListQuantity);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(EventSettingActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        fabProgressCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressCircle.show();
              saveButton();
            }
        });

        apiInterface.getEventAllDetail(eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    detail=apiResponse.getEvent();
                    arrayListFromAdapter=detail.getTicketId();


                    expandableCardViewEventLocation.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
                        @Override
                        public void onExpandChanged(View v, boolean isExpanded) {

                            addMap(detail.getLatitude(),detail.getLongitude(),savedInstanceState);


                            eventDetailLocation.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    if(MotionEvent.ACTION_UP==motionEvent.getAction()) {
                                        initSearchFab();

                                    }
                                    return false;
                                }


                            });

                        }

                    });


                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

        apiInterface.getEventAllDetail(eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    EventDetail eventDetail=apiResponse.getEvent();






                    //detail=eventDetail;
                    eventDetailtitle.setText(eventDetail.getTitle());
                    title=eventDetail.getTitle();
                    eventDetailtitle.setClickable(true);
                    eventDetailtitle.setCursorVisible(true);
                    eventDetailtitle.setFocusable(true);
                    eventDetailtitle.setFocusableInTouchMode(true);

                    eventDetailDescription.setText(eventDetail.getDescription());
                    description=eventDetail.getDescription();
                    eventDetailDescription.setClickable(true);
                    eventDetailDescription.setCursorVisible(true);
                    eventDetailDescription.setFocusable(true);
                    eventDetailDescription.setFocusableInTouchMode(true);

                    eventDetailStartdate.setText(eventDetail.getStartDate());
                    startDate=eventDetail.getStartDate();
//                    eventDetailStartdate.setClickable(true);
//                    eventDetailStartdate.setCursorVisible(true);
//                    eventDetailStartdate.setFocusable(true);
//                    eventDetailStartdate.setFocusableInTouchMode(true);

                    eventDetailEndDate.setText(eventDetail.getEndDate());
                    endDate=eventDetail.getEndDate();
//                    eventDetailEndDate.setClickable(true);
//                    eventDetailEndDate.setCursorVisible(true);
//                    eventDetailEndDate.setFocusable(true);
//                    eventDetailEndDate.setFocusableInTouchMode(true);

                    eventDetailStartTime.setText(eventDetail.getStartTime());
                    startTime=eventDetail.getStartTime();
//                    eventDetailStartTime.setClickable(true);
//                    eventDetailStartTime.setCursorVisible(true);
//                    eventDetailStartTime.setFocusable(true);
//                    eventDetailStartTime.setFocusableInTouchMode(true);

                    eventDetailEndTime.setText(eventDetail.getEndTime());
                    endTime=eventDetail.getEndTime();
//                    eventDetailEndTime.setClickable(true);
//                    eventDetailEndTime.setCursorVisible(true);
//                    eventDetailEndTime.setFocusable(true);
//                    eventDetailEndTime.setFocusableInTouchMode(true);


                    eventDetailLocation.setText(eventDetail.getPlace());

                    pagePrivate=eventDetail.getPageStatus();
                    ticketStatus=eventDetail.getTicketStatus();
                    if(pagePrivate.equals("1")){
                        switchPrivate.setChecked(true);
                    }else {
                        switchPrivate.setChecked(false);
                    }

                    if(ticketStatus.equals("1")){
                        switchTicket.setChecked(true);
                    }else {
                        switchTicket.setChecked(false);
                    }

                    String imagePath=KEY_IMAGE_ADDRESS+(eventDetail.getImage());
                    Glide.with(EventSettingActivity.this)
                            .load(Uri.parse(imagePath))
                            .placeholder(R.drawable.ic_okazo_logo_background)
                            .error(R.drawable.ic_okazo_logo_background)
                            .centerCrop()
                            .into(imageViewProfile);

//                changeTitle=title;
//                changeDescription=description;
//                changeStartDate=startDate;
//                changeEndDate=endDate;
//                changeEndTime=endTime;
//                changeStartTime=startTime;
//                changePageStatus=pagePrivate;
//                changeTicketStatus=ticketStatus;
                    if(ticketStatus.equals("1")) {
                        textViewRemoveType.setVisibility(View.GONE);
                        arrayListPrice=eventDetail.getTicketPrice();
                        arrayListQuantity=eventDetail.getTicketQuantity();
                        arrayListName=eventDetail.getTicketName();
                        arrayListId=eventDetail.getTicketId();

                        ticketCount=eventDetail.getTicketCount();
                        if (Integer.valueOf(ticketCount) > 1) {
                            textViewAddType.setVisibility(View.VISIBLE);
                            textViewRemoveType.setVisibility(View.VISIBLE);
                            //if multiple ticket type
                            linearLayout.setVisibility(View.GONE);
                            linearLayout1.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.GONE);


                            adapter=new EditTicketAdapter(arrayListId,arrayListName,arrayListPrice,arrayListQuantity);
                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(EventSettingActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            // recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            //arrayListFromAdapter=arrayListId;


                            adapter.setOnRemoveClickListener(new EditTicketAdapter.OnRemoveClickListener() {
                                @Override
                                public void onRemoveClick(int position,ArrayList<String>id) {
                                    if(adapter.getItemCount()<=2){
                                        DynamicToast.makeError(EventSettingActivity.this,"Atleats two type of ticket needed").show();

                                    }else {
                                        arrayListId.remove(position);
                                        arrayListName.remove(position);
                                        arrayListPrice.remove(position);
                                        arrayListQuantity.remove(position);
                                        //  arrayListFromAdapter=id;
                                        if (arrayListId.equals(arrayListFromAdapter)) {
                                            aBooleanTicketStatus = false;

                                        } else {
                                            aBooleanTicketStatus = true;

                                        }
                                        visibleSaveButton();
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });

                            textViewAddType.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    expandableCardViewTicketDetail.collapse();

                                    arrayListId.add("Empty");
                                    arrayListName.add("Empty");
                                    arrayListPrice.add("Empty");
                                    arrayListQuantity.add("Empty");
                                    adapter.notifyDataSetChanged();
                                    aBooleanTicketStatus=true;
                                    visibleSaveButton();
                                    expandableCardViewTicketDetail.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
                                        @Override
                                        public void onExpandChanged(View v, boolean isExpanded) {
                                            if(!isExpanded){
                                                expandableCardViewTicketDetail.expand();
                                                expandableCardViewTicketDetail.removeOnExpandedListener();
                                            }
                                        }
                                    });
                                }
                            });
                            //adapter.notifyDataSetChanged();



//                            for(int i=0;i<arrayListPrice.size();i+=1){
//                                //Log.d("PRICE",arrayListPrice.get(i));
//                            }


                        }else {
                            // if no ticket type
                            textViewRemoveType.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.GONE);
                            linearLayout1.setVisibility(View.GONE);
                            linearLayout2.setVisibility(View.VISIBLE);

                            textInputEditTextQuantity.setClickable(true);
                            textInputEditTextQuantity.setFocusableInTouchMode(true);
                            textInputEditTextQuantity.setFocusable(true);
                            textInputEditTextQuantity.setCursorVisible(true);


                            textInputEditTextPrice.setClickable(true);
                            textInputEditTextPrice.setFocusableInTouchMode(true);
                            textInputEditTextPrice.setFocusable(true);
                            textInputEditTextPrice.setCursorVisible(true);
                            for (String val:arrayListPrice
                                 ) {
                                textInputEditTextPrice.setText(val);
                                ticketNoTypePrice=val;
                            }
                            for (String val:arrayListQuantity
                            ) {
                                textInputEditTextQuantity.setText(val);
                                ticketNoTypeQuantity=val;
                            }
                            //ticketPrice
                            textInputEditTextPrice.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    if(charSequence.toString().equals(ticketNoTypePrice)){
                                        aBooleanNoTypeTicketPrice=false;
//                    hangeTitle=charSequence.toString();
                                    }else {
                                        changeTicketPrice=charSequence.toString();
                                        aBooleanNoTypeTicketPrice=true;
                                    }
                                    visibleSaveButton();
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                            //ticketPrice

                            //ticketQuantity
                            textInputEditTextQuantity.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    if(charSequence.toString().equals(ticketNoTypeQuantity)){
                                        aBooleanNoTypeTicketQuantity=false;
//                    hangeTitle=charSequence.toString();
                                    }else {
                                        changeTicketQuantity=charSequence.toString();
                                        aBooleanNoTypeTicketQuantity=true;
                                    }
                                    visibleSaveButton();
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                            //ticketQuantity

                        }
                    }else {
                        //no ticket
                        linearLayoutNoTicket.setVisibility(View.GONE);
                        textViewTicketSwitchMessage.setVisibility(View.VISIBLE);
                    }
                    //title
                    eventDetailtitle.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(charSequence.toString().equals(title)){
                                aBooleanTitle=false;
//                    hangeTitle=charSequence.toString();
                            }else {
                                changeTitle=charSequence.toString();
                                aBooleanTitle=true;
                            }
                            visibleSaveButton();
                           // Toast.makeText(EventSettingActivity.this, "a "+aBooleanTitle, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    //title

                    //description
                    eventDetailDescription.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(charSequence.toString().equals(description)){
                                aBooleanDescription=false;
//                    hangeTitle=charSequence.toString();
                            }else {
                                changeDescription=charSequence.toString();
                                aBooleanDescription=true;
                            }
                            visibleSaveButton();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    //description

                    //startDate
                   eventDetailStartdate.setOnTouchListener(new View.OnTouchListener() {
                       @Override
                       public boolean onTouch(View view, MotionEvent motionEvent) {
                           if(MotionEvent.ACTION_UP==motionEvent.getAction()){
                               DateTimePicker dateTimePicker=new DateTimePicker();
                               dateTimePicker.showDatePickerDialog(EventSettingActivity.this,eventDetailStartdate,"date");
                           }
                           return false;
                       }
                   });
                    eventDetailStartdate.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(charSequence.toString().equals(startDate)){
                                aBooleanStartDate=false;
//                    hangeTitle=charSequence.toString();
                            }else {
                                changeStartDate=charSequence.toString();
                                aBooleanStartDate=true;
                            }
                            visibleSaveButton();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    //startDate

                    //endDate
                    eventDetailEndDate.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if(MotionEvent.ACTION_UP==motionEvent.getAction()){
                                DateTimePicker dateTimePicker=new DateTimePicker();
                                dateTimePicker.showDatePickerDialog(EventSettingActivity.this,eventDetailEndDate,"date");
                            }
                            return false;
                        }
                    });
                    eventDetailEndDate.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(charSequence.toString().equals(endDate)){
                                aBooleanEndDate=false;
//                    hangeTitle=charSequence.toString();
                            }else {
                                changeEndDate=charSequence.toString();
                                aBooleanEndDate=true;
                            }
                            visibleSaveButton();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    //endDate

                    //startTime
                    eventDetailStartTime.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if(MotionEvent.ACTION_UP==motionEvent.getAction()){
                                DateTimePicker dateTimePicker=new DateTimePicker();
                                dateTimePicker.showDatePickerDialog(EventSettingActivity.this,eventDetailStartTime,"time");
                            }
                            return false;
                        }
                    });
                    eventDetailStartTime.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(charSequence.toString().equals(startTime)){
                                aBooleanStartTime=false;
//                    hangeTitle=charSequence.toString();
                            }else {
                                changeStartTime=charSequence.toString();
                                aBooleanStartTime=true;
                            }
                            visibleSaveButton();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    //startTime

                    //endTime
                    eventDetailEndTime.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if(MotionEvent.ACTION_UP==motionEvent.getAction()){
                                DateTimePicker dateTimePicker=new DateTimePicker();
                                dateTimePicker.showDatePickerDialog(EventSettingActivity.this,eventDetailEndTime,"time");
                            }
                            return false;
                        }
                    });
                    eventDetailEndTime.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(charSequence.toString().equals(endTime)){
                                aBooleanEndTime=false;
//                    hangeTitle=charSequence.toString();
                            }else {
                                changeEndTime=charSequence.toString();
                                aBooleanEndTime=true;
                            }
                            visibleSaveButton();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    //endTime


                }else {
                    if(apiResponse.getErrorMsg().equals("Banned")){
                        DynamicToast.makeError(EventSettingActivity.this,"This event has been blcked").show();
                        //TODO intent to main activity
                    }else {
                        DynamicToast.makeError(EventSettingActivity.this,"Unable to get data").show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });

        imageViewChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to change image?");
                confirmationDialog.show(getSupportFragmentManager(),"Confirmation");

            }
        });


        textViewRemoveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAddMore.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.GONE);
                aBooleanTicketStatus=true;
                visibleSaveButton();
            }
        });

        switchTicket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    //checked

                    textViewTicketSwitchMessage.setVisibility(View.GONE);
                    if(ticketStatus.equals("1")){


                        linearLayoutNoTicket.setVisibility(View.VISIBLE);
                        aBooleanTicketStatus=false;
                        changeTicketStatus="1";
                        ticketCount=String.valueOf(arrayListId.size());
                        if(Integer.valueOf(ticketCount)>1){
                            textViewRemoveType.setVisibility(View.VISIBLE);
                            buttonAddMore.setVisibility(View.GONE);
                            textViewAddType.setVisibility(View.VISIBLE);
                            if(!arrayListId.equals(arrayListFromAdapter)){


                                aBooleanTicketStatus=true;
                            }
                        }else if(Integer.valueOf(ticketCount)==1){
                            textViewRemoveType.setVisibility(View.GONE);
                            buttonAddMore.setVisibility(View.VISIBLE);
                        }
                        if(expandCount==0){

                        }else {
                            expandableCardViewTicketDetail.collapse();
                            expandableCardViewTicketDetail.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
                                @Override
                                public void onExpandChanged(View v, boolean isExpanded) {
                                    if(!isExpanded){
                                        expandableCardViewTicketDetail.expand();
                                        expandableCardViewTicketDetail.removeOnExpandedListener();
                                    }

                                }
                            });
                        }
                        expandCount+=1;
                        //linearLayout1.setVisibility(View.VISIBLE);
                    }else {
                        expandableCardViewTicketDetail.collapse();
                        linearLayoutNoTicket.setVisibility(View.VISIBLE);

                        linearLayout2.setVisibility(View.VISIBLE);

                        linearLayout.setVisibility(View.GONE);
                        linearLayout1.setVisibility(View.GONE);
                        buttonAddMore.setVisibility(View.VISIBLE);
//                        if(expandCount>0){
//
//                        }else {
//                            expandableCardViewTicketDetail.collapse();
//
//                            expandCount+=1;
//                            expandableCardViewTicketDetail.expand();
//                        }




//                        Log.d("expand1",expandableCardViewTicketDetail.isExpanded()+"");
//                         expandableCardViewTicketDetail.collapse();
//                        Log.d("expand2",expandableCardViewTicketDetail.isExpanded()+"");
                        changeTicketStatus="1";
                        aBooleanTicketStatus=true;
                        expandableCardViewTicketDetail.expand();
//                         expandableCardViewTicketDetail.expand();
//                        Log.d("expand3",expandableCardViewTicketDetail.isExpanded()+"");
                    }
                    visibleSaveButton();
                }else {
                    textViewAddType.setVisibility(View.GONE);
                        expandableCardViewTicketDetail.collapse();
                    //linearLayout2.setVisibility(View.GONE);
                    buttonAddMore.setVisibility(View.GONE);
                    textViewTicketSwitchMessage.setVisibility(View.VISIBLE);
                    linearLayoutNoTicket.setVisibility(View.GONE);
                    if(ticketStatus.equals("0")){
                        changeTicketStatus="0";
                        aBooleanTicketStatus=false;
                    }else {
                        changeTicketStatus="0";
                        aBooleanTicketStatus=true;
                    }
                    expandableCardViewTicketDetail.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
                        @Override
                        public void onExpandChanged(View v, boolean isExpanded) {
                            if(!isExpanded){
                                expandableCardViewTicketDetail.expand();
                                expandableCardViewTicketDetail.removeOnExpandedListener();
                            }

                        }
                    });
                    expandableCardViewTicketDetail.expand();
                    visibleSaveButton();
                    //unchecked
                }
            }
        });


        buttonAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableCardViewTicketDetail.collapse();
                linearLayout1.setVisibility(View.VISIBLE);
                textViewRemoveType.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.GONE);
                textViewAddType.setVisibility(View.VISIBLE);
//                arrayListId.add("Empty");
//                arrayListName.add("Empty");
//                arrayListPrice.add("Empty");
//                arrayListQuantity.add("Empty");
//                adapter1.notifyDataSetChanged();
                textViewAddType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        expandableCardViewTicketDetail.collapse();
                        aBooleanTicketStatus=true;
                        visibleSaveButton();
                        arrayListId.add("Empty");
                        arrayListName.add("Empty");
                        arrayListPrice.add("Empty");
                        arrayListQuantity.add("Empty");
                        adapter1.notifyDataSetChanged();
                        expandableCardViewTicketDetail.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
                            @Override
                            public void onExpandChanged(View v, boolean isExpanded) {
                                if(!isExpanded){
                                    expandableCardViewTicketDetail.expand();
                                    expandableCardViewTicketDetail.removeOnExpandedListener();
                                }
                            }
                        });
                    }
                });
                 //recyclerView=findViewById(R.id.expand_ticket_types_recycler_view);
                if(arrayListId.size()==1){
                    aBooleanTicketStatus=true;
                    visibleSaveButton();
                    arrayListId.add("Empty");
                   arrayListName.add("Empty");
                    arrayListPrice.add("Empty");
                    arrayListQuantity.add("Empty");
                }else if(arrayListId.size()==0){
                    aBooleanTicketStatus=true;
                    visibleSaveButton();
                    arrayListId.add("Empty");
                    arrayListName.add("Empty");
                    arrayListPrice.add("Empty");
                    arrayListQuantity.add("Empty");
                    arrayListId.add("Empty");
                    arrayListName.add("Empty");
                    arrayListPrice.add("Empty");
                    arrayListQuantity.add("Empty");
                }else {
                    aBooleanTicketStatus=false;
                    visibleSaveButton();
                }


                adapter1=new EditTicketAdapter(arrayListId,arrayListName,arrayListPrice,arrayListQuantity);
                LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(EventSettingActivity.this);
                linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager1);
                // recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter1);
                //adapter1.notifyDataSetChanged();
                //expandableCardViewTicketDetail.expand();
                adapter1.setOnRemoveClickListener(new EditTicketAdapter.OnRemoveClickListener() {
                    @Override
                    public void onRemoveClick(int position, ArrayList<String> id) {
                        if(adapter1.getItemCount()<=2){
                            DynamicToast.makeError(EventSettingActivity.this,"Atleats two type of ticket needed").show();

                        }else {
                            arrayListId.remove(position);
                            arrayListName.remove(position);
                            arrayListPrice.remove(position);
                            arrayListQuantity.remove(position);
                            //  arrayListFromAdapter=id;
                            if(arrayListId.equals(arrayListFromAdapter)){
                                aBooleanTicketStatus=false;

                            }else {
                                aBooleanTicketStatus=true;

                            }
                            visibleSaveButton();
                            adapter1.notifyDataSetChanged();
                        }

                    }
                });

                expandableCardViewTicketDetail.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
                    @Override
                    public void onExpandChanged(View v, boolean isExpanded) {
                        if(!isExpanded){
                            expandableCardViewTicketDetail.expand();
                            expandableCardViewTicketDetail.removeOnExpandedListener();
                        }
                    }
                });
                //expandableCardViewTicketDetail.list

            }
        });
    switchPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b){
                if(pagePrivate.equals("1")){
                    aBooleanPageStatus=false;
                    changePageStatus="1";
                }else {
                    changePageStatus="1";
                    aBooleanPageStatus=true;
                }
                visibleSaveButton();
               // Toast.makeText(EventSettingActivity.this, "a "+changeCounter, Toast.LENGTH_SHORT).show();
               // Toast.makeText(EventSettingActivity.this, "a "+changePageStatus+"/"+pagePrivate, Toast.LENGTH_SHORT).show();
            }else {
                if(pagePrivate.equals("0")){
                    aBooleanPageStatus=false;
                    changePageStatus="0";
                }else {
                    aBooleanPageStatus=true;
                    changePageStatus="0";

                }
                visibleSaveButton();
//                if(aBooleanTitle || aBooleanPageStatus || aBooleanDescription || aBooleanStartDate || aBooleanEndDate || aBooleanStartTime ||
//                        aBooleanEndTime|| aBooleanNoTypeTicketPrice || aBooleanNoTypeTicketQuantity){
//                    fabProgressCircle.setVisibility(View.VISIBLE);
//                }else {
//                    fabProgressCircle.setVisibility(View.GONE);
//                }
                //Toast.makeText(EventSettingActivity.this, "a "+changePageStatus+"false", Toast.LENGTH_SHORT).show();
            }
        }
    });
//    fabProgressCircle.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Log.d("Check",changeTitle+" "+changePageStatus);
//        }
//    });

    }

    private void addMap(String latitude, String longitude, Bundle savedInstanceState) {
        SupportMapFragment mapFragment;
        Mapbox.getInstance(EventSettingActivity.this, getString(R.string.mapbox_access_token));
        if(savedInstanceState==null) {

            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(EventSettingActivity.this, null);
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                    .zoom(14)

                    .build());

            mapFragment = SupportMapFragment.newInstance(options);
            transaction.add(R.id.expand_event_location, mapFragment, "com.mapbox.map");
            transaction.commit();
        }else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }
        if(mapFragment!=null){


            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    //List<Feature> symbolLayerIconFeatureList = new ArrayList<>();

                    mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")
                            .withImage(ICON_ID,BitmapFactory.decodeResource(EventSettingActivity.this.getResources(),R.drawable.map_default_map_marker))
                            .withSource(new GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeature(Feature.fromGeometry(Point.fromLngLat(Double.parseDouble(longitude),Double.parseDouble(latitude))))))
                            .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                    .withProperties(PropertyFactory.iconImage(ICON_ID),
                                            iconAllowOverlap(true),
                                            iconOffset(new Float[] {0f, -9f}))
                            ), new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {

// Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.


                        }
                    });
//                                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
//                                    @Override
//                                    public void onStyleLoaded(@NonNull Style style) {
//                                         //SymbolManager symbolManager=new SymbolManager(mapFragment,mapboxMap,style);
//
//                                    }
//                                });
                }
            });
        }
    }

    private void visibleSaveButton(){
        if(aBooleanTitle || aBooleanPageStatus || aBooleanDescription || aBooleanStartDate || aBooleanEndDate || aBooleanStartTime ||
                aBooleanEndTime|| aBooleanNoTypeTicketPrice || aBooleanNoTypeTicketQuantity || aBooleanTicketStatus || aBooleanImage || aBooleanLocation){
            fabProgressCircle.setVisibility(View.VISIBLE);
        }else {
            fabProgressCircle.setVisibility(View.GONE);
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            uriProfileImage = data.getData();

            Glide.with(EventSettingActivity.this)
                    .load(uriProfileImage)
                    .placeholder(R.drawable.ic_place_holder_background)
                    //.error(R.drawable.ic_image_not_found_background)
                    .centerCrop()
                    .into(imageViewProfile);
            // Bitmap bitmap = null;
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor=getContentResolver().query(uriProfileImage,filePathColumn,null,null,null);
            assert cursor!=null;
            cursor.moveToFirst();

            int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
            path=cursor.getString(columnIndex);
            cursor.close();

            aBooleanImage=true;
            visibleSaveButton();



        }


        if(resultCode== Activity.RESULT_OK && requestCode==REQUEST_CODE_AUTOCOMPLETE){

            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            startActivityForResult(
                    new PlacePicker.IntentBuilder()
                            .accessToken(getString(R.string.mapbox_access_token))
                            .placeOptions(PlacePickerOptions.builder().includeDeviceLocationButton(true)
                                    .statingCameraPosition(new CameraPosition.Builder()
                                            .target(new LatLng(((Point)selectedCarmenFeature.geometry()).latitude(), ((Point)selectedCarmenFeature.geometry()).longitude())).zoom(14).build())
                                    .build())
                            .build(EventSettingActivity.this), REQUEST_CODE);
        }

        if (resultCode == RESULT_CANCELED) {



        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            CarmenFeature carmenFeature = PlacePicker.getPlace(data);


            if (carmenFeature != null) {
                aBooleanLocation=true;
                visibleSaveButton();

                latitude = carmenFeature.center().latitude();
                longitude=carmenFeature.center().longitude();

                addMap(String.valueOf(latitude),String.valueOf(longitude),savedInstance);


                String  placeTitle = carmenFeature.placeName();
                eventDetailLocation.setText(placeTitle);


            }else
            {
                Toast.makeText(this, "Could not fetch location information, Please try again!", Toast.LENGTH_SHORT).show();
            }
        }



    }

    @Override
    public void OnYesClicked() {
        showImageChooser();
    }

    @Override
    public void OnNoClicked() {

    }
    private void initSearchFab() {

        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)

                        .build(PlaceOptions.MODE_CARDS))
                .build(EventSettingActivity.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);


    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        AlertDialog.Builder builder=new AlertDialog.Builder(EventSettingActivity.this);
        builder.setMessage("Any Changes made will not be applied")
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void saveButton(){
        if(eventDetailtitle.getText().toString()==null || eventDetailtitle.getText().toString().isEmpty()){
            eventDetailtitle.setError("Missing Value");
            DynamicToast.makeError(EventSettingActivity.this,"Value Missing!").show();
            return;
        }else {
            changeTitle=eventDetailtitle.getText().toString();
        }

        if(eventDetailDescription.getText().toString()==null || eventDetailDescription.getText().toString().isEmpty()){
            eventDetailDescription.setError("Missing Value");
            DynamicToast.makeError(EventSettingActivity.this,"Value Missing!").show();
            return;
        }else {
            changeDescription=eventDetailDescription.getText().toString();
        }

        if(aBooleanStartDate){

            StringBuilder stringBuilder = new StringBuilder(eventDetailStartdate.getText().toString());
            String sDate = stringBuilder.substring(4, stringBuilder.length());


            changeStartDate=sDate;
        }else {
            changeStartDate=eventDetailStartdate.getText().toString();

        }
        if(aBooleanEndDate){
            StringBuilder stringBuilder = new StringBuilder(eventDetailEndDate.getText().toString());
            String eDate = stringBuilder.substring(4, stringBuilder.length());
            changeEndDate=eDate;
        }else {
            changeEndDate=eventDetailEndDate.getText().toString();
        }
//
//        if(eventDetailDescription.getText().toString()==null || eventDetailDescription.getText().toString().isEmpty()){
//            eventDetailDescription.setError("Missing Value");
//            DynamicToast.makeError(EventSettingActivity.this,"Value Missing!").show();
//            return;
//        }else {
//
//        }

       // changeEndDate=eventDetailEndDate.getText().toString();
        changeStartTime=eventDetailStartTime.getText().toString();
        changeEndTime=eventDetailEndTime.getText().toString();
        changeLocation=eventDetailLocation.getText().toString();
        if(latitude!=null && longitude!=null){
            changeLatitude=String.valueOf(latitude);
            changeLongitude=String.valueOf(longitude);

        }else {
            changeLatitude=detail.getLatitude();
            changeLongitude=detail.getLongitude();
        }

        Log.d("ALLCHECK",changeTitle+"\n"+changeDescription+"\n"+changeStartDate+"\n"+changeEndDate+"\n"
                +changeStartTime+"\n"+changeEndTime+"\n"+changeLocation+"\n"+changeLatitude+"\n"+changeLongitude);
        if(changeTicketStatus.equals("1")){

//            if(){
//                DynamicToast.make(EventSettingActivity.this,"ON").show();
//            }
            ticketCount=String.valueOf(arrayListId.size());
            Log.d("VISI",eventDetailSingleTicketQuantity.getVisibility()+"");
            if(linearLayout2.getVisibility()==View.VISIBLE){
                changeTicketPrice = eventDetailSingleTicketPrice.getText().toString();
                    changeTicketQuantity = eventDetailSingleTicketQuantity.getText().toString();
                    changeTicketName = "NA";
                   // changeTicketId = arrayListId.get(0);
//                String name= ((TextInputEditText)recyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.card_ticket_type_edit_name)).getText().toString();
//                String price= ((TextInputEditText)recyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.card_ticket_type_edit_price)).getText().toString();
//                String quantity= ((TextInputEditText)recyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.card_ticket_type_edit_quantity)).getText().toString();
//                if(name==null || name.isEmpty() ||price==null || price.isEmpty()|| quantity==null || quantity.isEmpty() ){
//                    progressCircle.hide();
//                    DynamicToast.makeError(EventSettingActivity.this,"Ticket Information Missing!").show();
//
//                    return;
//                }else {
//                    changeTicketPrice = price;
//                    changeTicketQuantity = quantity;
//                    changeTicketName = "NA";
//                    changeTicketId = arrayListId.get(0);
//                }
            }else if(Integer.valueOf(ticketCount)>1){

                changeTicketPrice=null;
                changeTicketQuantity=null;
                changeTicketName=null;
                changeTicketId=null;
                for(int i=0;i<arrayListId.size();i++){
                   // Log.d("COUNT",i+"");
                   // Log.d("Name",arrayListName.get(i)+" ");
                  //  if(arrayListName.get(i).equals("Empty")){
                        String name= ((TextInputEditText)recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.card_ticket_type_edit_name)).getText().toString();
                        String price= ((TextInputEditText)recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.card_ticket_type_edit_price)).getText().toString();
                        String quantity= ((TextInputEditText)recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.card_ticket_type_edit_quantity)).getText().toString();

                        if(name==null || name.isEmpty() ||price==null || price.isEmpty()|| quantity==null || quantity.isEmpty() ){

                            progressCircle.hide();
                            DynamicToast.makeError(EventSettingActivity.this,"Multiple Ticket Information Missing!").show();

                            return;
                        }
                        changeTicketId = changeTicketId + "," + arrayListId.get(i);
                        changeTicketQuantity = changeTicketQuantity + "," + quantity;
                        changeTicketName = changeTicketName + "," + name;
                        changeTicketPrice = changeTicketPrice + "," + price;
//                    }else {
//                        changeTicketId = changeTicketId + "," + arrayListId.get(i);
//                        changeTicketQuantity = changeTicketQuantity + "," + arrayListQuantity.get(i);
//                        changeTicketName = changeTicketName + "," + arrayListName.get(i);
//                        changeTicketPrice = changeTicketPrice + "," + arrayListPrice.get(i);
//                    }
                }
            }else {
                progressCircle.hide();
                DynamicToast.makeError(EventSettingActivity.this,"Cant add Empty ticket").show();

                return;
            }
//            if(changeTicketId==null){
//
//            }
        }else {
            changeTicketPrice="No";
            changeTicketQuantity="No";
            changeTicketName="No";
            changeTicketId="No";
        }

        apiInterface.updateEventDetail(changeTitle,changeDescription,changeStartTime,changeEndTime,changeStartDate,changeEndDate,changeLocation,
                changeLatitude,changeLongitude,changeTicketStatus,changePageStatus,eventId,changeTicketPrice,changeTicketName,changeTicketQuantity)
                .enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse apiResponse=response.body();
                        if(!apiResponse.getError()){

                            //progressCircle.onCompleteFABAnimationEnd();

                           progressCircle.hide();
                           if(path!=null ){
                               File file = new File(path);

                               String imageName=(eventId)+"_"+"profile"+".png";
                               RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                               RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), imageName);
                               MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                               apiInterface.uploadEventProfileImage(fileToUpload,fileName).enqueue(new Callback<APIResponse>() {
                                   @Override
                                   public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                       APIResponse response1=response.body();
                                       if(!response1.getError()){
                                           progressCircle.beginFinalAnimation();
                                           DynamicToast.makeSuccess(EventSettingActivity.this,"Detail updated").show();
                                           progressCircle.hide();
                                           finish();
                                       }else {
                                           progressCircle.hide();
                                           DynamicToast.makeError(EventSettingActivity.this,"There was problem while uploading image").show();
                                       }
                                   }

                                   @Override
                                   public void onFailure(Call<APIResponse> call, Throwable t) {
                                       progressCircle.hide();
                                       DynamicToast.makeError(EventSettingActivity.this,"Image API FAILED").show();
                                   }
                               });
                           }else {
                               progressCircle.beginFinalAnimation();
                               DynamicToast.makeSuccess(EventSettingActivity.this,"Detail updated").show();
                               progressCircle.hide();
                               finish();
                           }
                           // fabProgressCircle.hide();


                        }else {
                            DynamicToast.makeError(EventSettingActivity.this,apiResponse.getErrorMsg()).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        //Log.d("ALLVAL",changeTicketStatus);
                        progressCircle.hide();
                        DynamicToast.makeError(EventSettingActivity.this,t.getLocalizedMessage()).show();
                    }
                });

        Log.d("TICKET",ticketStatus+"\n"+ticketCount+"\n"+changeTicketPrice);

       // Toast.makeText(EventSettingActivity.this, "a "+arrayListId.size(), Toast.LENGTH_SHORT).show();

    }
}
