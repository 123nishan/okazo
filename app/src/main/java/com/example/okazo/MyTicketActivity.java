package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.MyEventTicketAdapter;
import com.example.okazo.util.PostCommentAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_SHARED_PREFERENCE;

public class MyTicketActivity extends AppCompatActivity {
private String userId;
private RecyclerView recyclerView;
private ApiInterface apiInterface;
private MyEventTicketAdapter adapter;
private ArrayList<String> arrayListTitle=new ArrayList<>(),arrayListImage=new ArrayList<>(),arrayListStartDate=new ArrayList<>(),arrayListStartTime=new ArrayList<>(),arrayListTotalTicket=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        recyclerView=findViewById(R.id.my_ticket_activity_recycler_view);
        getSupportActionBar().setTitle("Tickets");
        SharedPreferences sharedPreferences = MyTicketActivity.this.getSharedPreferences(KEY_SHARED_PREFERENCE, MODE_PRIVATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.my_ticket_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart:
                Intent intent=new Intent(MyTicketActivity.this,CartActivity.class);
                startActivity(intent);
               // Toast.makeText(this, "CART", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
