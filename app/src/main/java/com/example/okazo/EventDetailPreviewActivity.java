package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alespero.expandablecardview.ExpandableCardView;
import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.EventPreviewTicketTypeAdapter;
import com.example.okazo.util.EventTypeAdapter;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.android.core.FileUtils;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.nekoloop.base64image.Base64Image;
import com.nekoloop.base64image.RequestEncode;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import static com.example.okazo.util.constants.KEY_TAG_ARRAY;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_NAME_LIST;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_NUMBER_LIST;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_PRICE_LIST;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.okazo.util.constants.KEY_BUNDLE_TICKET_DETAIL;
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
import static com.example.okazo.util.constants.KEY_RADIO_TICKET_CATEGORY;
import static com.example.okazo.util.constants.KEY_TICKET_NUMBER;
import static com.example.okazo.util.constants.KEY_TICKET_PRICE;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_SINGLE_NAME;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_SINGLE_NUMBER;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_SINGLE_PRICE;

public class EventDetailPreviewActivity extends AppCompatActivity  {
    AppBarLayout appBarLayout;
    FloatingActionButton buttonConfirm;

    private ApiInterface apiInterface;
    private ImageView imageView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private static final int CHOOSE_IMAGE = 505;
    private Uri uriProfileImage;
    private FABProgressCircle fabProgressCircle,fabProgressCircleToolBar;
    private Bundle bundleEventDetail,bundleTicketDetail;
 private ArrayList<EventDetail> selectedEventType=new ArrayList<EventDetail>();
private  EventPreviewTicketTypeAdapter adapterListView;
    private MapView mapView;
    private String ConvertImage,path ;

    private FloatingActionButton imageButtonExpandToolBarConfirm;
    private TextView textViewExpandToolBarEventTitle;
    private MarkerViewManager markerViewManager;
    private LinearLayout linearLayoutExpandToolBarLayout;
    private RelativeLayout relativeLayoutExpandToolbarImageLayout;
 private String eventTitle,startDate,endDate,startTime,endTime,pageStatus,ticketStatus,selectedLocaition,description,latitude,longitude,
         ticketCategory,ticketTypeSingleName,ticketTypeSinglePrice,ticketTypeSingleNumber,ticketNumber,ticketPrice;
ExpandableCardView expandableCardViewEventDetail,expandableCardViewTicketDetail,expandableCardViewEventDate,expandableCardViewEventLocation;
    private static final String SOURCE_ID = "SOURCE_ID";
    private  ArrayList<String> ticketTypeNameList=new ArrayList<>(),ticketTypePriceList=new ArrayList<>(),ticketTypeNumberList=new ArrayList<>();
    private String  [] ticketPriceArray, ticketNameArray,ticketQuantityArray;
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    String sharedPreferencesConstant = "hello";
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_event_detail_preview);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(sharedPreferencesConstant, MODE_PRIVATE);
        userId=sharedPreferences.getString("user_id","");
        appBarLayout= findViewById(R.id.event_detail_preview_app_bar);
        buttonConfirm=findViewById(R.id.fabToolBar);
        fabProgressCircleToolBar=findViewById(R.id.fabProgressCircleToolBar);
        imageButtonExpandToolBarConfirm=findViewById(R.id.fab);
        fabProgressCircle=findViewById(R.id.fabProgressCircle);
        textViewExpandToolBarEventTitle=findViewById(R.id.expand_toolbar_event_title);
        relativeLayoutExpandToolbarImageLayout=findViewById(R.id.expand_toolbar_image_select_layout);
        linearLayoutExpandToolBarLayout=findViewById(R.id.expand_toolbar_layout);
        imageView=findViewById(R.id.event_detail_image);
        expandableCardViewEventDetail=findViewById(R.id.event_detail_preview_expandable_event_detail);
        expandableCardViewTicketDetail=findViewById(R.id.event_detail_preview_expandable_ticket);
        expandableCardViewEventLocation=findViewById(R.id.event_detail_preview_expandable_event_detail_location);
        expandableCardViewEventDate=findViewById(R.id.event_detail_preview_expandable_event_detail_date);
        Bundle intent=getIntent().getExtras();
       // expandableCardViewEventDetail.collapse();


         //bundleEventDetail=getIntent().getBundleExtra(KEY_BUNDLE_EVENT_DETAIL);

         eventTitle=intent.getString(KEY_EVENT_TITLE);

         startDate=intent.getString(KEY_EVENT_START_DATE);
         endDate=intent.getString(KEY_EVENT_END_DATE);
         startTime=intent.getString(KEY_EVENT_START_TIME);
         endTime=intent.getString(KEY_EVENT_END_TIME);
         pageStatus=intent.getString(KEY_PAGE_STATUS);
        ticketStatus=intent.getString(KEY_EVENT_TICKET_STATUS);
        Toast.makeText(this, ticketStatus, Toast.LENGTH_SHORT).show();
         selectedLocaition=intent.getString(KEY_EVENT_SELECTED_LOCATION);
         description=intent.getString(KEY_EVENT_DESCRIPTION);
         latitude=intent.getString(KEY_LATITUDE);
         longitude=intent.getString(KEY_LONGITUDE);
         selectedEventType= (ArrayList<EventDetail>) intent.getSerializable(KEY_TAG_ARRAY);

        textViewExpandToolBarEventTitle.setText(eventTitle);
