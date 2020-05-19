package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;

import com.example.okazo.util.JavaMailAPI;


import com.example.okazo.util.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText editTextEmail,editTextPassword,editTextMobile,editTextName;
    Button btnRegister;
   // String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailPattern="^(.+)@(.+)$";;
    ApiInterface apiInterface;
    Boolean emailStatus=false,passwordStatus=false,nameStatus=false,mobileStatus=false;
    Pattern pattern;
    private ImageButton imageButtonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextMobile=findViewById(R.id.register_phone);
        editTextName=findViewById(R.id.register_name);
        editTextPassword=findViewById(R.id.register_password);
        editTextEmail=findViewById(R.id.register_email);
        btnRegister=findViewById(R.id.register_button);
        imageButtonLogin=findViewById(R.id.back_to_login);
        btnRegister.setVisibility(View.GONE);
        GifDrawable gifChecking = null;
        try {
            gifChecking = new GifDrawable( getResources(), R.drawable.loading_textview );
        } catch (IOException e) {
            e.printStackTrace();
        }
        pattern= Pattern.compile(emailPattern);
        GifDrawable finalGiffChecking = gifChecking;
        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                editTextEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, finalGiffChecking,null);

            }else {
//                if(editTextEmail.getText().toString().matches(emailPattern)){
//                }else {
//                    editTextEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_wrong),null);
//                }
            }

        });
        imageButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, finalGiffChecking,null);
                    if(editTextPassword.getText().toString().length()>5){

                    }
                }
                else {
                    if(editTextPassword.getText().toString().length()>5){




                    }else {
                        editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_wrong),null);
                    }
                }
            }
        });
        editTextMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextMobile.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, finalGiffChecking,null);

                }
                else {
                    if(editTextMobile.getText().toString().length()==10){
                    }else {
                        editTextMobile.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_wrong),null);
                    }
                }
            }
        });
        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, finalGiffChecking,null);

                }
                else {
                    if(editTextName.getText().toString().length()>5){
                    }else {
                        editTextName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_wrong),null);
                    }
                }
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Matcher matcher=pattern.matcher(s.toString());
                if(matcher.matches()){
                    editTextEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_correct,null),null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Matcher matcher=pattern.matcher(s.toString());
                if(matcher.matches()){
                    emailStatus=true;
                    editTextEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_correct,null),null);
                }else {
                    emailStatus=false;
                    btnRegister.setVisibility(View.GONE);
                }
                //TODO check here later for validation in email


                if(emailStatus && passwordStatus && nameStatus && mobileStatus){
                    btnRegister.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().length()>5){
                    editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_correct),null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>5){
                    passwordStatus=true;
                    editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_correct),null);
                }else {
                    passwordStatus=false;
                    btnRegister.setVisibility(View.GONE);
                }
                if(emailStatus && passwordStatus && nameStatus && mobileStatus){
                    btnRegister.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().length()==10){
                    editTextMobile.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_correct),null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()==10){
                    mobileStatus=true;
                    editTextMobile.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_correct),null);
                }else {
                    mobileStatus=false;
                    btnRegister.setVisibility(View.GONE);
                }
                if(emailStatus && passwordStatus && nameStatus && mobileStatus){
                    btnRegister.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(count>5 ){
                    editTextMobile.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_correct),null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(count >5){
                    nameStatus=true;
                    editTextName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_correct),null);
                }else {
                    nameStatus=false;
                    btnRegister.setVisibility(View.GONE);
                }
                if(emailStatus && passwordStatus && nameStatus && mobileStatus){
                    btnRegister.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=editTextMobile.getText().toString();
                String name=editTextName.getText().toString();
                String password=editTextPassword.getText().toString();
                String email=editTextEmail.getText().toString();
                Matcher matcher=pattern.matcher(email);
                Random random=new Random();
                if(matcher.matches()){
                    editTextEmail.setError(null);
                    if(name!=null && !name.isEmpty()){

                        editTextName.setError(null);
                        if(password!=null && !password.isEmpty()){
                            editTextPassword.setError(null);
                            if(phone!=null && !phone.isEmpty()){
                                editTextMobile.setError(null);

                                int num=random.nextInt(900000)+100000;
                                String identifier="first";
                                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                String verified="non-verified";
                                Date date=new Date();
                                Timestamp timestamp=new Timestamp(date.getTime());
                                Toast.makeText(RegisterActivity.this, "time"+verified, Toast.LENGTH_SHORT).show();

                                apiInterface.otp(email,String.valueOf(num),timestamp).enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse result = response.body();
                                        if(result.getError()){
                                            DynamicToast.makeError(getApplicationContext(), result.getErrorMsg()).show();
                                        }else {

                                            FirebaseInstanceId.getInstance().getInstanceId()
                                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                            if (!task.isSuccessful()) {
                                                                //Log.w(TAG, "getInstanceId failed", task.getException());
                                                                return;
                                                            }

                                                            // Get new Instance ID token
                                                            String token = task.getResult().getToken();
                                                            apiInterface.registerUser(email,password,name,phone,token).enqueue(new Callback<APIResponse>() {
                                                                @Override
                                                                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                                                    APIResponse apiResponse=response.body();
                                                                    if(apiResponse.getError()){
                                                                        DynamicToast.makeError(getApplicationContext(), apiResponse.getErrorMsg()).show();
                                                                    }else {

                                                                        JavaMailAPI javaMailAPI=new JavaMailAPI(RegisterActivity.this,"nishan.nishan.timalsena@gmail.com",
                                                                                "Verification Code","OTP is:",num+"",password,phone,name,email,identifier);
                                                                        javaMailAPI.execute();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<APIResponse> call, Throwable t) {

                                                                }
                                                            });
                                                            // Log and toast
                                                           // String msg = getString(R.string.msg_token_fmt, token);

                                                            //Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {
                                        DynamicToast.makeWarning(getApplicationContext(),"something went wrong").show();
                                    }
                                });


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
