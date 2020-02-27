package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    Button buttonLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextInputEditText inputEditTextEmail,inputEditTextPassword;
    String email,password;
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLogin=findViewById(R.id.login_submit);
        inputEditTextEmail=findViewById(R.id.login_email);
        inputEditTextPassword=findViewById(R.id.login_password);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=inputEditTextEmail.getText().toString().trim();
                password=inputEditTextPassword.getText().toString().trim();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
//                if (email.matches(emailPattern)){
//                        inputEditTextEmail.setError(null);
//                    if(password.isEmpty()){
//                        inputEditTextPassword.setError("Error");
//                    }else {
//                        inputEditTextPassword.setError(null);
//                        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
//                        apiInterface.loginUser(email,password)
//                                .enqueue(new Callback<APIResponse>() {
//                                    @Override
//                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//                                        APIResponse result=response.body();
//                                        if(result.getError()){
//                                            DynamicToast.makeSuccess(getApplicationContext(),result.getErrorMsg()).show();
//                                        }else {
//
//
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<APIResponse> call, Throwable t) {
//                                        DynamicToast.makeSuccess(getApplicationContext(),"Failed").show();
//                                    }
//                                });
//
//
//                    }
//                }else{
//                    inputEditTextEmail.setError("Invalid Email");
//                }


            }
        });
    }

}
