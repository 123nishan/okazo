package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.okazo.util.MyTicketAdapter;
import com.example.okazo.util.TicketQrAdapter;

import java.util.ArrayList;

import static com.example.okazo.util.constants.KEY_EVENT_TITLE;
import static com.example.okazo.util.constants.KEY_TICKET_ID_ARRAY;
import static com.example.okazo.util.constants.KEY_TICKET_NAME;
import static com.example.okazo.util.constants.KEY_TICKET_QUANTITY;

public class TicketQrActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private TextView textViewTotalQty;

private String eventName,ticketName,ticketId,ticketQuantity;
private int totalQty;
private TicketQrAdapter ticketQrAdapter;
private ArrayList<String> arrayListId=new ArrayList<>(),arrayListName=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_qr);
        recyclerView=findViewById(R.id.ticket_qr_activity_recycler_view);
        textViewTotalQty=findViewById(R.id.ticket_qr_activity_total_qty);
        Bundle bundle=getIntent().getExtras();
        eventName=bundle.getString(KEY_EVENT_TITLE);
        ticketName=bundle.getString(KEY_TICKET_NAME);
        ticketId=bundle.getString(KEY_TICKET_ID_ARRAY);
        ticketQuantity=bundle.getString(KEY_TICKET_QUANTITY);
        getSupportActionBar().setTitle(eventName);
        textViewTotalQty.setText("Total Quantity: "+ticketQuantity);
        totalQty=Integer.valueOf(ticketQuantity);
        for(int i=0;i<totalQty;i++){
            arrayListId.add(ticketId);
            arrayListName.add(ticketName);
        }
        ticketQrAdapter=new TicketQrAdapter(arrayListId,arrayListName,TicketQrActivity.this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(TicketQrActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ticketQrAdapter);

    }
}
