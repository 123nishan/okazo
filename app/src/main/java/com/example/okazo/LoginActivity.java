package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;


public class LoginActivity extends AppCompatActivity {
    Button buttonLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextInputEditText inputEditTextEmail,inputEditTextPassword;
    String email,password;
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
                if (email.matches(emailPattern)){
                        inputEditTextEmail.setError(null);
                    if(password.isEmpty()){
                        inputEditTextPassword.setError("Error");
                    }else {
                        inputEditTextPassword.setError(null);
                        DynamicToast.makeSuccess(getApplicationContext(),email+" "+password).show();
                    }
                }else{
                    inputEditTextEmail.setError("Invalid Email");
                }


            }
        });
    }

}