//collapsed toolbar
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabProgressCircleToolBar.show();
                confirmDetail();

            }
        });
        //extended tool bar
        imageButtonExpandToolBarConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabProgressCircle.show();

                confirmDetail();
            }
        });

        if(ticketStatus.toLowerCase().equals("private")){
            ticketStatus="1";
            bundleTicketDetail=intent.getBundle(KEY_BUNDLE_TICKET_DETAIL);
            ticketCategory=bundleTicketDetail.getString(KEY_RADIO_TICKET_CATEGORY);

            if(ticketCategory.toLowerCase().equals("no")){
                ticketNumber=bundleTicketDetail.getString(KEY_TICKET_NUMBER);
                ticketPrice=bundleTicketDetail.getString(KEY_TICKET_PRICE);
            }else {
                bundleTicketDetail=getIntent().getBundleExtra(KEY_BUNDLE_TICKET_DETAIL);
               // editTextsList= (ArrayList<TextInputEditText>) bundleTicketDetail.getSerializable(KEY_TICKET_TYPE_LIST);
                ticketTypeSingleName=bundleTicketDetail.getString(KEY_TICKET_TYPE_SINGLE_NAME);
                ticketTypeSinglePrice=bundleTicketDetail.getString(KEY_TICKET_TYPE_SINGLE_PRICE);
                ticketTypeSingleNumber=bundleTicketDetail.getString(KEY_TICKET_TYPE_SINGLE_NUMBER);
                ticketTypeNameList= (ArrayList<String>) bundleTicketDetail.getSerializable(KEY_TICKET_TYPE_NAME_LIST);
                ticketTypePriceList= (ArrayList<String>) bundleTicketDetail.getSerializable(KEY_TICKET_TYPE_PRICE_LIST);
                ticketTypeNumberList= (ArrayList<String>) bundleTicketDetail.getSerializable(KEY_TICKET_TYPE_NUMBER_LIST);
                ticketTypeNameList.add(ticketTypeSingleName);
                ticketTypePriceList.add(ticketTypeSinglePrice);
                ticketTypeNumberList.add(ticketTypeSingleNumber);
                ticketNameArray=ticketTypeNameList.toArray(new String[0]);
                ticketPriceArray=ticketTypePriceList.toArray(new String [0]);
                ticketQuantityArray=ticketTypeNumberList.toArray(new String[0]);






            }


        }
        expandableCardViewEventDetail.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged(View v, boolean isExpanded) {
                if(isExpanded){

                }
            }
        });
