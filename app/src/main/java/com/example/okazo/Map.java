package com.example.okazo;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class Map extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMap;
   private static final int Request_code=101;
   Location mLocation;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(googleServicesAvailable()) {
            setContentView(R.layout.activity_map);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            getLocation();
        }else {
            Toast.makeText(this, "Map is not supported", Toast.LENGTH_SHORT).show();
        }

    }

    private void getLocation() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},Request_code

            );
            return;
        }
        Task<Location>task=fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    mLocation=location;

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_service_center);
                    supportMapFragment.getMapAsync(Map.this);

//                    Toast.makeText(Map.this, "latlng"+latLng, Toast.LENGTH_SHORT).show();


//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                }
            }
        });
    }


    //check if play services is installed or not
    public boolean googleServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (apiAvailability.isUserResolvableError(isAvailable)) {
            Dialog dialog = apiAvailability.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "cant connect to play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("google map","");
        this.googleMap = googleMap;
        Double lat=mLocation.getLatitude();
        Double longi=mLocation.getLongitude();
        LatLng latLng=new LatLng(lat,longi);

        googleMap.addMarker(new MarkerOptions().position(latLng).title("Your location")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                googleMap.setMyLocationEnabled(true);


        LatLng bhaktapur=new LatLng(27.67352,85.3877);
        LatLng Imadol=new LatLng(27.66193,85.34179);
        LatLng Nakhipot=new LatLng(27.65711,85.32204);
        LatLng Bhaisepati=new LatLng(27.64922,85.30012);
        LatLng Pepsicola=new LatLng(27.69065,85.36512);
        LatLng Koteshwor=new LatLng(27.68286,85.34338);
        LatLng Sankhamul=new LatLng(27.68191,85.32968);
        LatLng Jhamsikhel=new LatLng(27.67733,85.30869);
        LatLng Balkhu=new LatLng(27.68503,85.29338);
        LatLng Kalanki=new LatLng(27.69006,85.27205);
        LatLng MidBaneshwor=new LatLng(27.69558,85.33833);
        LatLng Teku=new LatLng(27.69525,85.30965);
        LatLng Durbarmarg=new LatLng(27.71134,85.31748);
        LatLng Sorakhutte=new LatLng(27.7185,85.3056);
        LatLng Banasthali=new LatLng(27.72595,85.29922);
        LatLng Chabahil=new LatLng(27.72107,85.35255);
        LatLng Samakhusi =new LatLng(27.74083,85.32138);
        LatLng Golfutar =new LatLng(27.75053,85.34548);

        googleMap.addMarker(new MarkerOptions().position(bhaktapur).title("GarageInc, Bhaktapur"));
        googleMap.addMarker(new MarkerOptions().position(Imadol).title("GarageInc, Imadol"));
        googleMap.addMarker(new MarkerOptions().position(Nakhipot).title("GarageInc, Nakhipot"));
        googleMap.addMarker(new MarkerOptions().position(Bhaisepati).title("GarageInc, Bhaisepati"));
        googleMap.addMarker(new MarkerOptions().position(Pepsicola).title("GarageInc, Pepsicola"));
        googleMap.addMarker(new MarkerOptions().position(Koteshwor).title("GarageInc, Koteshwor"));
        googleMap.addMarker(new MarkerOptions().position(Sankhamul).title("GarageInc, Sankhamul"));
        googleMap.addMarker(new MarkerOptions().position(Jhamsikhel).title("GarageInc, Jhamsikhel"));
        googleMap.addMarker(new MarkerOptions().position(Balkhu).title("GarageInc, Balkhu"));
        googleMap.addMarker(new MarkerOptions().position(Kalanki).title("GarageInc, Kalanki"));
        googleMap.addMarker(new MarkerOptions().position(MidBaneshwor).title("GarageInc, Mid Baneshwor"));
        googleMap.addMarker(new MarkerOptions().position(Teku).title("GarageInc, Mid Teku"));
        googleMap.addMarker(new MarkerOptions().position(Durbarmarg).title("GarageInc, Mid Durbarmarg"));
        googleMap.addMarker(new MarkerOptions().position(Sorakhutte).title("GarageInc, Mid Sorakhutte"));
        googleMap.addMarker(new MarkerOptions().position(Banasthali).title("GarageInc, Mid Banasthali"));
        googleMap.addMarker(new MarkerOptions().position(Chabahil).title("GarageInc, Mid Chabahil"));
        googleMap.addMarker(new MarkerOptions().position(Samakhusi ).title("GarageInc, Mid Samakhusi "));
        googleMap.addMarker(new MarkerOptions().position(Golfutar ).title("GarageInc, Mid Golfutar "));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.mapTypeNone:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_code:
                if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }
                break;
        }
    }
}
