package com.example.okazo.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.okazo.GeoFenceActivity;
import com.example.okazo.LoginActivity;
import com.example.okazo.R;
import com.example.okazo.eventDetail;
import com.example.okazo.util.constants;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    AppBarLayout appBarLayout;
    Button buttonLogOut,buttonAddEvent;
    AppCompatImageButton imageButtonAdd;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        buttonLogOut=view.findViewById(R.id.home_fragment_log_out);
        appBarLayout= view.findViewById(R.id.fragment_home_app_bar);
        imageButtonAdd= view.findViewById(R.id.fragment_home_add);
        collapsingToolbarLayout=view.findViewById(R.id.fragment_home_collapsing_tool_bar);
        buttonAddEvent=view.findViewById(R.id.add_event);
        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity().getApplicationContext(), eventDetail.class);
                startActivity(intent);
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange=-1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(scrollRange==-1){
                    scrollRange=appBarLayout.getTotalScrollRange();
                }

                if(scrollRange+i==0){

                    imageButtonAdd.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle("Okazo");

                }else {
                    //fully expanded
                    imageButtonAdd.setVisibility(View.GONE);
                    collapsingToolbarLayout.setTitle("");

                }
            }
        });
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getActivity(),"Logging Out").show();
                SharedPreferences pref = getActivity().getSharedPreferences(constants.KEY_SHARED_PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_email","");
                editor.commit();

                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //
          Intent intent=new Intent(getActivity().getApplicationContext(), GeoFenceActivity.class);
            startActivity(intent);
            }
        });
        return view;
    }


}