//
        expandableCardViewTicketDetail.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged(View v, boolean isExpanded) {
                if(isExpanded) {

                }
            }
        });

        expandableCardViewEventDate.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged(View v, boolean isExpanded) {
                if(isExpanded){

                }
            }
        });
        TextView textInputEditTextTicket=findViewById(R.id.expand_ticket_status);
        LinearLayout linearLayoutTicketLayout=findViewById(R.id.expand_ticket_layout_yes);
        LinearLayout linearLayoutSingleLayout=findViewById(R.id.expand_ticket_single_layout);
        //private means  No ticket
        if(ticketStatus.toLowerCase().equals("public")){
            ticketStatus="0";
            textInputEditTextTicket.setText("Free Entry");
            linearLayoutTicketLayout.setVisibility(View.GONE);



        }
        //Yes  ticket
        else {
            linearLayoutTicketLayout.setVisibility(View.VISIBLE);
            textInputEditTextTicket.setText("All Ticket Details");


            LinearLayout linearLayoutNoTicketType=findViewById(R.id.expand_no_ticket_type);

            LinearLayout linearLayoutYesTicketType=findViewById(R.id.expand_yes_ticket_type);

            if(ticketCategory.toLowerCase().equals("no")){
                linearLayoutNoTicketType.setVisibility(View.VISIBLE);
                linearLayoutYesTicketType.setVisibility(View.GONE);
                linearLayoutSingleLayout.setVisibility(View.GONE);
                TextInputEditText textInputEditTextNumber=findViewById(R.id.expand_event_total_ticket);
                TextInputEditText textInputEditTextPrice=findViewById(R.id.expand_ticekt_price);
                textInputEditTextNumber.setText(ticketNumber);
                textInputEditTextPrice.setText(ticketPrice);
                //Toast.makeText(EventDetailPreviewActivity.this, "test"+ticketCategory, Toast.LENGTH_SHORT).show();

            }else {
                linearLayoutTicketLayout.setVisibility(View.VISIBLE);
                linearLayoutNoTicketType.setVisibility(View.GONE);
                linearLayoutSingleLayout.setVisibility(View.GONE);
                linearLayoutYesTicketType.setVisibility(View.VISIBLE);
//                TextView textViewTicketName=findViewById(R.id.expand_ticket_type_name);
//                TextView textViewTicketPrice=findViewById(R.id.expand_ticket_type_price);
//                TextView textViewTicketNumber=findViewById(R.id.expand_ticket_type_Number);
//                textViewTicketName.setText(ticketTypeSingleName);
//                textViewTicketPrice.setText(ticketTypeSinglePrice);
//                textViewTicketNumber.setText(ticketTypeSingleNumber);

                RecyclerView recyclerViewTicketType = findViewById(R.id.expand_ticket_types_recycler_view);

                EventPreviewTicketTypeAdapter eventPreviewTicketTypeAdapter=new EventPreviewTicketTypeAdapter(ticketTypeNameList,ticketTypeNumberList,ticketTypePriceList);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerViewTicketType.getContext(),linearLayoutManager.getOrientation());
                recyclerViewTicketType.addItemDecoration(dividerItemDecoration);
                recyclerViewTicketType.setLayoutManager(linearLayoutManager);
                recyclerViewTicketType.setAdapter(eventPreviewTicketTypeAdapter);


            }


        }

        //

        //expandable for date
        TextInputEditText textInputEditTextExpandStartDate,textInputEditTextExpandEndDate,textInputEditTextExpandStartTime,textInputEditTextExpandEndTime;
        textInputEditTextExpandEndDate=findViewById(R.id.expand_event_detail_event_end_date);
        textInputEditTextExpandStartDate=findViewById(R.id.expand_event_detail_event_start_date);
        textInputEditTextExpandStartTime=findViewById(R.id.expand_event_detail_event_start_time);
        textInputEditTextExpandEndTime=findViewById(R.id.expand_event_detail_event_end_time);
        textInputEditTextExpandEndDate.setText(endDate);
        textInputEditTextExpandStartDate.setText(startDate);
        textInputEditTextExpandStartTime.setText(startTime);
        textInputEditTextExpandEndTime.setText(endTime);

        //
