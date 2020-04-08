package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
import com.bumptech.glide.Glide;
import com.example.okazo.Model.EventDetail;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.okazo.util.constants.KEY_BUNDLE_EVENT_DETAIL;
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

public class EventDetailPreviewActivity extends AppCompatActivity {
    AppBarLayout appBarLayout;
    AppCompatImageButton buttonConfirm;

    private ImageView imageView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private static final int CHOOSE_IMAGE = 505;
    private Uri uriProfileImage;
    private Bundle bundleEventDetail,bundleTicketDetail;
 private ArrayList<EventDetail> selectedEventType=new ArrayList<EventDetail>();
 private ArrayList<TextInputEditText> editTextsList=new ArrayList<>();
    private MapView mapView;
    private MarkerViewManager markerViewManager;
 private String eventTitle,startDate,endDate,startTime,endTime,pageStatus,ticketStatus,selectedLocaition,description,latitude,longitude,
         ticketCategory,ticketTypeSingleName,ticketTypeSinglePrice,ticketTypeSingleNumber,ticketNumber,ticketPrice;
ExpandableCardView expandableCardViewEventDetail,expandableCardViewTicketDetail,expandableCardViewEventDate,expandableCardViewEventLocation;
    private static final String SOURCE_ID = "SOURCE_ID";

    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_event_detail_preview);
        appBarLayout= findViewById(R.id.event_detail_preview_app_bar);
        buttonConfirm=findViewById(R.id.event_detail_preview_confirm);
        imageView=findViewById(R.id.event_detail_image);
        expandableCardViewEventDetail=findViewById(R.id.event_detail_preview_expandable_event_detail);
        expandableCardViewTicketDetail=findViewById(R.id.event_detail_preview_expandable_ticket);
        expandableCardViewEventLocation=findViewById(R.id.event_detail_preview_expandable_event_detail_location);
         bundleEventDetail=getIntent().getBundleExtra(KEY_BUNDLE_EVENT_DETAIL);
         ticketStatus=bundleEventDetail.getString(KEY_EVENT_TICKET_STATUS);
         eventTitle=bundleEventDetail.getString(KEY_EVENT_TITLE);
         startDate=bundleEventDetail.getString(KEY_EVENT_START_DATE);
         endDate=bundleEventDetail.getString(KEY_EVENT_END_DATE);
         startTime=bundleEventDetail.getString(KEY_EVENT_START_TIME);
         endTime=bundleEventDetail.getString(KEY_EVENT_END_TIME);
         pageStatus=bundleEventDetail.getString(KEY_PAGE_STATUS);
         selectedLocaition=bundleEventDetail.getString(KEY_EVENT_SELECTED_LOCATION);
         description=bundleEventDetail.getString(KEY_EVENT_DESCRIPTION);
         latitude=bundleEventDetail.getString(KEY_LATITUDE);
         longitude=bundleEventDetail.getString(KEY_LONGITUDE);

        if(ticketStatus.toLowerCase().equals("public")){

            bundleTicketDetail=getIntent().getBundleExtra(KEY_BUNDLE_TICKET_DETAIL);
            ticketCategory=bundleTicketDetail.getString(KEY_RADIO_TICKET_CATEGORY);
            ticketTypeSingleName=bundleTicketDetail.getString(KEY_TICKET_TYPE_SINGLE_NAME);
            ticketTypeSinglePrice=bundleTicketDetail.getString(KEY_TICKET_TYPE_SINGLE_PRICE);
            ticketTypeSingleNumber=bundleTicketDetail.getString(KEY_TICKET_TYPE_SINGLE_NUMBER);
            ticketNumber=bundleTicketDetail.getString(KEY_TICKET_NUMBER);
            ticketPrice=bundleTicketDetail.getString(KEY_TICKET_PRICE);


        }else{

        }
        expandableCardViewEventDetail.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged(View v, boolean isExpanded) {
                if(isExpanded){
                   // TextView textView=v.findViewById(R.id.expand_textview);
                   // textView.setText(eventTitle);
                }
            }
        });
        expandableCardViewEventLocation.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
            @Override
            public void onExpandChanged(View v, boolean isExpanded) {
                if(isExpanded){
                    SupportMapFragment mapFragment;
                    Mapbox.getInstance(EventDetailPreviewActivity.this, getString(R.string.mapbox_access_token));
                    if(savedInstanceState==null) {
                        Log.d("latLng",latitude);
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

               }else {
                   //fully expanded
                   imageView.setVisibility(View.VISIBLE);
                   buttonConfirm.setVisibility(View.GONE);
                   collapsingToolbarLayout.setTitle("");

               }
           }
       });
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile image"), CHOOSE_IMAGE);
    }
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
                //imageView.setImageBitmap(bitmap);
               // handleUpload(bitmap);

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        markerViewManager.onDestroy();

        mapView.onDestroy();
    }

}