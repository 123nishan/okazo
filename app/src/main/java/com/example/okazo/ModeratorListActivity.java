package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.Model.Note;
import com.example.okazo.util.ConfirmationDialog;
import com.example.okazo.util.ModeratorListAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_USER_ID;

public class ModeratorListActivity extends AppCompatActivity implements ConfirmationDialog.orderConfirmationListener {
    private String userId;
    private RecyclerView recyclerView;
    private ModeratorListAdapter adapter;
    private ApiInterface apiInterface;
    private ArrayList<String> eventName=new ArrayList<>(),eventImage=new ArrayList<>(),eventId=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_list);
        Bundle bundle=getIntent().getExtras();
        userId=bundle.getString(KEY_USER_ID);
        recyclerView=findViewById(R.id.moderator_list_activity_recycler_view);

        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.getModeratorListUser(userId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                        ArrayList<EventDetail> eventDetails=apiResponse.getEventArray();
                    for (EventDetail val:eventDetails
                         ) {
                        eventId.add(val.getId());
                        eventImage.add(val.getImage());
                        eventName.add(val.getTitle());
                    }
                    adapter=new ModeratorListAdapter(eventName,eventImage,getApplicationContext());
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnClickListener(new ModeratorListAdapter.OnClickListener() {
                        @Override
                        public void onClick(int position) {
                            ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to leave this event?");
                            confirmationDialog.show(getSupportFragmentManager(),"Confirmation");
                        }
                    });
                }else {
                    DynamicToast.makeError(getApplicationContext(),apiResponse.getErrorMsg()).show();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });


    }

    @Override
    public void OnYesClicked() {
        DynamicToast.makeSuccess(getApplicationContext(),"DELETE").show();
    }

    @Override
    public void OnNoClicked() {

    }
}