//
        RecyclerView recyclerView=findViewById(R.id.expand_event_tag_recylerview);
        String parentClass="preview";
        EventTypeAdapter adapter1=new EventTypeAdapter(selectedEventType,parentClass);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(EventDetailPreviewActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);






        recyclerView.setHasFixedSize(false);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter1);
//
        TextInputEditText textInputEditTextEventTitle=findViewById(R.id.expand_event_title);
        TextInputEditText textInputEditTextEventDescription=findViewById(R.id.expand_event_description);
        textInputEditTextEventTitle.setText(eventTitle);
        textInputEditTextEventDescription.setText(description);

        TextInputEditText textInputEditTextExpandLocation;
        textInputEditTextExpandLocation=findViewById(R.id.expand_event_location_detail);
        textInputEditTextExpandLocation.setText(selectedLocaition);

        expandableCardViewEventLocation.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged(View v, boolean isExpanded) {
                if(isExpanded){

                    SupportMapFragment mapFragment;
                    Mapbox.getInstance(EventDetailPreviewActivity.this, getString(R.string.mapbox_access_token));
                    if(savedInstanceState==null) {

                        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        MapboxMapOptions options = MapboxMapOptions.createFromAttributes(EventDetailPreviewActivity.this, null);
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
                                .withImage(ICON_ID,BitmapFactory.decodeResource(EventDetailPreviewActivity.this.getResources(),R.drawable.map_default_map_marker))
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
            }
        });

        //Toast.makeText(this, "name is"+bundle1, Toast.LENGTH_SHORT).show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        collapsingToolbarLayout=findViewById(R.id.event_detail_preview_collapsing_tool_bar);
       appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
           int scrollRange=-1;
           @Override
           public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
               if(scrollRange==-1){
                   scrollRange=appBarLayout.getTotalScrollRange();
               }

               if(scrollRange+i==0){
                   imageView.setVisibility(View.GONE);
                   buttonConfirm.setVisibility(View.VISIBLE);
                   collapsingToolbarLayout.setTitle("Okazo");
                   linearLayoutExpandToolBarLayout.setVisibility(View.GONE);
                   relativeLayoutExpandToolbarImageLayout.setVisibility(View.GONE);

               }else {
                   //fully expanded
                   imageView.setVisibility(View.VISIBLE);
                   buttonConfirm.setVisibility(View.GONE);
                   collapsingToolbarLayout.setTitle("");
                   linearLayoutExpandToolBarLayout.setVisibility(View.VISIBLE);
                   relativeLayoutExpandToolbarImageLayout.setVisibility(View.VISIBLE);

               }
           }
       });
    }

    private void showImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
      startActivityForResult(intent,CHOOSE_IMAGE);
        //startActivityForResult(Intent.createChooser(intent, "Select image"), CHOOSE_IMAGE);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            uriProfileImage = data.getData();

            Glide.with(EventDetailPreviewActivity.this)
                    .load(uriProfileImage)
                    .placeholder(R.drawable.ic_place_holder_background)
                    //.error(R.drawable.ic_image_not_found_background)
                    .centerCrop()
                    .into(imageView);
           // Bitmap bitmap = null;
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor=getContentResolver().query(uriProfileImage,filePathColumn,null,null,null);
                assert cursor!=null;
                cursor.moveToFirst();

                int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
                path=cursor.getString(columnIndex);
                cursor.close();

