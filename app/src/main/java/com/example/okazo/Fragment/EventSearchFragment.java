package com.example.okazo.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.BuildConfig;
import com.example.okazo.EventActivity;
import com.example.okazo.LoginActivity;
import com.example.okazo.MessageActivity;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.R;
import com.example.okazo.util.EventSearchDiscoverAdapter;
import com.example.okazo.util.EventTypeCollectionAdapter;
import com.example.okazo.util.MessageAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.okazo.util.constants.KEY_EVENT_DETAIL;
import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventSearchFragment extends Fragment {

    public EventSearchFragment() {
        // Required empty public constructor
    }
    private RecyclerView recyclerView;





    private String userId;
    private Double userLatitude,userLongitude;
    private ApiInterface apiInterface;
    private EventSearchDiscoverAdapter discoverAdapter;
    private ArrayList<EventDetail> eventDetailDiscover=new ArrayList<>();
    private ArrayList<String> arrayListId=new ArrayList<>(),arrayListTitle=new ArrayList<>(),arrayListLocation=new ArrayList<>(),
            arrayListImage=new ArrayList<>(),arrayListDate=new ArrayList<>(),arrayListTime=new ArrayList<>(),arrayListHost=new ArrayList<>(),
            arrayListDistance=new ArrayList<>(),arrayListLatitude=new ArrayList<>(),arrayListLongitude=new ArrayList<>();
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    int PERMISSION_ID = 44;
    DecimalFormat decimalFormat;
    private ImageView imageViewFilter;
    private CardView cardViewFilter;
    private TextView textViewFilterCategory,textViewFilterReset;
    ArrayList<String>allEventType=new ArrayList<>(),allEventTypeImage=new ArrayList<>();
    private EventTypeCollectionAdapter collectionAdapter;
    private String totalCount="";
    private ImageView imageViewFilterClose;
    private EventDetail eventDetailIntent;
    private ArrayList<EventDetail> arrayListEventDetail=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_event_search, container, false);
        recyclerView=view.findViewById(R.id.event_search_fragment_discover_recyclerview);
        decimalFormat=new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        imageViewFilter=view.findViewById(R.id.event_search_fragment_filter);
        cardViewFilter=view.findViewById(R.id.event_search_fragment_filter_card);
        textViewFilterCategory=view.findViewById(R.id.event_search_fragment_filter_category);
        imageViewFilterClose=view.findViewById(R.id.event_search_fragment_filter_close);
        textViewFilterReset=view.findViewById(R.id.event_search_fragment_filter_all);
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences.getString("user_id","")!=null  && !sharedPreferences.getString("user_id","").isEmpty()){
            userId=sharedPreferences.getString("user_id","");

            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
//            mSettingsClient=LocationServices.getSettingsClient(getActivity().getApplicationContext());
//            mRequestingLocationUpdates = false;
//            mLastUpdateTime = "";
            imageViewFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageViewFilterClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cardViewFilter.setVisibility(View.GONE);
                        }
                    });

                    textViewFilterReset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            arrayListId.clear();
                            arrayListTitle.clear();
                            arrayListLocation.clear();
                            arrayListImage.clear();
                            arrayListHost.clear();
                            arrayListDate.clear();
                            arrayListTime.clear();
                            arrayListLongitude.clear();
                            arrayListLatitude.clear();
                            arrayListDistance.clear();
                            discoverEvent();
                            cardViewFilter.setVisibility(View.GONE);
                        }
                    });
                    cardViewFilter.setVisibility(View.VISIBLE);
                    textViewFilterCategory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            View dialog=getLayoutInflater().inflate(R.layout.bottom_sheet_event_type,null);
                            BottomSheetDialog sheetDialog=new BottomSheetDialog(getContext());
                            sheetDialog.setContentView(dialog);
                            sheetDialog.show();
                            apiInterface.getEventType().enqueue(new Callback<ArrayList<EventDetail>>() {
                                @Override
                                public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
                                    allEventType.clear();
                                    allEventTypeImage.clear();
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
                                    recyclerView1.addItemDecoration(new EventSearchFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
                                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView1.setAdapter(collectionAdapter);
                                    collectionAdapter.setOnClickListener(new EventTypeCollectionAdapter.OnClickListener() {
                                        @Override
                                        public void OnClick(int position, ArrayList<String> eventType) {
                                            String selectedEventType=eventType.get(position);
                                            selectEventType(selectedEventType);
                                            sheetDialog.hide();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            });
            apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
            discoverEvent();

        }else {
            DynamicToast.makeError(getActivity().getApplicationContext(),"Something went wrong").show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_email");
            editor.remove("user_id");
            editor.commit();
            Intent intent=new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
//        textView=view.findViewById(R.id.text_fragment);
//        textView.setText("SETTING");
       // EventSearchFragment fragment= (EventSearchFragment) getFragmentManager().findFragmentById(R.id.Eve)

        return view;
    }
    private void discoverEvent(){
        apiInterface.discoverEvent(userId).enqueue(new Callback<ArrayList<EventDetail>>() {
            @Override
            public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
                ArrayList<EventDetail> apiResponse=response.body();
                arrayListEventDetail=apiResponse;
                for (EventDetail value: apiResponse){
                    String totalEvent=value.getTotalEvent();
                    if(Integer.valueOf(totalEvent)>0) {
                        arrayListId.add(value.getId());
                        arrayListTitle.add(value.getTitle());
                        arrayListImage.add(value.getImage());
                        arrayListLocation.add(value.getPlace());
                        arrayListDate.add(value.getStartDate());
                        arrayListTime.add(value.getStartTime());
                        arrayListHost.add(value.getHostName());
                        arrayListLatitude.add(value.getLatitude());
                        arrayListLongitude.add(value.getLongitude());
                        Location l1 = new Location("");
                        l1.setLatitude(Double.parseDouble(value.getLatitude()));
                        l1.setLongitude(Double.parseDouble(value.getLongitude()));
                        float distanceInMeter = l1.distanceTo(userLocation);
                        arrayListDistance.add(decimalFormat.format((distanceInMeter / 1000)));
                    }



                }

                discoverAdapter=new EventSearchDiscoverAdapter(arrayListId,arrayListTitle,arrayListLocation,arrayListImage,arrayListHost,arrayListDate,arrayListTime,getActivity().getApplicationContext(),arrayListDistance);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(linearLayoutManager);

                recyclerView.setAdapter(discoverAdapter);
                discoverAdapter.setOnCardClickListener(new EventSearchDiscoverAdapter.OnCardClickListener() {
                    @Override
                    public void onCardClick(int position) {
                        eventDetailIntent=arrayListEventDetail.get(position);
                        Intent intent=new Intent(getActivity().getApplicationContext(), EventActivity.class);
                        intent.putExtra(KEY_EVENT_DETAIL,eventDetailIntent);
                        intent.putExtra(KEY_USER_ID,userId);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {

            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    userLocation=location;
                                    userLatitude=location.getLatitude();
                                    userLongitude=location.getLongitude();
                                    Log.d("l1",userLatitude+"   ");
//                                    latTextView.setText(location.getLatitude()+"");
//                                    lonTextView.setText(location.getLongitude()+"");
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }

    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        fusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );


    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
             userLocation = locationResult.getLastLocation();
             userLatitude=userLocation.getLatitude();
             userLongitude=userLocation.getLongitude();
            Log.d("l2",userLatitude+"   ");


        }
    };
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }


    private boolean isLocationEnabled() {
        //LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationManager locationManager= (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
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
    private void selectEventType(String eventType){
        apiInterface.checkEventType(eventType).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();

                if(!apiResponse.getError()){
                    Integer eventTypeCount= Integer.valueOf(apiResponse.getEvent().getTotalEvent());
                    if(eventTypeCount>0){

                        apiInterface.getEventLocation(userId, eventType).enqueue(new Callback<ArrayList<EventDetail>>() {
                            @Override
                            public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
                                ArrayList<EventDetail> eventDetail = response.body();
                                arrayListEventDetail=eventDetail;
                                arrayListId.clear();
                                arrayListTitle.clear();
                                arrayListLocation.clear();
                                arrayListImage.clear();
                                arrayListHost.clear();
                                arrayListDate.clear();
                                arrayListTime.clear();
                                arrayListLongitude.clear();
                                arrayListLatitude.clear();
                                arrayListDistance.clear();

                                for (EventDetail value : eventDetail
                                ) {
                                    totalCount=value.getTotalEvent();
                                    if(Integer.valueOf(totalCount)>0){
                                        arrayListId.add(value.getId());
                                        arrayListTitle.add(value.getTitle());
                                        arrayListImage.add(value.getImage());
                                        arrayListLocation.add(value.getPlace());
                                        arrayListDate.add(value.getStartDate());
                                        arrayListTime.add(value.getStartTime());
                                        arrayListHost.add(value.getHostName());
                                        Location l1=new Location("");
                                        l1.setLatitude(Double.parseDouble(value.getLatitude()));
                                        l1.setLongitude(Double.parseDouble(value.getLongitude()));
                                        float distanceInMeter=l1.distanceTo(userLocation);
                                        arrayListDistance.add(decimalFormat.format((distanceInMeter/1000)));
                                    }

//                                    arrayListLatitude.add(value.getLatitude());
//                                    arrayListLongitude.add(value.getLongitude());

                                }

                            discoverAdapter.notifyDataSetChanged();


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


