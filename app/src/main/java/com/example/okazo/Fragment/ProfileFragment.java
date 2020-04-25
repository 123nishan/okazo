package com.example.okazo.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.Note;
import com.example.okazo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private EditText editTextLatitude,editTextLongitude,editTextName;
    ProgressDialog progressDialog;
    ApiInterface apiInterface;
    Button submit;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        editTextLatitude=v.findViewById(R.id.latitude);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        editTextLongitude=v.findViewById(R.id.longitude);
        editTextName=v.findViewById(R.id.name);
        submit=v.findViewById(R.id.form_submit);
        progressDialog= new ProgressDialog(getContext());
        progressDialog.setMessage("please wait....");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getActivity().getApplicationContext(), MapBox.class);
//                startActivity(intent);
//                String latitude=editTextLatitude.getText().toString();
//                String longitude=editTextLongitude.getText().toString();
//                String name=editTextName.getText().toString().trim();
//
//                if(latitude.isEmpty()){
//                    editTextLatitude.setError("please enter");
//                }else if(longitude.isEmpty()){
//                    editTextLongitude.setError("please enter");
//                }else if(name.isEmpty()){
//                    editTextName.setError("please enter");
//
//                }else {
//                    saveDetail(latitude,longitude,name);
//                }
            }
        });
        return v;
    }

    private void saveDetail(String latitude, String longitude, String name) {
        progressDialog.show();

        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        Double longitudeDouble=Double.valueOf(longitude);
        Double latitudeDouble=Double.valueOf(latitude);
        Log.d("lat",latitudeDouble.toString());

        Call<Note> call =apiInterface.saveNote(latitudeDouble,longitudeDouble,name);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                progressDialog.dismiss();
                if(response.isSuccessful() && response.body()!=null){
                    Boolean success=response.body().getSuccess();
                    if(success){
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                         //back to main activity
                    }else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
