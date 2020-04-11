package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.util.JavaMailAPI;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {
    EditText editTextOtp;
    Button buttonVerify;
    ApiInterface apiInterface;

    ProgressBar progressBar;
    TextView textViewOtpTimer,textViewOtpResend;
    int counter=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        editTextOtp=findViewById(R.id.otp_code);
        buttonVerify=findViewById(R.id.otp_verify);
        textViewOtpTimer=findViewById(R.id.otp_timer);
        textViewOtpResend=findViewById(R.id.otp_resend);
//        String otp=getIntent().getExtras().getString("otp");
        String email=getIntent().getExtras().getString("email");
//
//        String password=getIntent().getExtras().getString("password");
//        String name=getIntent().getExtras().getString("name");
//        String phone=getIntent().getExtras().getString("phone");
        progressBar=findViewById(R.id.otp_progress_bar);
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                    textViewOtpTimer.setText(String.valueOf(counter));
                    counter++;
            }

            @Override
            public void onFinish() {
               textViewOtpResend.setVisibility(View.VISIBLE);
                buttonVerify.setEnabled(false);
                Toast.makeText(OtpActivity.this, "Time's off", Toast.LENGTH_SHORT).show();
            }
        }.start();
        textViewOtpResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random=new Random();
                int num=random.nextInt(900000)+100000;
                String identifier="resend";

                JavaMailAPI javaMailAPI=new JavaMailAPI(OtpActivity.this,"nishan.nishan.timalsena@gmail.com",
                        "Verification Code","OTP is:",num+"","","","name",email,identifier);
                javaMailAPI.execute();
            }
        });
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    progressBar.setVisibility(View.VISIBLE);
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


                        String verified="2";

                        apiInterface.otpVerification(email,verified).enqueue(new Callback<APIResponse>() {
                            @Override
                            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                APIResponse result = response.body();
                                if (result.getError()) {
                                    DynamicToast.makeError(getApplicationContext(), result.getErrorMsg()).show();
                                } else {
                                    String code = result.getUser().getCode();

                                   if (editTextOtp.getText().toString().equals(code)) {
                                         String verified="1";
                                         apiInterface.otpVerification(email,verified).enqueue(new Callback<APIResponse>() {
                                             @Override
                                             public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                                 APIResponse result=response.body();
                                                 if(result.getError()){
                                                     DynamicToast.makeError(getApplicationContext(), result.getErrorMsg()).show();
                                                     Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                                                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    startActivity(intent);
                                                 }else {

                                                     DynamicToast.makeSuccess(getApplicationContext(), "Verified!!").show();
                                                     Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                                                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                     startActivity(intent);
                                                 }
                                             }

                                             @Override
                                             public void onFailure(Call<APIResponse> call, Throwable t) {

                                             }
                                         });
                                    } else {

                                        Toast.makeText(OtpActivity.this, "Code didnt match", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                                @Override
                                public void onFailure (Call < APIResponse > call, Throwable t){


                            }
                        });
//                        apiInterface.registerUser(email, password, name, phone)
//                                .enqueue(new Callback<APIResponse>() {
//                                    @Override
//                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
//                                        APIResponse result = response.body();
//                                        if (result.getError()) {
//                                            DynamicToast.makeError(getApplicationContext(), result.getErrorMsg()).show();
//                                        } else {
//                                            DynamicToast.makeSuccess(getApplicationContext(), "User registerted").show();
//                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                            startActivity(intent);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<APIResponse> call, Throwable t) {
//
//                                    }
//                                });



            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DynamicToast.makeError(getApplicationContext(),"Verification Failed").show();
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
