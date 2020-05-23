package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.User;
import com.example.okazo.util.AdminUserAdapter;
import com.example.okazo.util.ConfirmationDialog;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminStaffActivity extends AppCompatActivity implements ConfirmationDialog.orderConfirmationListener {
    private RecyclerView recyclerView;
    private ApiInterface apiInterface;
    private AdminUserAdapter adapter;
    private ImageView textViewError;
    private ArrayList<User> userDetails=new ArrayList<>();
    private EditText editTextSearch;
    private String confirmationCondition;
    GifDrawable gifDrawable=null;
    String actionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_staff);
        recyclerView=findViewById(R.id.admin_staff_recyclerview);
        editTextSearch=findViewById(R.id.admin_staff_search);
        textViewError=findViewById(R.id.admin_staff_error);
        getSupportActionBar().setTitle("Staff");
        try {
            gifDrawable = new GifDrawable( getResources(), R.drawable.not_found );
            textViewError.setBackground(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.adminStaff().enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                userDetails=response.body();
                if(userDetails.get(0).getTotalUser().equals("0")){
                    textViewError.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else {

                    textViewError.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter=new AdminUserAdapter(userDetails,"user");
                    LinearLayoutManager linearLayout=new LinearLayoutManager(AdminStaffActivity.this);
                    linearLayout.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(linearLayout);
                    recyclerView.setAdapter(adapter);
//block
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
                            apiInterface.adminSearchStaff(editable.toString()).enqueue(new Callback<ArrayList<User>>() {
                                @Override
                                public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                                    userDetails=response.body();
                                    if(userDetails.get(0).getTotalUser().equals("0")){
                                        textViewError.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                        recyclerView.setAdapter(null);
                                    }else {
                                        textViewError.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter=new AdminUserAdapter(userDetails,"user");
                                        recyclerView.setAdapter(null);
                                        recyclerView.setAdapter(adapter);
                                        blockUser();
                                        unblockUser();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
    }

    private void blockUser(){
        adapter.setOnBlockClickListener(new AdminUserAdapter.OnBlockClickListener() {
            @Override
            public void onBlockClick(int position) {
                actionId=userDetails.get(position).getId();
                confirmationCondition="block";
                ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to block this user?");
                confirmationDialog.show(getSupportFragmentManager(),"Confirmation");


            }
        });
    }
    private void unblockUser(){
        adapter.setOnUnBlockClickListener(new AdminUserAdapter.OnUnBlockClickListener() {
            @Override
            public void onUnBlockClick(int position) {
                actionId=userDetails.get(position).getId();
                confirmationCondition="unblock";
                ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to unblock this user?");
                confirmationDialog.show(getSupportFragmentManager(),"Confirmation");

            }
        });
    }

    @Override
    public void OnYesClicked() {
        if(confirmationCondition.equals("block")){
            apiInterface.adminUserStatus(actionId,"block").enqueue(new Callback<APIResponse>() {
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
                    Toast.makeText(AdminStaffActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            apiInterface.adminUserStatus(actionId,"unblock").enqueue(new Callback<APIResponse>() {
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