//            Cursor cursor=getContentResolver().query(uriProfileImage,null,null,null);
//            cursor.moveToFirst();
//            String doc_id=cursor.getString(0);
//            doc_id=doc_id.substring(doc_id.lastIndexOf(":")+1);
//            cursor.close();
//            cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    null,MediaStore.Images.Media._ID+" = ? ",new String[]{doc_id},null);
//            cursor.moveToFirst();
//            String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            cursor.close();

//                bitmap= BitmapFactory.decodeFile(path);
//                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
//                byte [] b=byteArrayOutputStream.toByteArray();
//                ConvertImage=Base64.encodeToString(b,Base64.DEFAULT);
//                byte[] dataByte=Base64.decode(ConvertImage,Base64.DEFAULT);

//           File file=new File(path);
//
//            RequestBody requestBody=RequestBody.create(MediaType.parse("*/*"),file);
//            RequestBody fileName=RequestBody.create(MediaType.parse("text/plain"),file.getName());
//            MultipartBody.Part fileToUpload=MultipartBody.Part.createFormData("file",file.getName(),requestBody);
//            //RequestBody descBody=RequestBody.create(MediaType.parse("text/plain"),file.getName());
//            ApiInterface  apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//
//            apiInterface.eventCreation(fileToUpload,fileName).enqueue(new Callback<APIResponse>() {
//                @Override
//                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//
//
//                }
//
//                @Override
//                public void onFailure(Call<APIResponse> call, Throwable t) {
//
//
//                Log.d("imageError",t.getMessage());
//                }
//            });


//            Cursor cursor = null;
//            try {
//                String[] proj = { MediaStore.Images.Media.DATA };
//                cursor = getApplicationContext().getContentResolver().query(uriProfileImage,  proj, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                 Log.d("imageValue",cursor.getString(column_index));
//            } catch (Exception e) {
//                Log.d("imageValue", "getRealPathFromURI Exception : " + e.toString());
//
//            }



//            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream);
//            byte[] bytes=byteArrayOutputStream.toByteArray();
//            ConvertImage=Base64.encodeToString(bytes,Base64.DEFAULT);
//
//            Log.d("imageValue", String.valueOf(uriProfileImage));
//            Log.d("imageValue1", ConvertImage);
//            Log.d("imageValue2", bytes+"");

//            try {
//                InputStream inputStream=getContentResolver().openInputStream(uriProfileImage);
//                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }


            //imageView.setImageBitmap(bitmap);
               // handleUpload(bitmap);

        }
    }

    void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

private void confirmDetail(){
   new AlertDialog.Builder(EventDetailPreviewActivity.this)
           .setTitle("Event Confirmation")
           .setMessage("Do you want to confirm the details?")
           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   confirmedDetail();
               }
           })
           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

               }
           })
           .show();

}

