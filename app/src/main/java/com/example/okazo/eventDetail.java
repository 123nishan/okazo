package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.EventDetail;
import com.example.okazo.util.DateTimePicker;
import com.example.okazo.util.EventTypeAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
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
    private Button buttonNext;
    RecyclerView recyclerView;
    private TextView spinnerError;
    private Boolean dateStatus=false,timeStatus=false,spinnerStatus=false,titleStatus=false;
    private TextInputEditText inputEditTextEventName,inputEditTextEventDate,inputEditTextEventTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        eventTypeSpinner=findViewById(R.id.event_detail_event_type_spinner);
       apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
       buttonNext=findViewById(R.id.event_detail_first_button);
        inputEditTextEventDate=findViewById(R.id.event_detail_event_date);
        inputEditTextEventTime=findViewById(R.id.event_detail_event_time);
        inputEditTextEventName=findViewById(R.id.event_name);
        spinnerError=(TextView)eventTypeSpinner.getSelectedView();
        buttonNext.setVisibility(View.GONE);

        inputEditTextEventDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP==event.getAction()){
                    DateTimePicker dateTimePicker=new DateTimePicker();
                    dateTimePicker.showDatePickerDialog(eventDetail.this,inputEditTextEventDate,"date");
                }

                return false;
            }
        });
        inputEditTextEventTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP==event.getAction()){
                    DateTimePicker dateTimePicker=new DateTimePicker();
                    dateTimePicker.showDatePickerDialog(eventDetail.this,inputEditTextEventTime,"time");
                }
                return false;
            }
        });
        inputEditTextEventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(count>4){
                        titleStatus=true;

                        inputEditTextEventName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_correct,null),null);
                    }else {
                        inputEditTextEventName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_wrong,null),null);
                        titleStatus=false;
                    }
                if(dateStatus && timeStatus && spinnerStatus && timeStatus){
                    buttonNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputEditTextEventDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>=10){
                    inputEditTextEventDate.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_correct,null),null);
                    dateStatus=true;
                }else {
                    inputEditTextEventDate.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_wrong,null),null);
                }
                if(dateStatus && timeStatus && spinnerStatus && titleStatus){
                    buttonNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputEditTextEventTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>=4){
                    inputEditTextEventTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_correct,null),null);
                    timeStatus=true;
                }else {
                    inputEditTextEventTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null, ResourcesCompat.getDrawable(getResources(),R.drawable.ic_wrong,null),null);
                }
                if(dateStatus && timeStatus && spinnerStatus && titleStatus){
                    buttonNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       buttonNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(eventDetail.this,EventLocationActivity.class);
               startActivity(intent);

           }
       });
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
        recyclerView=findViewById(R.id.event_detail_recycler_view);
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

                  if(recyclerView.getChildCount()==1){
                      spinnerStatus=false;


                      spinnerError=(TextView)eventTypeSpinner.getSelectedView();
                      spinnerError.setTextColor(Color.RED);

                      buttonNext.setVisibility(View.GONE);

                    //

                  }else {
                      spinnerStatus=true;
                      if(dateStatus && timeStatus && spinnerStatus && titleStatus){
                          buttonNext.setVisibility(View.VISIBLE);
                      }
                  }

//                 Log.d("setis: ", String.valueOf(selectedEventType.get(position).getEventType()));
      adapter.notifyDataSetChanged();
            }
         });




    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position>0) {

            if (selectedEventType.size() >= 5) {
                Toast.makeText(this, "you cant select more than 4", Toast.LENGTH_SHORT).show();
            } else {

                String item = parent.getItemAtPosition(position).toString();
                spinnerStatus=true;
                EventDetail eventDetail = new EventDetail(item);

                if(dateStatus && timeStatus && spinnerStatus && titleStatus){
                    buttonNext.setVisibility(View.VISIBLE);
                }
                if (!set.contains(item)) {
                    set.add(item);

                    selectedEventType.add(eventDetail);


                    adapter.notifyDataSetChanged();

                    //
                }
                eventTypeSpinner.setSelection(0);
            }
        }

        }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
