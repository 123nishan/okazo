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
import com.example.okazo.util.GMailSender;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextEmail,editTextPassword,editTextMobile,editTextName;
    Button btnRegister;
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
                int num=random.nextInt(900000)+100000;
               //DynamicToast.makeSuccess(getApplicationContext(),num+"").show();

                      try{
                          DynamicToast.makeSuccess(getApplicationContext(),"entered").show();
                          GMailSender  sender=new GMailSender("nishan_98@hotmail.com","#Nishan123");
                          sender.sendMail("Email Sender App",
                                  num+"",
                                  "nishan_98@hotmail.com",
                                  email);
                          Log.d("mylogSent", "Error: " + "sent");
                      } catch (Exception e) {
                          Log.d("mylogError", "Error: " + e.getMessage());

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
;            }


}
