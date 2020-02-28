package com.example.okazo.Fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.Note;
import com.example.okazo.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.util.Log.d;
import static com.example.okazo.util.constants.MAPVIEW_BUNDLE_KEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    GoogleMap googleMap;
    Call<List<Note>> call;
    ApiInterface apiInterface;
    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_event, container, false);
       initGoogleMap(savedInstanceState,v);
        Retrofit retrofit= ApiClient.getApiClient();

        apiInterface=retrofit.create(ApiInterface.class);
        call=apiInterface.getLocation();
        return v;
    }
private void initGoogleMap(Bundle savedInstanceState,View v){
    Bundle mapViewBundle = null;
    if (savedInstanceState != null) {
        mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
    }
    mMapView = (MapView)v. findViewById(R.id.map_event);
    mMapView.onCreate(mapViewBundle);

    mMapView.getMapAsync(this);
}
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap=map;
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)
        !=PackageManager.PERMISSION_GRANTED){
            return;
        }


//        call.enqueue(new Callback<List<Note>>() {
//            @Override
//            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
//                for(Note note : response.body()){
//                   double lat=note.getLatitude();
//                   double longi=note.getLongitude();
//                   String name=note.getName();
//                    LatLng latLng=new LatLng(lat,longi);
//                    String date=note.getDate();
//                    googleMap.addMarker(new MarkerOptions().position(latLng).title(name+"\n"+date));
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Note>> call, Throwable t) {
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                d("failed",t.getMessage());
//                d("response",t.toString());
//            }
//        });
        googleMap.setMyLocationEnabled(true);
    }


    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



}