private void confirmedDetail() {


    Random random = new Random();
    String moderatorId="M"+ (String.format("%06d", random.nextInt(999999)));

    apiInterface.checkModerator(userId).enqueue(new Callback<APIResponse>() {
        @Override
        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
            APIResponse apiResponse=response.body();
            String mod_status="0";
            if(!apiResponse.getError()){
                if(apiResponse.getErrorMsg()==null || apiResponse.getErrorMsg().isEmpty()){
                    mod_status="1";
                    createEvent(apiResponse.getMod_Id(),mod_status);
                    Toast.makeText(EventDetailPreviewActivity.this, "mod is present", Toast.LENGTH_SHORT).show();
                }else {

                    mod_status="0";
                    createEvent(moderatorId,mod_status);
                    Toast.makeText(EventDetailPreviewActivity.this, "create new mode", Toast.LENGTH_SHORT).show();
                }

            }else {
                DynamicToast.makeError(EventDetailPreviewActivity.this,apiResponse.getErrorMsg()).show();
            }
        }

        @Override
        public void onFailure(Call<APIResponse> call, Throwable t) {
            fabProgressCircle.hide();

        }
    });




}
private void createEvent(String modId,String mod_status){
    Random random1 = new Random();
    String eventId = "E" + (String.format("%06d", random1.nextInt(999999)));
    //Toast.makeText(this, "evet"+eve, Toast.LENGTH_SHORT).show();
    if(ticketStatus.toLowerCase().equals("private") || ticketStatus.equals("1")) {
        if (ticketCategory.toLowerCase().equals("no") || ticketCategory.equals("0")) {
            ticketCategory = "0";
        } else {
            ticketCategory = "1";
        }
    }
    StringBuilder stringBuilder = new StringBuilder(startDate);
    String sDate = stringBuilder.substring(4, stringBuilder.length() - 1);
    stringBuilder = new StringBuilder(endDate);
    String eDate = stringBuilder.substring(4, stringBuilder.length() - 1);
    //page status off means 0/public
    if(pageStatus.toLowerCase().equals("public") || pageStatus.equals("0")){
        pageStatus="0";
    }else {
        pageStatus="1";
    }

   String allTicketName=null;
    int counter=0;
    for (String value:ticketNameArray
         ) {
        if(counter==0){
            allTicketName=value;
        }else {
            allTicketName=allTicketName+","+value;
        }
        counter+=1;
    }
    String allTicketPrice=null;
    counter=0;
    for (String value:ticketPriceArray
    ) {
        if(counter==0){
            allTicketPrice=value;
        }else {
            allTicketPrice=allTicketPrice+","+value;
        }
        counter+=1;
    }
    String allTicketNumber=null;
    counter=0;
    for (String value:ticketQuantityArray
    ) {
        if(counter==0){
            allTicketNumber=value;
        }else {
            allTicketNumber=allTicketNumber+","+value;
        }
        counter+=1;
    }
    apiInterface.eventCreation(eventTitle,eventId,description,startTime,endTime,sDate,eDate,selectedLocaition,latitude,longitude,
            ticketStatus,pageStatus,userId,ticketCategory,modId,ticketPrice,ticketNumber,allTicketPrice,allTicketName,allTicketNumber,mod_status).enqueue(new Callback<APIResponse>() {
        @Override
        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
            APIResponse eventDetail=response.body();
            if(!eventDetail.getError()){

                if (path == null) {
                    fabProgressCircle.hide();
                    fabProgressCircleToolBar.hide();
                    Toast.makeText(EventDetailPreviewActivity.this, "uploading information", Toast.LENGTH_SHORT).show();
                } else {


                    File file = new File(path);

                    String imageName=(eventDetail.getEvent_id())+"_"+"profile"+".png";
                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                    RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), imageName);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    apiInterface.uploadEventProfileImage(fileToUpload,fileName).enqueue(new Callback<APIResponse>() {
                        @Override
                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                            APIResponse response1=response.body();
                            if(!response1.getError()){
                                fabProgressCircle.beginFinalAnimation();
                                fabProgressCircleToolBar.beginFinalAnimation();
                                Intent  intent=new Intent(getApplicationContext(),MainActivity.class);
                                DynamicToast.makeSuccess(EventDetailPreviewActivity.this,"Event is Created").show();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);

                            }else {
                                fabProgressCircle.hide();
                                fabProgressCircleToolBar.hide();
                                DynamicToast.makeError(EventDetailPreviewActivity.this,"There was problem while uploading image").show();
                            }
                        }

                        @Override
                        public void onFailure(Call<APIResponse> call, Throwable t) {
                            DynamicToast.makeError(EventDetailPreviewActivity.this,"Failed To upload detail").show();
                        }
                    });
                }
            }else {
                fabProgressCircle.hide();
                fabProgressCircleToolBar.hide();
                Toast.makeText(EventDetailPreviewActivity.this, eventDetail.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<APIResponse> call, Throwable t) {
            fabProgressCircle.hide();
            fabProgressCircleToolBar.hide();
            Toast.makeText(EventDetailPreviewActivity.this, "test+ "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}

}
