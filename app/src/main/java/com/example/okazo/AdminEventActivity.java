package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.AdminEventAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_STAFF_ID;

public class AdminEventActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private AdminEventAdapter adapter;
    private String staffId;
    private  ArrayList<EventDetail> eventDetails;
    private EditText editTextSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event);
        recyclerView=findViewById(R.id.admin_event_recyclerview);
        editTextSearch=findViewById(R.id.admin_event_search);
        Bundle bundle=getIntent().getExtras();
        staffId=bundle.getString(KEY_STAFF_ID);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);





        apiInterface.getEventLocation(staffId,"Admin").enqueue(new Callback<ArrayList<EventDetail>>() {
            @Override
            public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
               eventDetails=response.body();
               adapter=new AdminEventAdapter(eventDetails);
                LinearLayoutManager linearLayout=new LinearLayoutManager(getApplicationContext());
                linearLayout.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayout);
                recyclerView.setAdapter(adapter);

                editTextSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                            apiInterface.adminSearchEvent(editable.toString()).enqueue(new Callback<ArrayList<EventDetail>>() {
                                @Override
                                public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
                                    eventDetails=response.body();
                                    if(eventDetails.get(0).getTotalEvent().equals("0")){
                                        DynamicToast.make(getApplicationContext(),"No event").show();
                                        recyclerView.setAdapter(null);
                                    }else {
                                        adapter=new AdminEventAdapter(eventDetails);
                                        recyclerView.setAdapter(null);
                                        recyclerView.setAdapter(adapter);
                                    }

                                }

                                @Override
                                public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {

                                }
                            });

                    }
                });
                // block
                adapter.setOnBlockClickListener(new AdminEventAdapter.OnBlockClickListener() {
                    @Override
                    public void onBlockClick(int position) {
                        String id=eventDetails.get(position).getId();
                        apiInterface.eventStatus(id,"block").enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                APIResponse apiResponse=response.body();
                                if(!apiResponse.getError()){
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);
                                }else {
                                    DynamicToast.makeError(getApplicationContext(),apiResponse.getErrorMsg()).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponse> call, Throwable t) {

                            }
                        });
                    }
                });

                //unblock
                adapter.setOnUnBlockClickListener(new AdminEventAdapter.OnUnBlockClickListener() {
                    @Override
                    public void onUnBlockClick(int position) {
                        String id=eventDetails.get(position).getId();
                        apiInterface.eventStatus(id,"unblock").enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                APIResponse apiResponse=response.body();
                                if(!apiResponse.getError()){
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);
                                }else {
                                    DynamicToast.makeError(getApplicationContext(),apiResponse.getErrorMsg()).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<APIResponse> call, Throwable t) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {

            }
        });


    }
}
