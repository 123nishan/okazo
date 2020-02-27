package com.example.okazo.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.okazo.LoginActivity;
import com.example.okazo.R;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    Button buttonLogOut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        buttonLogOut=view.findViewById(R.id.home_fragment_log_out);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getActivity(),"Logging Out").show();
                SharedPreferences pref = getActivity().getSharedPreferences("BuyyaPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }

}
