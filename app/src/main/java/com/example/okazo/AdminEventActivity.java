package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.AdminEventAdapter;
import com.example.okazo.util.ConfirmationDialog;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_STAFF_ID;

public class AdminEventActivity extends AppCompatActivity implements ConfirmationDialog.orderConfirmationListener {
    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private AdminEventAdapter adapter;
    private String staffId;
    private  ArrayList<EventDetail> eventDetails;
    private EditText editTextSearch;
    private String confirmationCondition;
    String actionId;

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
                // block
                blockUser();

                //unblock
                unblockUser();

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
                                        blockUser();
                                        unblockUser();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {

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

    private void unblockUser() {
        adapter.setOnUnBlockClickListener(new AdminEventAdapter.OnUnBlockClickListener() {
            @Override
            public void onUnBlockClick(int position) {
                 actionId=eventDetails.get(position).getId();
                confirmationCondition="unblock";
                ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to unblock this event?");
                confirmationDialog.show(getSupportFragmentManager(),"Confirmation");

            }
        });
    }

    private void blockUser() {

        adapter.setOnBlockClickListener(new AdminEventAdapter.OnBlockClickListener() {
            @Override
            public void onBlockClick(int position) {
                 actionId=eventDetails.get(position).getId();
                confirmationCondition="block";
                Log.d("TEST",confirmationCondition);
                ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to block this event?");
                confirmationDialog.show(getSupportFragmentManager(),"Confirmation");

            }
        });
    }

    @Override
    public void OnYesClicked() {
        Log.d("TEST1",confirmationCondition);
        if(confirmationCondition.equals("block")){
            apiInterface.eventStatus(actionId,"block").enqueue(new Callback<APIResponse>() {
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
                    DynamicToast.makeError(getApplicationContext(),t.getLocalizedMessage()).show();
                }
            });
        }else {
            apiInterface.eventStatus(actionId,"unblock").enqueue(new Callback<APIResponse>() {
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

    }

    @Override
    public void OnNoClicked() {

    }
}
