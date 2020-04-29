package com.example.okazo.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.collection.LongSparseArray;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.EventActivity;
import com.example.okazo.EventDetailPreviewActivity;
import com.example.okazo.LoginActivity;
import com.example.okazo.MainActivity;
import com.example.okazo.Map;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Note;
import com.example.okazo.R;

import com.example.okazo.util.EventTypeAdapter;
import com.example.okazo.util.EventTypeCollectionAdapter;
import com.example.okazo.util.MapEventTypeAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.VectorSource;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static com.example.okazo.util.constants.KEY_EVENT_DETAIL;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_USER_ID;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static android.util.Log.d;
import static com.example.okazo.util.constants.MAPVIEW_BUNDLE_KEY;
import static com.mapbox.mapboxsdk.maps.Style.OUTDOORS;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements  PermissionsListener,OnMapReadyCallback, MapboxMap.OnMapClickListener {


    public EventFragment() {
        // Required empty public constructor
    }
    private MapboxMap mapboxMap;
    private ApiInterface apiInterface;
    private PermissionsManager permissionsManager;
    private ArrayList<String> arrayListEventType;
    private MapEventTypeAdapter adapter;

    public ArrayList<Double> lat=new ArrayList<>();
    public ArrayList<Double> lng=new ArrayList<>();
    private MapView mapView;
    private CardView cardView;
    private  TextView textViewTitle,textViewStartDate,textViewStartTime,textViewLocation,textViewMore,textViewHost;
    private CircleImageView imageView;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    private FeatureCollection featureCollection;
    private HashMap<String,String> hashMap=new HashMap<String,String>();

    private    SymbolManager symbolManager;

private  List<Symbol> symbolList=new ArrayList<>();
    ArrayList<String>eventType=new ArrayList<>();
    ArrayList<String>eventTypeImage=new ArrayList<>();
    ArrayList<String>allEventType=new ArrayList<>(),allEventTypeImage=new ArrayList<>();
    private String selectedEventId;
    private String userId;
    private EventDetail eventDetailIntent;
    private String mapEventType;
    private EventTypeCollectionAdapter collectionAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        View v = inflater.inflate(R.layout.fragment_event, container, false);
        SupportMapFragment mapFragment;
        apiInterface=ApiClient.getApiClient().create(ApiInterface.class);
        cardView=v.findViewById(R.id.event_fragment_card);
        cardView.setVisibility(View.GONE);

        textViewTitle=v.findViewById(R.id.event_fragment_card_title);
        textViewStartDate=v.findViewById(R.id.event_fragment_card_date);
        textViewStartTime=v.findViewById(R.id.event_fragment_card_time);
        imageView=v.findViewById(R.id.event_fragment_card_image_view);
        textViewLocation=v.findViewById(R.id.event_fragment_card_location);
        textViewMore=v.findViewById(R.id.event_fragment_card_more);
        textViewHost=v.findViewById(R.id.event_fragment_card_host);
        eventType.add("All");
        eventTypeImage.add("okazo/util_image/all.png");

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){
            userId=sharedPreferences.getString("user_id","");

        }else {
            DynamicToast.makeError(getActivity().getApplicationContext(),"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_email","");
            editor.putString("user_id","");
            Intent intent=new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        hashMap.put("food","fast-food-15");
        hashMap.put("music","music-15");
        hashMap.put("dance","pitch-15");
        hashMap.put("sports","soccer-15");
        hashMap.put("party","bar-15");
        hashMap.put("educational","library-15");
        hashMap.put("automobile","car-15");

        MainActivity mainActivity= (MainActivity) this.getActivity();
        ActionBar bar=mainActivity.getSupportActionBar();
        bar.hide();
        mapView = v.findViewById(R.id.event_fragent_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

       // Log.d("checkHere",lat.size()+"nishan"+lng.size());


//        if(savedInstanceState==null){
//            final FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
//            MapboxMapOptions options=MapboxMapOptions.createFromAttributes(getActivity(),null);
//            options.camera(new CameraPosition.Builder().target(new LatLng(38.899895,-7703401))
//            .zoom(13)
//                    .build()
//            );
//            mapFragment=SupportMapFragment.newInstance(options);
//            transaction.add(R.id.map_box_frame,mapFragment,"com.mapbox.map");
//            transaction.commit();
//
//
//        }else {
//            mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentByTag("com.mapbox.map");
//        }
//        if(mapFragment!=null){
//            mapFragment.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(@NonNull MapboxMap mapboxMap) {
//                   EventFragment.this.mapboxMap=mapboxMap;
//                   mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
//                       @Override
//                       public void onStyleLoaded(@NonNull Style style) {
//                        enableLocationComponent(style);
//                          // MarkerViewManager markerViewManager = new MarkerViewManager(mapFragment, mapboxMap);
//                       }
//                   });
//                }
//            });
//        }
        apiInterface.getMapEventType().enqueue(new Callback<ArrayList<EventDetail>>() {
            @Override
            public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
                ArrayList<EventDetail> eventDetail=response.body();

                for (EventDetail value : eventDetail
                ){
                 eventType.add(value.getEventType());
                 eventTypeImage.add(value.getEventTypeImage());
                }
                eventType.add("More");
                eventTypeImage.add("okazo/util_image/more.png");

            }

            @Override
            public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        call=apiInterface.getLocation();
        return v;
    }
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

// Get an instance of the LocationComponent.
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate the LocationComponent
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle).build());

