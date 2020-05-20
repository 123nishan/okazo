package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Fragment.EventFragment;
import com.example.okazo.Fragment.EventSearchFragment;
import com.example.okazo.Fragment.HomeFragment;
import com.example.okazo.Fragment.ProfileFragment;
import com.example.okazo.util.constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import static android.util.Log.d;
import static com.example.okazo.util.constants.KEY_ID_FOR_CHAT;
import static com.example.okazo.util.constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.okazo.util.constants.PERMISSION_REQUEST_ENABLE_GPS;

public class MainActivity extends AppCompatActivity {



private FloatingActionButton floatingActionButton;
TextView textView;
//BottomNavigationView bottomNavigationView;
 public    ChipNavigationBar bottomNavigationView;
private boolean mLocationPermissionGranted=false;
SwipeRefreshLayout  swipeRefreshLayout;

private FusedLocationProviderClient mFusedLocationClient;

String userEmail,userId;

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private Boolean mRequestingLocationUpdates;

    private ResideMenu resideMenu;
    private MainActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUpMenu();
        mRequestingLocationUpdates = false;
//        textView=findViewById(R.id.textview);
//        swipeRefreshLayout=findViewById(R.id.swipe_refresh);


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {

                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("TOKEN",token);
                    }});


        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new HomeFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_message);

        }


        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(constants.KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences1.getString("user_id","")!=null && !sharedPreferences1.getString("user_id","").isEmpty()){
            userId=sharedPreferences1.getString("user_id","");
        }else {



                    userEmail = getIntent().getExtras().getString("email");
            userId = getIntent().getExtras().getString("userId");
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(constants.KEY_SHARED_PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor shared_editor = sharedPreferences.edit();
            shared_editor.putString("user_email", userEmail);
            shared_editor.putString("user_id", userId);
            shared_editor.commit();
        }
            bottomNavigationView.setItemSelected(R.id.nav_home,true);


        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                FrameLayout frameLayout;
                frameLayout = findViewById(R.id.frame_container);
                switch (i){
                    case R.id.nav_home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.pop_in, R.anim.pop_out, R.anim.pop_in, R.anim.pop_out)
                                .replace(R.id.frame_container,new HomeFragment())
                                .addToBackStack(null)
                                .commit();
                        frameLayout.removeAllViewsInLayout();
                        break;
                    case R.id.nav_event:




                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.pop_in, R.anim.pop_out, R.anim.pop_in, R.anim.pop_out)
                                .replace(R.id.frame_container,new EventFragment())
                                .addToBackStack(null)
                                .commit();
                        frameLayout.removeAllViewsInLayout();

                        break;
                    case R.id.nav_profile:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.pop_in, R.anim.pop_out, R.anim.pop_in, R.anim.pop_out)
                                .replace(R.id.frame_container,new ProfileFragment())
                                .addToBackStack(null)
                                .commit();
                        frameLayout.removeAllViewsInLayout();

                        break;
                    case R.id.nav_event_search:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.pop_in, R.anim.pop_out, R.anim.pop_in, R.anim.pop_out)
                                .replace(R.id.frame_container,new EventSearchFragment())
                                .addToBackStack(null)
                                .commit();
                        frameLayout.removeAllViewsInLayout();

                        break;
//                    case R.id.nav_shop:
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .setCustomAnimations(R.anim.pop_in, R.anim.pop_out, R.anim.pop_in, R.anim.pop_out)
//                                .replace(R.id.frame_container,new ProfileFragment())
//                                .addToBackStack(null)
//                                .commit();
//                        frameLayout.removeAllViewsInLayout();
//                        break;
                }
            }
        });
//        floatingActionButton=findViewById(R.id.add);
//
//        floatingActionButton.setOnClickListener(View ->
//                startActivity(new Intent(this,EditorActivity.class))
//                );
        //getData();


    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(MainActivity.this);
//        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.color.colorPrimary);
        resideMenu.attachToActivity(this);
       // resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        String titles[] = { "Home", "Ticket", "Message", "Reward" };
        int icon[] = { R.drawable.ic_home, R.drawable.ic_tickets, R.drawable.ic_message, R.drawable.ic_setting };

        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            int finalI = i;
            item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (titles[finalI]){
                        case "Home":
                            Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                            resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                            break;
                        case "Ticket":
                            Intent intent=new Intent(MainActivity.this,MyTicketActivity.class);
                            startActivity(intent);
                            break;
                        case "Message":
                            Intent intent1=new Intent(MainActivity.this,ChatActivity.class);
                            intent1.putExtra(KEY_ID_FOR_CHAT,userId);
                            Log.d("CHECKHERE",userId);
                            startActivity(intent1);
                            break;
                        case "Reward":
                            Intent intent2=new Intent(MainActivity.this,GeoFenceActivity.class);
                            intent2.putExtra(KEY_ID_FOR_CHAT,userId);
                            Log.d("CHECKHERE",userId);
                            startActivity(intent2);
                            break;
                    }

                }
            });
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }
//        itemHome     = new ResideMenuItem(this, R.drawable.ic_home,     "Home");
//        itemProfile  = new ResideMenuItem(this, R.drawable.ic_people_group,  "Profile");
//        itemCalendar = new ResideMenuItem(this, R.drawable.ic_calendar, "Calendar");
//        itemSettings = new ResideMenuItem(this, R.drawable.ic_setting, "Settings");



//        itemHome.setOnClickListener( this);
//        itemProfile.setOnClickListener( this);
//        itemCalendar.setOnClickListener( this);
//        itemSettings.setOnClickListener(this);

//        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
//        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }


//    private ResideMenu.OnMenuListener menuListener=new ResideMenu.OnMenuListener() {
//    @Override
//    public void openMenu() {
//        Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void closeMenu() {
//        Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
//    }
//};
    private void getLastKnowLocation(){
        //d("location","last location");
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Location location=task.getResult();
                   // Log.d("lat",String.valueOf(location.getLatitude()));
                   // Log. d("longi",String.valueOf(location.getLongitude()));

                }else {
                   // Log. d("Failed","");
                }
            }
        });
    }
    private boolean checkMapServices(){
      //if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        //}
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
           // getLastKnowLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

//    public boolean isServicesOK(){
//        d("map", "isServicesOK: checking google services version");
//
//        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
//
//        if(available == ConnectionResult.SUCCESS){
//            //everything is fine and the user can make map requests
//            d("map", "isServicesOK: Google Play Services is working");
//            return true;
//        }
//        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
//            //an error occured but we can resolve it
//            d("map", "isServicesOK: an error occured but we can fix it");
//            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
//            dialog.show();
//        }else{
//            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        mLocationPermissionGranted = false;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mLocationPermissionGranted = true;
//                }
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PERMISSION_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
//                    getChatrooms();
                   // getLastKnowLocation();
                }
                else{
                    if (mRequestingLocationUpdates && checkPermissions()) {
                        // startLocationUpdates();
                    } else if (!checkPermissions()) {
                        requestPermissions();
                    }
                   // getLocationPermission();
                }
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(isMapsEnabled()){

        }


        //getLastKnowLocation();
//        if(checkMapServices()){
////            if(mLocationPermissionGranted){
////
////            }
//
////        }else {
////                getLocationPermission();
//           }
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            // Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            // Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                // Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    // Log.i(TAG, "Permission granted, updates requested, starting location updates");
                   // startLocationUpdates();
                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }



}
