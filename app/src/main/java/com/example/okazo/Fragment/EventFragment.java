package com.example.okazo.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.MainActivity;
import com.example.okazo.Map;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Note;
import com.example.okazo.R;
import com.example.okazo.util.EventTypeAdapter;
import com.example.okazo.util.MapEventTypeAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
public class EventFragment extends Fragment implements  PermissionsListener,OnMapReadyCallback {


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

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        View v = inflater.inflate(R.layout.fragment_event, container, false);
        SupportMapFragment mapFragment;
        apiInterface=ApiClient.getApiClient().create(ApiInterface.class);
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
                ArrayList<String>eventType=new ArrayList<>();
                ArrayList<String>eventTypeImage=new ArrayList<>();
                for (EventDetail value : eventDetail
                ){
                 eventType.add(value.getEventType());
                 eventTypeImage.add(value.getEventTypeImage());
                }
                eventType.add("More");
                eventTypeImage.add("okazo/util_image/more.png");
                RecyclerView recyclerView=v.findViewById(R.id.event_fragment_recycler_view);
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
                        Toast.makeText(context, "it is "+eventDetail.get(position), Toast.LENGTH_SHORT).show();
                    }
                });
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

        apiInterface.getEventLocation().enqueue(new Callback<ArrayList<EventDetail>>() {
            @Override
            public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
                ArrayList<EventDetail> eventDetail=response.body();
                List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
                for (EventDetail value: eventDetail
                ) {

                    lat.add(Double.valueOf(value.getLatitude()));
                    lng.add(Double.valueOf(value.getLongitude()));


                }
//                        for (int i=0;i<lat.size();i++){
//            symbolLayerIconFeatureList.add(Feature.fromGeometry(
//                    Point.fromLngLat(lat.get(i),lng.get(i))
//            ));
//            }


                        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(-57.225365, -33.213144)));
                symbolLayerIconFeatureList.add(Feature.fromGeometry(
                        Point.fromLngLat(39.551276, 93.244850)));
                        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(85.312736, 27.69939)));

                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41").withImage(ICON_ID,BitmapFactory.decodeResource(getResources(),R.drawable.mapbox_marker_icon_default))
                        .withSource(new GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))
                        .withLayer(new SymbolLayer(LAYER_ID,SOURCE_ID)
                                .withProperties(
                                        iconImage(ICON_ID),
                                        iconAllowOverlap(true),
                                        iconIgnorePlacement(true),
                                        iconOffset(new Float[]{0f,-9f})
                                )), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        UiSettings uiSettings=mapboxMap.getUiSettings();
                        uiSettings.setLogoEnabled(false);
                        uiSettings.setCompassEnabled(false);
                        
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
                //Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        for (int i=0;i<lat.size();i++){
//            symbolLayerIconFeatureList.add(Feature.fromGeometry(
//                    Point.fromLngLat(lat.get(i),lng.get(i))
//            ));

       // }


//        symbolLayerIconFeatureList.add(Feature.fromGeometry(
//                Point.fromLngLat(-54.14164, -33.981818)));
//        symbolLayerIconFeatureList.add(Feature.fromGeometry(
//                Point.fromLngLat(-56.990533, -30.583266)));



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
}



