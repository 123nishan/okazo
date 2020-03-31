package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.EventTypeAdapter;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class eventDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
AppCompatSpinner eventTypeSpinner;
    private ApiInterface apiInterface;
    private  ArrayList<String> arraySpinner;
    private ArrayList<EventDetail> selectedEventType=new ArrayList<EventDetail>();
    private HashSet<String> set=new HashSet<>();
    private EventTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        eventTypeSpinner=findViewById(R.id.event_detail_event_type_spinner);
       apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
      apiInterface.getEventType().enqueue(new Callback<ArrayList<EventDetail>>() {
          @Override
          public void onResponse(Call<ArrayList<EventDetail>> call, Response<ArrayList<EventDetail>> response) {
              ArrayList<EventDetail> eventDetail=response.body();
          arraySpinner=new ArrayList<>();
            arraySpinner.add("Select an tag");
              for (EventDetail eventType:eventDetail
                   ) {

                  arraySpinner.add(eventType.getEventType());
                  Log.d("eventType",eventType.getEventType());
              }
              ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,arraySpinner);
              adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
              eventTypeSpinner.setAdapter(adapter);
          }

          @Override
          public void onFailure(Call<ArrayList<EventDetail>> call, Throwable t) {
              Toast.makeText(eventDetail.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
          }
      });
        RecyclerView recyclerView=findViewById(R.id.event_detail_recycler_view);
        adapter=new EventTypeAdapter(selectedEventType);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        eventTypeSpinner.setOnItemSelectedListener(this);
        adapter.setRemoveClickListner(new EventTypeAdapter.OnRemoveClickListner() {
             @Override
                 public void onRemoveClick(int position, ArrayList<EventDetail> eventDetails) {
                 set.remove(eventDetails.get(position).getEventType());
                  selectedEventType.remove(position);

//                 Log.d("setis: ", String.valueOf(selectedEventType.get(position).getEventType()));
      adapter.notifyDataSetChanged();
            }
         });




    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position>0) {
            String item = parent.getItemAtPosition(position).toString();

            EventDetail eventDetail=new EventDetail(item);

                if(!set.contains(item)){
                    set.add(item);

                    selectedEventType.add(eventDetail);
                    Toast.makeText(this, "asdasd"+item, Toast.LENGTH_SHORT).show();

                    adapter.notifyDataSetChanged();
                }
            }

        }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
