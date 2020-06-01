package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_EVENT_TITLE;
import static com.example.okazo.util.constants.KEY_IMAGE;
import static com.example.okazo.util.constants.KEY_IMAGE_ADDRESS;
import static com.example.okazo.util.constants.KEY_LATITUDE;
import static com.example.okazo.util.constants.KEY_LONGITUDE;
import static com.example.okazo.util.constants.KEY_PLACE;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {
    private MapView mapView;
    private static final String MAKI_ICON_CAFE = "cafe-15";
    private static final String MAKI_ICON_HALL = "town-hall-15";
    private static final String MAKI_BUILDING = "building-15";
    private SymbolManager symbolManager;
    private Symbol symbol;
    private double lat,lng;
    private String image,title,place;
    private TextView textViewTitle,textViewPlace,textViewNavigation;
    private CircleImageView circleImageView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private CardView cardViewLocation;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private LocationComponent locationComponent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_location);
       mapView=findViewById(R.id.location_mapView);
       textViewPlace=findViewById(R.id.location_place);
       textViewTitle=findViewById(R.id.location_title);
       circleImageView=findViewById(R.id.location_image);
       cardViewLocation=findViewById(R.id.location_card);
       textViewNavigation=findViewById(R.id.location_navigation);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::onMapReady);
        Bundle bundle=getIntent().getExtras();
        lat=Double.valueOf(bundle.getString(KEY_LATITUDE));
        lng=Double.valueOf(bundle.getString(KEY_LONGITUDE));
        image=bundle.getString(KEY_IMAGE);
        title=bundle.getString(KEY_EVENT_TITLE);
        place=bundle.getString(KEY_PLACE);

        String imagePath=KEY_IMAGE_ADDRESS+(image);
        Glide.with(getApplicationContext())
                .load(Uri.parse(imagePath))
                .placeholder(R.drawable.ic_okazo_logo_background)
                .error(R.drawable.ic_okazo_logo_background)
                .centerCrop()
                .into(circleImageView);
        textViewTitle.setText(title);
        textViewPlace.setText(place);

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
       LocationActivity.this.mapboxMap=mapboxMap;
        mapboxMap.getUiSettings().setAttributionEnabled(false);
        mapboxMap.getUiSettings().setLogoEnabled(false);
        mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                symbolManager =new SymbolManager(mapView,mapboxMap,style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setTextAllowOverlap(true);
                symbol=symbolManager.create(new SymbolOptions()
                .withLatLng(new LatLng(lat,lng))
                .withIconImage(MAKI_ICON_HALL)
                        .withIconSize(2.0f)
                        .withDraggable(false)
                );
            textViewNavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean simulateRoute = true;
                    NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                            .directionsRoute(currentRoute)
                            .shouldSimulateRoute(simulateRoute)
                            .build();
                    NavigationLauncher.startNavigation(LocationActivity.this, options);
                }
            });
                symbolManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public void onAnnotationClick(Symbol symbol) {

                        Point originPoint=
                                Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                                        locationComponent.getLastKnownLocation().getLatitude());
                        Point destinationPoint=Point.fromLngLat(lng,lat);
                        getRoute(originPoint,destinationPoint);
                        cardViewLocation.setVisibility(View.VISIBLE);


                    }
                });
            }
        });
    }

    private void enableLocationComponent(Style style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
             locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, style).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }


    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response

                        if (response.body() == null) {
//                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
//                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);


// Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
//                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        DynamicToast.makeWarning(getApplicationContext(),"location permission is need for this feature").show();
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
            DynamicToast.makeWarning(this, "Permission not granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
