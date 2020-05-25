package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.okazo.util.constants;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;
import static com.example.okazo.util.constants.KEY_STAFF_EMAIL;
import static com.example.okazo.util.constants.KEY_STAFF_ID;
import static com.example.okazo.util.constants.KEY_STAFF_ROLE;

public class AdminActivity extends AppCompatActivity {
    private CardView cardViewRegisterStaff,cardViewEvent,cardViewUser,cardViewPost,cardViewTMoney,cardViewAllStaff;
    private String staffId,staffRole,staffEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        cardViewRegisterStaff=findViewById(R.id.admin_activity_register_staff);
        cardViewAllStaff=findViewById(R.id.admin_activity_all_staff);
        cardViewEvent=findViewById(R.id.admin_activity_all_event);

        cardViewTMoney=findViewById(R.id.admin_activity_t_money);
        cardViewUser=findViewById(R.id.admin_activity_all_user);
        Bundle bundle=getIntent().getExtras();


        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(constants.KEY_SHARED_PREFERENCE, MODE_PRIVATE);
        if(sharedPreferences1.getString("user_id","")!=null && !sharedPreferences1.getString("user_id","").isEmpty()){
            staffId=sharedPreferences1.getString("user_id","");
            staffEmail=sharedPreferences1.getString("user_email","");
            staffRole=sharedPreferences1.getString("staff_role","");
        }
        else {
            staffId=bundle.getString(KEY_STAFF_ID);
            staffRole=bundle.getString(KEY_STAFF_ROLE);
            staffEmail=bundle.getString(KEY_STAFF_EMAIL);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(constants.KEY_SHARED_PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor shared_editor = sharedPreferences.edit();
            shared_editor.putString("user_email", staffEmail);
            shared_editor.putString("user_id", staffId);
            shared_editor.putString("staff_role", staffRole);
            shared_editor.commit();
        }

        if(staffRole.equals("2")){
            //admin
            cardViewRegisterStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent=new Intent(AdminActivity.this,RegisterActivity.class);
                        intent.putExtra("condition","admin");
                        startActivity(intent);
                }
            });

            cardViewAllStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(AdminActivity.this,AdminStaffActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            //staff
            cardViewRegisterStaff.setCardBackgroundColor(getColor(R.color.mapbox_plugins_material_grey_200));
            cardViewAllStaff.setCardBackgroundColor(getColor(R.color.mapbox_plugins_material_grey_200));
            cardViewAllStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DynamicToast.makeWarning(getApplicationContext(),"You dont permisison to this feature");
                }
            });
            cardViewRegisterStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DynamicToast.makeWarning(getApplicationContext(),"You dont permisison to this feature");
                }
            });
        }
        cardViewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminActivity.this,AdminEventActivity.class);
                intent.putExtra(KEY_STAFF_ID,staffId);
                startActivity(intent);
            }
        });
        cardViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminActivity.this,AdminUserActivity.class);

                startActivity(intent);
            }
        });

        cardViewTMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminActivity.this,AdminTMoneyActivity.class);

                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.staff_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.staff_logout){
            DynamicToast.makeWarning(getApplicationContext(),"Logging Out").show();
            SharedPreferences pref = getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("user_email");
            editor.remove("user_id");
            editor.remove("staff_role");
            editor.commit();

            Intent intent3=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent3);
        }
        return super.onOptionsItemSelected(item);
    }
}