// Enable the LocationComponent so that it's actually visible on the map
            locationComponent.setLocationComponentEnabled(true);

// Set the LocationComponent's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the LocationComponent's render mode
            locationComponent.setRenderMode(RenderMode.NORMAL);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getActivity(), "PERMISISON NOT GRANTED", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
     this.mapboxMap = mapboxMap;
     mapEventType="All";
        apiInterface.getEventLocation(userId,mapEventType).enqueue(new Callback<ArrayList<EventDetail>>() {
            @Override
            public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
                ArrayList<EventDetail> eventDetail=response.body();
                //List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
                for (EventDetail value: eventDetail
                ) {
                    lat.add(Double.valueOf(value.getLatitude()));
                    lng.add(Double.valueOf(value.getLongitude()));
                }
                          mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                       // HashMap <String,Symbol> symbolHashMap=new HashMap<>();
                        mapboxMap.addOnMapClickListener(EventFragment.this::onMapClick);
                        UiSettings uiSettings=mapboxMap.getUiSettings();
                        uiSettings.setLogoEnabled(false);
                        uiSettings.setCompassEnabled(false);

                        List<SymbolOptions> symbolOptions=new ArrayList<>();
                        addSymbol(style,eventDetail,symbolOptions);
//                       for(int i=0;i<lat.size();i++){
//                           int counter=i;
//                         symbolManager=new SymbolManager(mapView,mapboxMap,style);
//                           symbolManager.setIconAllowOverlap(true);
//                           symbolManager.setIconIgnorePlacement(true);
//                           if(eventDetail.get(i).getStatus().equals("1")) {
//                               if (eventDetail.get(i).getTagCount() > 1) {
//
//                                   symbolOptions.add(new SymbolOptions()
//                                           .withLatLng(new LatLng(lat.get(i), lng.get(i)))
//                                           .withIconImage("town-hall-15")
//                                           .withIconSize(2.0f));
//                               } else {
//
//                                   String eventType = (eventDetail.get(i).getTags()).toLowerCase();
//                                   String value = hashMap.get(eventType);
//
//                                   symbolOptions.add(new SymbolOptions()
//                                           .withLatLng(new LatLng(lat.get(i), lng.get(i)))
//                                           .withIconImage(value)
//                                           .withIconSize(2.0f));
//                               }
//
//                               cardView.setOnClickListener(new View.OnClickListener() {
//                                   @Override
//                                   public void onClick(View view) {
//                                        Intent intent=new Intent(getActivity().getApplicationContext(), EventActivity.class);
//                                        intent.putExtra(KEY_EVENT_DETAIL,eventDetailIntent);
//                                        intent.putExtra(KEY_USER_ID,userId);
//                                        startActivity(intent);
//
//                                   }
//                               });
//                           }
//                       }
                       symbolManager.create(symbolOptions);
                       symbolClick(eventDetail);

//                       symbolManager.addClickListener(new OnSymbolClickListener() {
//                           @Override
//                           public void onAnnotationClick(Symbol symbol) {
//                               int latLen,lngLen;
//                               String selectedLat= String.valueOf(symbol.getLatLng().getLatitude());
//                               String selectedLng= String.valueOf(symbol.getLatLng().getLongitude());
//                               latLen=selectedLat.length();
//                               lngLen=selectedLng.length();
//
//                            if(latLen<9){
//                                selectedLat=selectedLng+0;
//
//                            }
//                            if(lngLen<9){
//
//                                selectedLng=selectedLng+0;
//                            }
//
//                               for (EventDetail val:eventDetail
//                                    ) {
//                                   String lat=val.getLatitude();
//                                   String lng=val.getLongitude();
//                                   if(selectedLat.equals(lat) && selectedLng.equals(lng)){
//                                       eventDetailIntent=val;
//                                            cardView.setVisibility(View.VISIBLE);
//                                            textViewTitle.setText(val.getTitle());
//                                            textViewStartDate.setText(val.getStartDate());
//                                            textViewStartTime.setText(val.getStartTime());
//                                            textViewLocation.setText(val.getPlace());
//                                            textViewHost.setText(val.getHostName());
//                                       String imagePath=KEY_IMAGE_ADDRESS+(val.getImage());
//                                       Glide.with(getActivity().getApplicationContext())
//                                               .load(Uri.parse(imagePath))
//                                               .placeholder(R.drawable.ic_place_holder_background)
//                                               //.error(R.drawable.ic_image_not_found_background)
//                                               .centerCrop()
//                                               .into(imageView);
//
//                                       break;
//                                   }
//                               }
//
//                           }
//                       });
                        RecyclerView recyclerView=getView().findViewById(R.id.event_fragment_recycler_view);
                        String parentClass="eventFragment";
                        Context context=getActivity().getApplicationContext();
                        adapter=new MapEventTypeAdapter(eventType,eventTypeImage,context);
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
                        adapter.setOnClickListener(new MapEventTypeAdapter.OnClickListener() {
                            @Override
                            public void OnClick(int position, ArrayList<String> eventDetail) {

                                String eventTypeString=eventDetail.get(position);
                                if(eventTypeString.equals("More")){
                                    allEventType.clear();
                                    allEventTypeImage.clear();
                                    View dialog=getLayoutInflater().inflate(R.layout.bottom_sheet_event_type,null);
                                    BottomSheetDialog sheetDialog=new BottomSheetDialog(getContext());
                                    sheetDialog.setContentView(dialog);
                                    sheetDialog.show();
                                    apiInterface.getEventType().enqueue(new Callback<ArrayList<EventDetail>>() {
                                        @Override
                                        public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
                                            ArrayList<EventDetail> eventDetails=response.body();
                                            RecyclerView recyclerView1=dialog.findViewById(R.id.bottom_sheet_event_type_recycleview);
                                            for (EventDetail val:eventDetails
                                                 ) {
                                                allEventType.add(val.getEventType());
                                                allEventTypeImage.add(val.getEventTypeImage());

                                            }
                                            collectionAdapter=new EventTypeCollectionAdapter(allEventType,allEventTypeImage,getActivity().getApplicationContext());
                                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(dialog.getContext(), 2);
                                            recyclerView1.setLayoutManager(mLayoutManager);
                                            recyclerView1.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                                            recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                            recyclerView1.setAdapter(collectionAdapter);
                                            collectionAdapter.setOnClickListener(new EventTypeCollectionAdapter.OnClickListener() {
                                                @Override
                                                public void OnClick(int position, ArrayList<String> eventType) {
                                                    String selectedEventType=eventType.get(position);
                                                    selectEventType(selectedEventType,symbolOptions,style);
                                                    sheetDialog.hide();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {

                                        }
                                    });
                                }else {

                                    cardView.setVisibility(View.GONE);

                                            selectEventType(eventTypeString,symbolOptions,style);


                                    //symbolManager.create(symbolOptions);
////                        Toast.makeText(context, "a"+position, Toast.LENGTH_SHORT).show();
//                        Log.d("omen",symbolList.get(0)+" a");
//                       // cardView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    //    Log.d("nishan",eventType.get(0));
//                        SymbolManager symbolManager=new SymbolManager(mapView,mapboxMap,style);
//                        symbolManager.setIconAllowOverlap(true);
//                        symbolManager.setIconIgnorePlacement(true);
//                        Symbol symbol = symbolManager.create(new SymbolOptions()
//                                .withLatLng(new LatLng(27.699399, 85.312736))
//                                .withIconImage(ICON_ID)
//                                .withIconSize(1.0f));

//                          MarkerViewManager markerViewManager = new MarkerViewManager(mapView, mapboxMap);
//
//                           MarkerView markerView=new MarkerView(new LatLng(27.699933,85.312736),);
                    }

                });
//

                //Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        cardView.setVisibility(View.GONE);
        return false;
    }
    private void symbolClick(ArrayList<EventDetail> eventDetail){
        symbolManager.addClickListener(new OnSymbolClickListener() {
            @Override
            public void onAnnotationClick(Symbol symbol) {
                int latLen,lngLen;
                String selectedLat= String.valueOf(symbol.getLatLng().getLatitude());
                String selectedLng= String.valueOf(symbol.getLatLng().getLongitude());
                latLen=selectedLat.length();
                lngLen=selectedLng.length();

                if(latLen<9){
                    selectedLat=selectedLng+0;

                }
                if(lngLen<9){

                    selectedLng=selectedLng+0;
                }

                for (EventDetail val:eventDetail
                ) {
                    String lat=val.getLatitude();
                    String lng=val.getLongitude();
                    if(selectedLat.equals(lat) && selectedLng.equals(lng)){
                        eventDetailIntent=val;
                        cardView.setVisibility(View.VISIBLE);
                        textViewTitle.setText(val.getTitle());
                        textViewStartDate.setText(val.getStartDate());
                        textViewStartTime.setText(val.getStartTime());
                        textViewLocation.setText(val.getPlace());
                        textViewHost.setText(val.getHostName());
                        String imagePath=KEY_IMAGE_ADDRESS+(val.getImage());
                        Glide.with(getActivity().getApplicationContext())
                                .load(Uri.parse(imagePath))
                                .placeholder(R.drawable.ic_place_holder_background)
                                //.error(R.drawable.ic_image_not_found_background)
                                .centerCrop()
                                .into(imageView);

                        break;
                    }
                }

            }
        });
    }
    private void addSymbol(Style style, ArrayList<EventDetail> eventDetail, List<SymbolOptions> symbolOptions)
    {
        for(int i=0;i<lat.size();i++){
            int counter=i;
            symbolManager=new SymbolManager(mapView,mapboxMap,style);
            symbolManager.setIconAllowOverlap(true);
            symbolManager.setIconIgnorePlacement(true);
            if(eventDetail.get(i).getStatus().equals("1")) {
                if (eventDetail.get(i).getTagCount() > 1) {

                    symbolOptions.add(new SymbolOptions()
                            .withLatLng(new LatLng(lat.get(i), lng.get(i)))
                            .withIconImage("town-hall-15")
                            .withIconSize(2.0f));
                } else {

                    String eventType = (eventDetail.get(i).getTags()).toLowerCase();
                    String value = hashMap.get(eventType);

                    symbolOptions.add(new SymbolOptions()
                            .withLatLng(new LatLng(lat.get(i), lng.get(i)))
                            .withIconImage(value)
                            .withIconSize(2.0f));
                }

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity().getApplicationContext(), EventActivity.class);
                        intent.putExtra(KEY_EVENT_DETAIL,eventDetailIntent);
                        intent.putExtra(KEY_USER_ID,userId);
                        startActivity(intent);

                    }
                });
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    private void selectEventType(String eventType, List<SymbolOptions> symbolOptions, Style style){
        apiInterface.checkEventType(eventType).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    Integer eventTypeCount= Integer.valueOf(apiResponse.getEvent().getTotalEvent());
                    if(eventTypeCount>0){
                        symbolOptions.clear();
                        symbolManager.deleteAll();
                        apiInterface.getEventLocation(userId, eventType).enqueue(new Callback<ArrayList<EventDetail>>() {
                            @Override
                            public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
                                ArrayList<EventDetail> eventDetail = response.body();

                                lat.clear();
                                lng.clear();

                                for (EventDetail value : eventDetail
                                ) {

                                    lat.add(Double.valueOf(value.getLatitude()));
                                    lng.add(Double.valueOf(value.getLongitude()));
                                    Log.d("hereNishan", value.getTitle());

                                }
                                // List<SymbolOptions> symbolOptions=new ArrayList<>();
                                addSymbol(style, eventDetail, symbolOptions);
//                                          for(int i=0;i<lat.size();i++){
//
//                                              symbolManager=new SymbolManager(mapView,mapboxMap,style);
//                                              symbolManager.setIconAllowOverlap(true);
//                                              symbolManager.setIconIgnorePlacement(true);
//                                              if(eventDetail.get(i).getStatus().equals("1")) {
//                                                  if (eventDetail.get(i).getTagCount() > 1) {
//
//                                                      symbolOptions.add(new SymbolOptions()
//                                                              .withLatLng(new LatLng(lat.get(i), lng.get(i)))
//                                                              .withIconImage("town-hall-15")
//                                                              .withIconSize(2.0f));
//                                                  } else {
//
//                                                      String eventType = (eventDetail.get(i).getTags()).toLowerCase();
//                                                      String value = hashMap.get(eventType);
//
//                                                      symbolOptions.add(new SymbolOptions()
//                                                              .withLatLng(new LatLng(lat.get(i), lng.get(i)))
//                                                              .withIconImage(value)
//                                                              .withIconSize(2.0f));
//                                                  }
//
//                                                  cardView.setOnClickListener(new View.OnClickListener() {
//                                                      @Override
//                                                      public void onClick(View view) {
//                                                          Intent intent=new Intent(getActivity().getApplicationContext(), EventActivity.class);
//                                                          intent.putExtra(KEY_EVENT_DETAIL,eventDetailIntent);
//                                                          intent.putExtra(KEY_USER_ID,userId);
//                                                          startActivity(intent);
//
//                                                      }
//                                                  });
//                                              }
//                                          }
                                symbolManager.create(symbolOptions);
                                symbolClick(eventDetail);
//                                          symbolManager.addClickListener(new OnSymbolClickListener() {
//                                              @Override
//                                              public void onAnnotationClick(Symbol symbol) {
//                                                  int latLen,lngLen;
//                                                  String selectedLat= String.valueOf(symbol.getLatLng().getLatitude());
//                                                  String selectedLng= String.valueOf(symbol.getLatLng().getLongitude());
//                                                  latLen=selectedLat.length();
//                                                  lngLen=selectedLng.length();
//
//                                                  if(latLen<9){
//                                                      selectedLat=selectedLng+0;
//
//                                                  }
//                                                  if(lngLen<9){
//
//                                                      selectedLng=selectedLng+0;
//                                                  }
//
//                                                  for (EventDetail val:eventDetail
//                                                  ) {
//                                                      String lat=val.getLatitude();
//                                                      String lng=val.getLongitude();
//                                                      if(selectedLat.equals(lat) && selectedLng.equals(lng)){
//                                                          eventDetailIntent=val;
//                                                          cardView.setVisibility(View.VISIBLE);
//                                                          textViewTitle.setText(val.getTitle());
//                                                          textViewStartDate.setText(val.getStartDate());
//                                                          textViewStartTime.setText(val.getStartTime());
//                                                          textViewLocation.setText(val.getPlace());
//                                                          textViewHost.setText(val.getHostName());
//                                                          String imagePath=KEY_IMAGE_ADDRESS+(val.getImage());
//                                                          Glide.with(getActivity().getApplicationContext())
//                                                                  .load(Uri.parse(imagePath))
//                                                                  .placeholder(R.drawable.ic_place_holder_background)
//                                                                  //.error(R.drawable.ic_image_not_found_background)
//                                                                  .centerCrop()
//                                                                  .into(imageView);
//
//                                                          break;
//                                                      }
//                                                  }
//
//                                              }
//                                          });

                            }

                            @Override
                            public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {
                                DynamicToast.makeError(getActivity().getApplicationContext(), t.getLocalizedMessage()).show();
                            }
                        });
                    }else {
                        DynamicToast.makeError(getActivity().getApplicationContext(),"No Event of this category").show();
                    }
                }else {
                    DynamicToast.makeError(getActivity().getApplicationContext(),apiResponse.getErrorMsg()).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }
}



