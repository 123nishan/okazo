package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.okazo.util.JavaMailAPI;
import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    Button buttonLogin;
    TextView textViewRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextInputEditText inputEditTextEmail, inputEditTextPassword;
    String email, password;
    ApiInterface apiInterface;
    ProgressBar progressBar;
    String sharedPreferencesConstant = "hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(sharedPreferencesConstant, MODE_PRIVATE);

        if (sharedPreferences.getString("user_email", "") != null && !sharedPreferences.getString("user_email", "").isEmpty()) {
            Toast.makeText(this, "email: "+sharedPreferences.getString("user_email", ""), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("email", sharedPreferences.getString("user_email", ""));
            startActivity(intent);
        } else {

        setContentView(R.layout.activity_login);
        buttonLogin = findViewById(R.id.login_submit);
        inputEditTextEmail = findViewById(R.id.login_email);
        inputEditTextPassword = findViewById(R.id.login_password);
        textViewRegister = findViewById(R.id.login_register);
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        textViewRegister.setOnClickListener(new View.OnClickListener() {
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

                if (email.matches(emailPattern)) {
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
                                            if(result.getUser().getVerified().equals("non-verified")){

                                                Random random=new Random();
                                                int num=random.nextInt(900000)+100000;
                                                Date date=new Date();
                                                Timestamp timestamp=new Timestamp(date.getTime());
                                                String verified="non-verified";

                                                    apiInterface.otp(email,String.valueOf(num),verified,timestamp).enqueue(new Callback<APIResponse>() {
                                                        @Override
                                                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                                            APIResponse apiResponse=response.body();

                                                            if(apiResponse.getError()){
                                                                DynamicToast.makeError(getApplicationContext(),apiResponse.getErrorMsg()).show();
                                                            }else {
                                                                String identifier="frist";
                                                                JavaMailAPI javaMailAPI=new JavaMailAPI(LoginActivity.this,"nishan.nishan.timalsena@gmail.com",
                                                                        "Verification Code","OTP is:",num+"","","","",email,identifier);
                                                                javaMailAPI.execute();

                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<APIResponse> call, Throwable t) {
                                                            DynamicToast.makeError(getApplicationContext(), t.getLocalizedMessage()).show();
                                                        }
                                                    });
                                            }else {
                                                progressBar.setProgress(100);
                                                Toast.makeText(LoginActivity.this, "email:" + email, Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                intent1.putExtra("email", email);
                                                startActivity(intent1);
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
