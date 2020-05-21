package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.util.JavaMailAPI;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
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

import static com.example.okazo.util.constants.KEY_STAFF_EMAIL;
import static com.example.okazo.util.constants.KEY_STAFF_ID;
import static com.example.okazo.util.constants.KEY_STAFF_ROLE;


public class LoginActivity extends AppCompatActivity {
    Button buttonLogin;
    AppCompatImageButton registerButton;
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailPattern="^(.+)@(.+)$";;
    TextInputEditText inputEditTextEmail, inputEditTextPassword;
    String email, password,userId;
    ApiInterface apiInterface;
    ProgressBar progressBar;
    Pattern pattern;
    Boolean emailStatus=false;
    Boolean passwordStatus=false;
    String sharedPreferencesConstant = "hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(sharedPreferencesConstant, MODE_PRIVATE);
        pattern= Pattern.compile(emailPattern);
        if (sharedPreferences.getString("user_email", "") != null && !sharedPreferences.getString("user_email", "").isEmpty()
        &&sharedPreferences.getString("user_id","")!=null && !sharedPreferences.getString("user_id","").isEmpty()) {

            if(sharedPreferences.getString("staff_role","")!=null && !sharedPreferences.getString("staff_role","").isEmpty()){
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra(KEY_STAFF_EMAIL, sharedPreferences.getString("user_email", ""));
                intent.putExtra(KEY_STAFF_ID, sharedPreferences.getString("user_id", ""));
                intent.putExtra(KEY_STAFF_ROLE, sharedPreferences.getString("staff_role", ""));
                startActivity(intent);
            }else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("email", sharedPreferences.getString("user_email", ""));
                intent.putExtra("userId", sharedPreferences.getString("user_id", ""));
                startActivity(intent);
            }



        } else {

        setContentView(R.layout.activity_login);
        buttonLogin = findViewById(R.id.login_submit);
        inputEditTextEmail = findViewById(R.id.login_email);
        inputEditTextPassword = findViewById(R.id.login_password);
        registerButton = findViewById(R.id.login_register);
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        buttonLogin.setVisibility(View.GONE);
            GifDrawable gifChecking = null;

            try {
                gifChecking = new GifDrawable( getResources(), R.drawable.loading_textview );

            } catch (IOException e) {
                e.printStackTrace();
            }
            GifDrawable finalGiffChecking = gifChecking;
            inputEditTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
                if(hasFocus){
                   inputEditTextEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, finalGiffChecking,null);

                }else {
                    if(inputEditTextEmail.getText().toString().matches(emailPattern)){






                    }else {
                        inputEditTextEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_wrong),null);
                    }
                }

            });
          inputEditTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
              @Override
              public void onFocusChange(View v, boolean hasFocus) {
                  if(hasFocus){
                      inputEditTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, finalGiffChecking,null);
                      if(inputEditTextPassword.getText().toString().length()>5){

                      }
                  }
                  else {
                      if(inputEditTextPassword.getText().toString().length()>5){




                      }else {
                          inputEditTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_wrong),null);
                      }
                  }
              }
          });

          inputEditTextEmail.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                  Matcher matcher=pattern.matcher(s.toString());

                if(matcher.matches()){
                    inputEditTextEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_correct,null),null);
                }
              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                  Matcher matcher=pattern.matcher(s.toString());
                  if(matcher.matches()){
                      emailStatus=true;
                      inputEditTextEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_correct,null),null);
                  }else {
                      emailStatus=false;
                      buttonLogin.setVisibility(View.GONE);
                  }

                    if(emailStatus && passwordStatus){
                        buttonLogin.setVisibility(View.VISIBLE);
                    }
              }

              @Override
              public void afterTextChanged(Editable s) {

              }
          });
          inputEditTextPassword.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().length()>5){
                    inputEditTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_correct),null);
                }
              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(s.toString().length()>5){
                     passwordStatus=true;
                     inputEditTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.ic_correct),null);
                 }else {
                     passwordStatus=false;
                     buttonLogin.setVisibility(View.GONE);
                 }
                  if(emailStatus && passwordStatus){
                      buttonLogin.setVisibility(View.VISIBLE);
                  }
              }

              @Override
              public void afterTextChanged(Editable s) {

              }
          });
            registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setBackgroundColor(Color.BLACK);
                progressBar.setProgress(10);
                email = inputEditTextEmail.getText().toString().trim();
                password = inputEditTextPassword.getText().toString().trim();
                Matcher matcher=pattern.matcher(email);
                if (matcher.matches()) {
                    inputEditTextEmail.setError(null);
                    if (password.isEmpty()) {
                        inputEditTextPassword.setError("Error");
                    } else {
                        progressBar.setProgress(40);
                        inputEditTextPassword.setError(null);
                        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                        apiInterface.loginUser(email, password)
                                .enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse result = response.body();

                                        if (result.getError()) {
                                            DynamicToast.makeError(getApplicationContext(), result.getErrorMsg()).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        } else {

                                            //customer



                                                userId = result.getUser().getId();

                                                if (result.getUser().getVerified().equals("2")) {


                                                    Random random = new Random();
                                                    int num = random.nextInt(900000) + 100000;
                                                    Date date = new Date();
                                                    Timestamp timestamp = new Timestamp(date.getTime());
                                                    String verified = "2";

                                                    apiInterface.otp(email, String.valueOf(num), timestamp).enqueue(new Callback<APIResponse>() {
                                                        @Override
                                                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                                            APIResponse apiResponse = response.body();

                                                            if (apiResponse.getError()) {
                                                                DynamicToast.makeError(getApplicationContext(), apiResponse.getErrorMsg()).show();
                                                            } else {
                                                                String identifier = "frist";
                                                                JavaMailAPI javaMailAPI = new JavaMailAPI(LoginActivity.this, "nishan.nishan.timalsena@gmail.com",
                                                                        "Verification Code", "OTP is:", num + "", "", "", "", email, identifier);
                                                                javaMailAPI.execute();

                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<APIResponse> call, Throwable t) {
                                                            DynamicToast.makeError(getApplicationContext(), t.getLocalizedMessage()).show();
                                                        }
                                                    });
                                                } else {
                                                    //customer
                                                    if (result.getUser().getRole().equals("1")) {
                                                        progressBar.setProgress(100);

                                                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        intent1.putExtra("email", email);
                                                        intent1.putExtra("userId", userId);
                                                        startActivity(intent1);
                                                    }else if(result.getUser().getRole().equals("2")){
                                                        //staff
                                                        Intent intent1 = new Intent(getApplicationContext(), AdminActivity.class);
                                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        intent1.putExtra(KEY_STAFF_ID, userId);
                                                        intent1.putExtra(KEY_STAFF_ROLE,result.getUser().getRole() );
                                                        intent1.putExtra(KEY_STAFF_EMAIL,email );
                                                        startActivity(intent1);
                                                    }else {
                                                        Intent intent1 = new Intent(getApplicationContext(), AdminActivity.class);
                                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        intent1.putExtra(KEY_STAFF_ID, userId);
                                                        intent1.putExtra(KEY_STAFF_ROLE,result.getUser().getRole() );
                                                        intent1.putExtra(KEY_STAFF_EMAIL,email );
                                                        startActivity(intent1);
                                                    }
                                                    // Log.d("userOTP","there");

                                                }



                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {
                                        DynamicToast.makeError(getApplicationContext(), t.getLocalizedMessage()).show();
                                    }
                                });


                    }
                } else {
                    inputEditTextEmail.setError("Invalid Email");
                }


            }
        });
    }

}


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();

    }
}
