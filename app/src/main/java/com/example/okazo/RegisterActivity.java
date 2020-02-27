package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;

import com.example.okazo.util.JavaMailAPI;


import com.example.okazo.util.constants;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextEmail,editTextPassword,editTextMobile,editTextName;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextMobile=findViewById(R.id.register_phone);
        editTextName=findViewById(R.id.register_name);
        editTextPassword=findViewById(R.id.register_password);
        editTextEmail=findViewById(R.id.register_email);
        btnRegister=findViewById(R.id.register_button);
          btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=editTextMobile.getText().toString();
                String name=editTextName.getText().toString();
                String password=editTextPassword.getText().toString();
                String email=editTextEmail.getText().toString();
                Random random=new Random();
                if(email.matches(emailPattern)){
                    editTextEmail.setError(null);
                    if(name!=null){
                        editTextName.setError(null);
                        if(password!=null){
                            editTextPassword.setError(null);
                            if(phone!=null){
                                editTextMobile.setError(null);
                                constants constant=new constants();
                                int num=random.nextInt(900000)+100000;

                                JavaMailAPI javaMailAPI=new JavaMailAPI(RegisterActivity.this,"nishan.nishan.timalsena@gmail.com",
                                        "Verification Code","OTP is:"+num);
                                javaMailAPI.execute();
                            }else {
                                editTextMobile.setError("Enter mobile number");
                            }
                        }else {
                            editTextPassword.setError("Enter password");
                        }

                    }else {
                        editTextName.setError("Enter Name");
                    }

                }else {
                    editTextEmail.setError("Incorrect Email format");
                }



                    }


        });



//                apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
//                apiInterface.registerUser(email,password,name,phone)
//                .enqueue(new Callback<APIResponse>() {
//                    @Override
//                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//                        APIResponse result=response.body();
//                        if(result.getError()){
//                            Toast.makeText(RegisterActivity.this, result.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//
//                            Toast.makeText(RegisterActivity.this, "Register Success:"+result.getUser().getOtp(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<APIResponse> call, Throwable t) {
//
//                    }
//                });
                }


}
