package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    TextView textViewRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextInputEditText inputEditTextEmail,inputEditTextPassword;
    String email,password;
    ApiInterface apiInterface;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLogin=findViewById(R.id.login_submit);
        inputEditTextEmail=findViewById(R.id.login_email);
        inputEditTextPassword=findViewById(R.id.login_password);
        textViewRegister=findViewById(R.id.login_register);
        progressBar=findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setBackgroundColor(Color.BLACK);
                progressBar.setProgress(10);
                email=inputEditTextEmail.getText().toString().trim();
                password=inputEditTextPassword.getText().toString().trim();

                if (email.matches(emailPattern)){
                        inputEditTextEmail.setError(null);
                    if(password.isEmpty()){
                        inputEditTextPassword.setError("Error");
                    }else {
                        progressBar.setProgress(40);
                        inputEditTextPassword.setError(null);
                        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
                        apiInterface.loginUser(email,password)
                                .enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse result=response.body();
                                        if(result.getError()){
                                            DynamicToast.makeError(getApplicationContext(),result.getErrorMsg()).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }else {
                                            progressBar.setProgress(100);
                                            Toast.makeText(LoginActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();
                                            Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                                            intent1.putExtra("email",email);
                                            startActivity(intent1);


                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {
                                        DynamicToast.makeSuccess(getApplicationContext(),"Error 500").show();
                                    }
                                });


                    }
                }else{
                    inputEditTextEmail.setError("Invalid Email");
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();

    }
}
