package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.User;
import com.example.okazo.util.AdminTMoneyAdapter;
import com.example.okazo.util.ConfirmationDialog;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminTMoneyActivity extends AppCompatActivity implements ConfirmationDialog.orderConfirmationListener {
    private RecyclerView recyclerView;
    private EditText editTextSearch;
    private ArrayList<User> users;
    private ApiInterface apiInterface;
    private AdminTMoneyAdapter adapter;
    private int totalAmount;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_t_money);
        recyclerView=findViewById(R.id.admin_t_money_recyclerview);
        editTextSearch=findViewById(R.id.admin_t_money_search);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.adminUserTMoney().enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                users=response.body();
                if(users.get(0).getTotalUser().equals("0")){
                    DynamicToast.make(getApplicationContext(),"No users").show();
                }else {
                        adapter=new AdminTMoneyAdapter(users);
                    LinearLayoutManager linearLayout=new LinearLayoutManager(AdminTMoneyActivity.this);
                    linearLayout.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(linearLayout);
                    recyclerView.setAdapter(adapter);
                    addAmount();

                    editTextSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            apiInterface.adminSearchUser(editable.toString()).enqueue(new Callback<ArrayList<User>>() {
                                @Override
                                public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                                    users=response.body();
                                    if(users.get(0).getTotalUser().equals("0")){
                                        DynamicToast.make(getApplicationContext(),"No User found").show();
                                        recyclerView.setAdapter(null);
                                    }else {
                                        adapter=new AdminTMoneyAdapter(users);
                                        recyclerView.setAdapter(null);
                                        recyclerView.setAdapter(adapter);
                                        addAmount();
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

    private void addAmount(){

        adapter.setOnButtonClickListener(new AdminTMoneyAdapter.OnButtonClickListener() {
            @Override
            public void onClick(int position) {
             if(!((EditText)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_admin_t_money_amount)).getText().toString().trim().isEmpty()){
                 int enterAmount=Integer.parseInt(((EditText)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_admin_t_money_amount)).getText().toString().trim());
                 int userAmount=users.get(position).getAmount();
                 totalAmount=userAmount+enterAmount;
                 currentUser=users.get(position).getId();
                                 ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to confirm the amount?");
                                confirmationDialog.show(getSupportFragmentManager(),"Confirmation");
             }else {
                 DynamicToast.makeError(getApplicationContext(),"Dont leave amount empty").show();
             }
//                actionId=users.get(position).getId();

            }
        });
    }

    @Override
    public void OnYesClicked() {
        apiInterface.adminAddTMoney(currentUser,String.valueOf(totalAmount)).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    DynamicToast.makeSuccess(getApplicationContext(),"Amount Added").show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }else {
                    DynamicToast.makeError(getApplicationContext(),"Couldnt not complete process").show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                DynamicToast.makeError(getApplicationContext(),t.getLocalizedMessage()).show();
            }
        });
    }

    @Override
    public void OnNoClicked() {

    }
}
