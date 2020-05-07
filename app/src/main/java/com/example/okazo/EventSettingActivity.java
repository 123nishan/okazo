package com.example.okazo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.okazo.util.EditTicketAdapter;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_ID;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_USER_ROLE;

public class EventSettingActivity extends AppCompatActivity implements ConfirmationDialog.orderConfirmationListener {
    private FABProgressCircle fabProgressCircle;
    private ImageView imageViewProfile,imageViewChange;
    private ExpandableCardView expandableCardViewEventDetail,expandableCardViewTicketDetail,expandableCardViewEventDate,expandableCardViewEventLocation;
    private String userRole,path,eventId,latitude,longitude,ticketStatus,pagePrivate,ticketCount;
    private Switch switchPrivate,switchTicket;
    private int changeCounter=0;
    private static final int CHOOSE_IMAGE = 505;
    private ApiInterface apiInterface;
    private Uri uriProfileImage;
    private TextInputEditText eventDetailtitle,eventDetailDescription,eventDetailStartdate,eventDetailEndDate,
            eventDetailStartTime,eventDetailEndTime,eventDetailLocation,eventDetailSingleTicketPrice,eventDetailSingleTicketQuantity,textInputEditTextQuantity,textInputEditTextPrice;
    private RelativeLayout relativeLayout;
    private TextView textViewTagHeading,textViewTicketSwitchMessage,textViewAddType;
    private EventDetail detail;
    private RecyclerView recyclerViewTag;
    private String title,description,startDate,endDate,startTime,endTime,ticketNoTypePrice,ticketNoTypeQuantity;
    private Boolean aBooleanTitle=false,aBooleanDescription=false,aBooleanStartDate=false,aBooleanStartTime=false
            ,aBooleanEndTime=false,aBooleanEndDate=false,aBooleanPageStatus=false,aBooleanTicketStatus=false,aBooleanNoTypeTicketPrice=false,aBooleanNoTypeTicketQuantity=false;
    private String changeTitle,changeDescription,changeStartDate,changeEndDate,changeStartTime,
            changeEndTime,changeLocation,changePageStatus,changeTicketStatus,changeTicketPrice,changeTicketQuantity;
    private ArrayList<String> arrayListPrice,arrayListQuantity,arrayListId,arrayListName;
    private EditTicketAdapter adapter,adapter1;
    private TextView buttonAddMore;
    private LinearLayout linearLayoutNoTicket,linearLayout,linearLayout2,linearLayout1;
    private int expandCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
       // getActionBar().hide();
        setContentView(R.layout.activity_event_setting);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        arrayListId=new ArrayList<>();
        arrayListName=new ArrayList<>();
        arrayListPrice=new ArrayList<>();
        arrayListQuantity=new ArrayList<>();

        fabProgressCircle=findViewById(R.id.event_setting_activity_fabProgressCircleToolBar);
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

     //   fabProgressCircle.setVisibility(View.GONE);


        Bundle bundle=getIntent().getExtras();
        userRole=bundle.getString(KEY_USER_ROLE);
        eventId=bundle.getString(KEY_EVENT_ID);



        apiInterface.getEventAllDetail(eventId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    EventDetail eventDetail=apiResponse.getEvent();


                    detail=eventDetail;
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
                    eventDetailStartdate.setClickable(true);
                    eventDetailStartdate.setCursorVisible(true);
                    eventDetailStartdate.setFocusable(true);
                    eventDetailStartdate.setFocusableInTouchMode(true);

                    eventDetailEndDate.setText(eventDetail.getEndDate());
                    endDate=eventDetail.getEndDate();
                    eventDetailEndDate.setClickable(true);
                    eventDetailEndDate.setCursorVisible(true);
                    eventDetailEndDate.setFocusable(true);
                    eventDetailEndDate.setFocusableInTouchMode(true);

                    eventDetailStartTime.setText(eventDetail.getStartTime());
                    startTime=eventDetail.getStartTime();
                    eventDetailStartTime.setClickable(true);
                    eventDetailStartTime.setCursorVisible(true);
                    eventDetailStartTime.setFocusable(true);
                    eventDetailStartTime.setFocusableInTouchMode(true);

                    eventDetailEndTime.setText(eventDetail.getEndTime());
                    endTime=eventDetail.getEndTime();
                    eventDetailEndTime.setClickable(true);
                    eventDetailEndTime.setCursorVisible(true);
                    eventDetailEndTime.setFocusable(true);
                    eventDetailEndTime.setFocusableInTouchMode(true);


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

                        arrayListPrice=eventDetail.getTicketPrice();
                        arrayListQuantity=eventDetail.getTicketQuantity();
                        arrayListName=eventDetail.getTicketName();
                        arrayListId=eventDetail.getTicketId();
                        ticketCount=eventDetail.getTicketCount();
                        if (Integer.valueOf(ticketCount) > 1) {
                            //if multiple ticket type
                            linearLayout.setVisibility(View.GONE);
                            linearLayout1.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.GONE);

                            RecyclerView recyclerView=findViewById(R.id.expand_ticket_types_recycler_view);
                            adapter=new EditTicketAdapter(arrayListId,arrayListName,arrayListPrice,arrayListQuantity);
                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(EventSettingActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            // recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();



//                            for(int i=0;i<arrayListPrice.size();i+=1){
//                                //Log.d("PRICE",arrayListPrice.get(i));
//                            }


                        }else {
                            // if no ticket type
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




        switchTicket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    //checked

                    textViewTicketSwitchMessage.setVisibility(View.GONE);
                    if(ticketStatus.equals("1")){
                        aBooleanTicketStatus=false;
                        changeTicketStatus="1";
                        buttonAddMore.setVisibility(View.GONE);
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
                    linearLayout2.setVisibility(View.GONE);
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
                linearLayout2.setVisibility(View.GONE);
                textViewAddType.setVisibility(View.VISIBLE);
                textViewAddType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        expandableCardViewTicketDetail.collapse();
                        
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
                RecyclerView recyclerView=findViewById(R.id.expand_ticket_types_recycler_view);
                if(arrayListId.size()==0){

                    arrayListId.add("Empty");
                   arrayListName.add("Empty");
                    arrayListPrice.add("Empty");
                    arrayListQuantity.add("Empty");
                }

                adapter1=new EditTicketAdapter(arrayListId,arrayListName,arrayListPrice,arrayListQuantity);
                LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(EventSettingActivity.this);
                linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager1);
                // recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter1);
                //adapter1.notifyDataSetChanged();
                //expandableCardViewTicketDetail.expand();

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
    fabProgressCircle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("Check",changeTitle+" "+changePageStatus);
        }
    });

    }

    private void visibleSaveButton(){
        if(aBooleanTitle || aBooleanPageStatus || aBooleanDescription || aBooleanStartDate || aBooleanEndDate || aBooleanStartTime ||
                aBooleanEndTime|| aBooleanNoTypeTicketPrice || aBooleanNoTypeTicketQuantity || aBooleanTicketStatus){
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



        }
    }

    @Override
    public void OnYesClicked() {
        showImageChooser();
    }

    @Override
    public void OnNoClicked() {

    }
}
