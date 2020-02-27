package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Fragment.EventFragment;
import com.example.okazo.Fragment.HomeFragment;
import com.example.okazo.Fragment.ProfileFragment;
import com.example.okazo.Model.Note;
import com.example.okazo.util.constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static android.util.Log.d;
import static com.example.okazo.util.constants.ERROR_DIALOG_REQUEST;
import static com.example.okazo.util.constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.okazo.util.constants.PERMISSION_REQUEST_ENABLE_GPS;

public class MainActivity extends AppCompatActivity  {
private FloatingActionButton floatingActionButton;
TextView textView;
BottomNavigationView bottomNavigationView;
private boolean mLocationPermissionGranted=false;
SwipeRefreshLayout  swipeRefreshLayout;

private FusedLocationProviderClient mFusedLocationClient;
String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView=findViewById(R.id.textview);
//        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        bottomNavigationView=findViewById(R.id.bottm_nav);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_message);

        }
        userEmail=getIntent().getExtras().getString("email");
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(constants.KEY_SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor shared_editor = sharedPreferences.edit();
        shared_editor.putString("user_email",userEmail);
        shared_editor.commit();
        String temp;
        temp=sharedPreferences.getString("user_email","");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
//                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
//                        startActivity(intent);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new HomeFragment()).commit();
                        return true;
                    case R.id.nav_event:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new EventFragment()).commit();

                        return true;
                    case R.id.nav_shop:
                        Toast.makeText(MainActivity.this, "No thing added", Toast.LENGTH_SHORT).show();
//                        Intent intent1=new Intent(getApplicationContext(),RegisterActivity.class);
//                        startActivity(intent1);

                        return true;
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new ProfileFragment()).commit();
                        return true;
                }
                return false;
            }
        });
//        floatingActionButton=findViewById(R.id.add);
//
//        floatingActionButton.setOnClickListener(View ->
//                startActivity(new Intent(this,EditorActivity.class))
//                );
        //getData();


    }
    private void getLastKnowLocation(){
        d("location","last location");
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Location location=task.getResult();
                    d("lat",String.valueOf(location.getLatitude()));
                    d("longi",String.valueOf(location.getLongitude()));

                }else {
                    d("Failed","");
                }
            }
        });
    }
    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSION_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
//            getChatrooms();
            getLastKnowLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        d("map", "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            d("map", "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            d("map", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        d("map", "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSION_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
//                    getChatrooms();
                    getLastKnowLocation();
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }
//    private void getData(){
//
//        Retrofit retrofit= ApiClient.getApiClient();
//
//        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
//        Call<List<Note>> call=apiInterface.getLocation();
//        d("entered method","1");
//        call.enqueue(new Callback<List<Note>>() {
//            @Override
//            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
//                for(Note note : response.body()){
//                     Log.d("response",note.getName());
//                     Log.d("response",note.getDate());
//                     Log.d("response",note.getLatitude().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Note>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                d("failed",t.getMessage());
//                d("response",t.toString());
//            }
//        });
//    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices()){
            if(mLocationPermissionGranted){

            }else {
                getLocationPermission();
            }
        }    }
}
