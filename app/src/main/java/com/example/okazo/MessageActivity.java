package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.Chat;
import com.example.okazo.util.ChatAdapter;
import com.example.okazo.util.MessageAdapter;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_RECEIVER_ID;
import static com.example.okazo.util.constants.KEY_SENDER_ID;
import static com.example.okazo.util.constants.KEY_SENDER_NAME;

public class MessageActivity extends AppCompatActivity {
    private TextView textViewName,textViewError;
    private RecyclerView recyclerView;
    private EditText editText;
    private Button button;
    private ApiInterface apiInterface;
    private String senderId,currentUser,senderName;
    private MessageAdapter adapter;
    private LinearLayout linearLayout;
    ArrayList<String> arrayListMessage=new ArrayList<>(),arrayListCreatedAt=new ArrayList<>(),arrayListSenderId=new ArrayList<>(),arrayListReceiverId=new ArrayList<>(),arrayListImage=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        textViewName=findViewById(R.id.message_activity_name);
        linearLayout=findViewById(R.id.message_activity_send_message_layout);
        editText=findViewById(R.id.message_activity_message);
        button=findViewById(R.id.message_activiy_send_message);
        textViewError=findViewById(R.id.message_activity_account_blocked);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        Bundle bundle=getIntent().getExtras();
        senderId=bundle.getString(KEY_SENDER_ID);
        currentUser=bundle.getString(KEY_RECEIVER_ID);
        senderName=bundle.getString(KEY_SENDER_NAME);
        textViewName.setText(senderName);
        button.setVisibility(View.GONE);
        getMessage();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        String sendMessage=editText.getText().toString();
                                apiInterface.sendMessgae(senderId,currentUser,sendMessage).enqueue(new Callback<APIResponse>() {
                                    @Override
                                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                        APIResponse apiResponse=response.body();
                                        if(!apiResponse.getError()){
                                            ArrayList<Chat> chats=apiResponse.getChatArray();
                                            for (Chat val:chats
                                                 ) {
                                                arrayListImage.add(val.getImage());
                                                arrayListSenderId.add(val.getSenderId());
                                                arrayListReceiverId.add(val.getReceiverId());
                                                arrayListCreatedAt.add(val.getCreated_at());
                                                arrayListMessage.add(val.getMessage());
                                            }
                                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                                            apiInterface.sendInboxNotification(sendMessage,currentUser).enqueue(new Callback<APIResponse>() {
                                                @Override
                                                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                                    APIResponse response1=response.body();
                                                    if(!response1.getError()){

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<APIResponse> call, Throwable t) {

                                                }
                                            });
                                            editText.clearFocus();
                                            editText.setText("");
                                            button.setVisibility(View.GONE);
                                            for (String test:arrayListMessage
                                                 ) {
                                                Log.d("MESSAGE",test);
                                            }
//                                            adapter.notifyDataSetChanged();
//                                            recyclerView.scrollToPosition(arrayListMessage.size()-1);
                                        }else {
                                          if(apiResponse.getErrorMsg().equals("2") || apiResponse.getErrorMsg().equals("3") || apiResponse.getErrorMsg().equals("4")){
                                                    linearLayout.setVisibility(View.GONE);
                                                    textViewError.setVisibility(View.VISIBLE);

                                          }else {
                                              DynamicToast.makeError(MessageActivity.this,apiResponse.getErrorMsg()).show();
                                          }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<APIResponse> call, Throwable t) {

                                    }
                                });
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView=findViewById(R.id.message_activity_recyclerview);
        recyclerView.setVisibility(View.VISIBLE);
        adapter=new MessageAdapter(arrayListMessage,arrayListCreatedAt,arrayListSenderId,arrayListReceiverId,currentUser,getApplicationContext(),arrayListImage);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

       recyclerView.setAdapter(adapter);


    }
    private void getMessage(){
        apiInterface.getMessage(currentUser,senderId).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){
                    ArrayList<Chat> chats=apiResponse.getChatArray();
                    for (Chat value:chats
                         ) {
                                arrayListMessage.add(value.getMessage());
                                arrayListCreatedAt.add(value.getCreated_at());
                                arrayListSenderId.add(value.getSenderId());
                                arrayListReceiverId.add(value.getReceiverId());
                                arrayListImage.add(value.getImage());
                    }
                    adapter.notifyDataSetChanged();


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
    protected void onStart() {
        super.onStart();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(adapter);
    }
}
