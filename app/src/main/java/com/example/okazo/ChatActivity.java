package com.example.okazo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.okazo.Api.APIResponse;
import com.example.okazo.Api.ApiClient;
import com.example.okazo.Api.ApiInterface;
import com.example.okazo.Model.Chat;
import com.example.okazo.util.ChatAdapter;
import com.example.okazo.util.EventPreviewTicketTypeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.okazo.util.constants.KEY_ID_FOR_CHAT;
import static com.example.okazo.util.constants.KEY_RECEIVER_ID;
import static com.example.okazo.util.constants.KEY_SENDER_ID;
import static com.example.okazo.util.constants.KEY_SENDER_NAME;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
   // private Button button;
    private ApiInterface apiInterface;
    private String id;
    private ChatAdapter adapter;
    private ArrayList<String> arrayListSenderId=new ArrayList<>(),arrayListCreatedAt=new ArrayList<>(),arrayListName=new ArrayList<>(),
            arrayListImage=new ArrayList<>(),arrayListMessage=new ArrayList<>(),arrayListUnseenCount=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView=findViewById(R.id.chat_activity_recyclerview);
//        editText=findViewById(R.id.chat_activity_text);
//        button=findViewById(R.id.chat_activity_send);
        textView=findViewById(R.id.chat_activity_no_chat);
        Bundle intent=getIntent().getExtras();
        id=intent.getString(KEY_ID_FOR_CHAT);


        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        getAllChat();


    }

    private void setUpRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
         adapter=new ChatAdapter(arrayListSenderId,arrayListCreatedAt,arrayListName,arrayListImage,getApplicationContext(),arrayListMessage,arrayListUnseenCount);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnCardClickListener(new ChatAdapter.OnCardClickListener() {
            @Override
            public void onClick(int position, ArrayList<String> sender_id) {
                apiInterface.makeSeen(id,sender_id.get(position)).enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse apiResponse=response.body();

                        if(!apiResponse.getError()){
                            ((Button)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.card_chat_room_unseen) ).setVisibility(View.GONE);
                            Intent intent=new Intent(ChatActivity.this,MessageActivity.class);
                            intent.putExtra(KEY_SENDER_ID,sender_id.get(position));
                            intent.putExtra(KEY_RECEIVER_ID,id);
                            intent.putExtra(KEY_SENDER_NAME,arrayListName.get(position));
                            startActivity(intent);
                        }else {
                            DynamicToast.makeError(getApplicationContext(),apiResponse.getErrorMsg()).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {

                    }
                });

            }
        });


    }
    private void getAllChat(){

        apiInterface.getChatRoom(id).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse=response.body();
                if(!apiResponse.getError()){

                    ArrayList<Chat> chat=apiResponse.getChatArray();
                    for (Chat val:chat
                         ) {
                         arrayListSenderId.add(val.getSenderId());
                         arrayListCreatedAt.add(val.getCreated_at());
                         arrayListImage.add(val.getImage());
                         arrayListName.add(val.getName());
                         arrayListMessage.add(val.getMessage());
                         arrayListUnseenCount.add(val.getUnseenCount());
                    }

                    setUpRecyclerView();
                }else {
                    if(apiResponse.getErrorMsg().equals("NO CHAT")){
                        textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

                    }else {
                        DynamicToast.makeError(ChatActivity.this,"HERE").show();
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {

            }
        });
    }
}
