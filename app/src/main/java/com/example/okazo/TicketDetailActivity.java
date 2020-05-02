package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.okazo.util.constants.KEY_BUNDLE_EVENT_DETAIL;
import static com.example.okazo.util.constants.KEY_BUNDLE_TICKET_DETAIL;
import static com.example.okazo.util.constants.KEY_EVENT_DESCRIPTION;
import static com.example.okazo.util.constants.KEY_RADIO_TICKET_CATEGORY;
import static com.example.okazo.util.constants.KEY_TICKET_NUMBER;
import static com.example.okazo.util.constants.KEY_TICKET_PRICE;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_LIST;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_NAME_LIST;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_NUMBER_LIST;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_PRICE_LIST;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_SINGLE_NAME;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_SINGLE_NUMBER;
import static com.example.okazo.util.constants.KEY_TICKET_TYPE_SINGLE_PRICE;

public class TicketDetailActivity extends AppCompatActivity {
Button buttonAddMore;
LinearLayout linearLayout=null;
int counter=1;
int viewCounter=1;
RadioGroup radioGroup;
RadioButton radioButton;
private ImageView imageViewNext;
private Bundle extra;
LinearLayout linearLayoutTicketTypes;
private TextView textViewSingleTicketNumberError,textViewSingleTicketPriceError,textViewTicketTypeName,textViewTicketTypePrice,textViewTicketTypeNumber;
TextInputEditText inputEditTextOneNumberTicket,inputEditTextOneTicketPrice,inputEditTextTicketTypeName,inputEditTextTicketTypePrice,inputEditTextTicketTypeNumber;
    private TextInputLayout textInputLayoutSingleNumberTicket,textInputLayoutSinglePriceTicket;
ArrayList<TextInputEditText> editTextsList=new ArrayList<TextInputEditText>();
List<TextView> textViewsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ticket_detail);
       // getSupportActionBar().hide();
        Toolbar toolbar=findViewById(R.id.toolbar_ticket);
        setSupportActionBar(toolbar);
        buttonAddMore=findViewById(R.id.event_ticket_add_more);
        inputEditTextOneNumberTicket=findViewById(R.id.event_ticket_number);
        inputEditTextOneTicketPrice=findViewById(R.id.event_ticket_price_single);
        textViewSingleTicketNumberError=findViewById(R.id.ticket_number_single_error);
        linearLayoutTicketTypes=findViewById(R.id.event_ticket_type_layout);
        textViewSingleTicketPriceError=findViewById(R.id.ticket_price_single_error);
        textViewTicketTypeName=findViewById(R.id.ticket_type_name_error);
        textViewTicketTypePrice=findViewById(R.id.ticket_type_price_error);
        textViewTicketTypeNumber=findViewById(R.id.ticket_type_number_error);
        textInputLayoutSingleNumberTicket=findViewById(R.id.event_ticket_textview);
        textInputLayoutSinglePriceTicket=findViewById(R.id.event_ticket_price_layout);
        textInputLayoutSingleNumberTicket.setVisibility(View.GONE);
        textInputLayoutSinglePriceTicket.setVisibility(View.GONE);

        inputEditTextTicketTypeName=findViewById(R.id.event_ticket_first_ticket_name);
        inputEditTextTicketTypePrice=findViewById(R.id.event_ticket_first_ticket_price);
        inputEditTextTicketTypeNumber=findViewById(R.id.event_ticket_first_ticket_number);

         extra=getIntent().getExtras();
        //Log.d("bundleExtra",extra.getString(KEY_EVENT_DESCRIPTION));




        radioGroup=findViewById(R.id.radio_group);
        imageViewNext=findViewById(R.id.toolbar_next);

        imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID=radioGroup.getCheckedRadioButtonId();
                radioButton=findViewById(selectedID);
                int counter=0;
                if(radioButton.getText().toString().toLowerCase().equals("no")){
                    //NO
                   if( inputEditTextOneNumberTicket.getText().toString().equals("")){
                       textViewSingleTicketNumberError.setText("Please enter number of ticket");
                       inputEditTextOneNumberTicket.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_error),null);
                   }else {
                       counter+=1;
                       textViewSingleTicketNumberError.setText("");
                       inputEditTextOneNumberTicket.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                   }
                   if(inputEditTextOneTicketPrice.getText().toString().equals("")){
                       textViewSingleTicketPriceError.setText("Please enter ticket price");
                       inputEditTextOneTicketPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_error),null);

                   }else {
                       counter+=1;
                       textViewSingleTicketPriceError.setText("");
                       inputEditTextOneTicketPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                   }
                   if(counter==2){
                       Intent intent=new Intent(TicketDetailActivity.this,EventDetailPreviewActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString(KEY_TICKET_NUMBER,inputEditTextOneNumberTicket.getText().toString());
                        bundle.putString(KEY_TICKET_PRICE,inputEditTextOneTicketPrice.getText().toString());
                        bundle.putString(KEY_RADIO_TICKET_CATEGORY,radioButton.getText().toString());
                        extra.putBundle(KEY_BUNDLE_TICKET_DETAIL,bundle);
                        intent.putExtras(extra);
//                        intent.putExtra(KEY_BUNDLE_EVENT_DETAIL,extra);
//                        intent.putExtra(KEY_BUNDLE_TICKET_DETAIL,bundle);
                       startActivity(intent);
                       //Toast.makeText(TicketDetailActivity.this, "DONE", Toast.LENGTH_SHORT).show();
                   }
                }else {
                    //YES
                    //for edit text ij list
                    int editTextValidCounter=0;
                    int listCounter=editTextsList.size();
                   //
                    if(inputEditTextTicketTypeName.getText().toString().equals("")){
                        textViewTicketTypeName.setText("Please enter asdasdasd");
                        inputEditTextTicketTypeName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_error),null);
                    }else {
                        counter+=1;
                        textViewTicketTypeName.setText("");
                        inputEditTextTicketTypeName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                    }
                    if(inputEditTextTicketTypePrice.getText().toString().equals("")){
                        inputEditTextTicketTypePrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_error),null);
                        textViewTicketTypePrice.setText("Please enter Value");
                    }else {
                        counter+=1;
                        inputEditTextTicketTypePrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                        textViewTicketTypePrice.setText("");
                    }
                    if(inputEditTextTicketTypeNumber.getText().toString().equals("")){
                        inputEditTextTicketTypeNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.ic_error),null);
                        textViewTicketTypeNumber.setText("Please enter value");
                    }else {
                        counter+=1;
                        inputEditTextTicketTypeNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                        textViewTicketTypeNumber.setText("");
                    }
//                    if(listCounter==0) {
//                        // only one ticket type
//
//                    if(counter==3){
//                        Toast.makeText(TicketDetailActivity.this, "check"+"single DONE", Toast.LENGTH_SHORT).show();
////
//                    }
//                      //
//                    }
//                    else {
                        //multiple ticket type

                        for (int i = 0; i < listCounter; i++) {


                            if (editTextsList.get(i).getText().toString().equals("")) {
                                textViewsList.get(i).setVisibility(View.VISIBLE);
                                textViewsList.get(i).setText("Please Enter value");
                                textViewsList.get(i).setTextColor(Color.RED);
                                editTextsList.get(i).setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_error), null);
                            } else {
                                editTextValidCounter += 1;

                                textViewsList.get(i).setText("");
                                textViewsList.get(i).setVisibility(View.GONE);
                                editTextsList.get(i).setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                            }


                        }
                    if(counter==3 && editTextValidCounter==listCounter){
                        ArrayList<String> ticketTypeNameList=new ArrayList<>(),ticketTypePriceList=new ArrayList<>(),ticketTypeNumberList=new ArrayList<>();
                        for (int i=0;i<editTextsList.size()-1;i=i+3){

                            ticketTypeNameList.add(editTextsList.get(i).getText().toString());
                            ticketTypePriceList.add((editTextsList.get(i+1).getText().toString()));
                            ticketTypeNumberList.add((editTextsList.get(i+2).getText().toString()));
                        }

                        Intent intent1=new Intent(TicketDetailActivity.this,EventDetailPreviewActivity.class);
                        Bundle bundle=new Bundle();

                        bundle.putString(KEY_RADIO_TICKET_CATEGORY,radioButton.getText().toString());
                        bundle.putString(KEY_TICKET_TYPE_SINGLE_NAME,inputEditTextTicketTypeName.getText().toString());
                        bundle.putString(KEY_TICKET_TYPE_SINGLE_NUMBER,inputEditTextTicketTypeNumber.getText().toString());
                        bundle.putString(KEY_TICKET_TYPE_SINGLE_PRICE,inputEditTextTicketTypePrice.getText().toString());
                        //bundle.putSerializable(KEY_TICKET_TYPE_LIST,editTextsList);
                        bundle.putSerializable(KEY_TICKET_TYPE_NAME_LIST,ticketTypeNameList);
                        bundle.putSerializable(KEY_TICKET_TYPE_PRICE_LIST,ticketTypePriceList);
                        bundle.putSerializable(KEY_TICKET_TYPE_NUMBER_LIST,ticketTypeNumberList);

                        extra.putBundle(KEY_BUNDLE_TICKET_DETAIL,bundle);

                        intent1.putExtras(extra);
//                        intent.putExtra(KEY_BUNDLE_EVENT_DETAIL,extra);
//                        intent.putExtra(KEY_BUNDLE_TICKET_DETAIL,bundle);
                        startActivity(intent1);




                    }
                    }





                }
           // }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton=findViewById(checkedId);
                if(radioButton.getText().toString().toLowerCase().equals("yes")){
                        linearLayoutTicketTypes.setVisibility(View.VISIBLE);
                        buttonAddMore.setVisibility(View.VISIBLE);
                    textInputLayoutSinglePriceTicket.setVisibility(View.GONE);
                    textInputLayoutSingleNumberTicket.setVisibility(View.GONE);
                    textViewSingleTicketNumberError.setVisibility(View.GONE);
                    textViewSingleTicketPriceError.setVisibility(View.GONE);
                    textViewSingleTicketPriceError.setText("");
                    textViewSingleTicketNumberError.setText("");
                    inputEditTextOneTicketPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                    inputEditTextOneNumberTicket.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                    int listCounter=editTextsList.size();
                    for (int i=0;i<listCounter;i++){
                        textViewsList.get(i).setText("");
                        textViewsList.get(i).setVisibility(View.GONE);
                        editTextsList.get(i).setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                    }
                }else {
                    linearLayoutTicketTypes.setVisibility(View.GONE);
                    buttonAddMore.setVisibility(View.GONE);
                    textInputLayoutSingleNumberTicket.setVisibility(View.VISIBLE);
                    textInputLayoutSinglePriceTicket.setVisibility(View.VISIBLE);
                    textViewSingleTicketNumberError.setVisibility(View.VISIBLE);
                    textViewSingleTicketPriceError.setVisibility(View.VISIBLE);

                    textViewTicketTypeName.setText("");
                    textViewTicketTypePrice.setText("");
                    textViewTicketTypeNumber.setText("");
                    inputEditTextTicketTypeNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                    inputEditTextTicketTypePrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                    inputEditTextTicketTypeName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                }
                //Toast.makeText(TicketDetailActivity.this, "te"+radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        buttonAddMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                linearLayout=findViewById(R.id.event_ticket_type_layout);

                CardView materialCardView2=new CardView(TicketDetailActivity.this);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                materialCardView2.setLayoutParams(layoutParams);
                float scale=getResources().getDisplayMetrics().density;
                int dpAsPixel=(int) (7*scale+0.5f);
                layoutParams.setMargins(0,15,0,0);
                linearLayout.setPadding(dpAsPixel,dpAsPixel,dpAsPixel,dpAsPixel);
            materialCardView2.setRadius(dpAsPixel);
                 dpAsPixel=(int) (5*scale+0.5f);
            materialCardView2.setElevation(dpAsPixel);

                //Relative layout
                LinearLayout relativeLayout=new LinearLayout(TicketDetailActivity.this);
                relativeLayout.setLayoutParams(new MaterialCardView.LayoutParams(
                        MaterialCardView.LayoutParams.MATCH_PARENT,
                        MaterialCardView.LayoutParams.WRAP_CONTENT
                ));
relativeLayout.setOrientation(LinearLayout.VERTICAL);
relativeLayout.setPadding(4,4,4,4);

                LinearLayout.LayoutParams param5=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                ImageButton imageButton=new ImageButton(TicketDetailActivity.this);
                imageButton.setLayoutParams(param5);
                imageButton.setBackground(getResources().getDrawable(R.drawable.ic_wrong));
                param5.gravity=Gravity.RIGHT;
                relativeLayout.addView(imageButton);

                //first edit text layout
                LinearLayout.LayoutParams param1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                TextInputLayout textInputLayout=new TextInputLayout(TicketDetailActivity.this);


                textInputLayout.setLayoutParams(param1);



                TextInputEditText textInputEditText=new TextInputEditText(TicketDetailActivity.this);
                textInputEditText.setLayoutParams(new TextInputLayout.LayoutParams(
                        TextInputLayout.LayoutParams.MATCH_PARENT,
                        TextInputLayout.LayoutParams.MATCH_PARENT
                ));
                //first edit text
//                TextInputEditText textInputEditText=new TextInputEditText(TicketDetailActivity.this);
//                textInputEditText.setLayoutParams(new TextInputLayout.LayoutParams(
//                        TextInputLayout.LayoutParams.WRAP_CONTENT,
//                        TextInputLayout.LayoutParams.WRAP_CONTENT
//                ));
                textInputEditText.setId(counter);
                textInputEditText.setHint("Name");
                textInputLayout.addView(textInputEditText);

                LinearLayout.LayoutParams param6=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView textView=new TextView(TicketDetailActivity.this);
                textView.setText("");
                textView.setLayoutParams(param6);


                relativeLayout.addView(textInputLayout);
                relativeLayout.addView(textView);
                counter+=1;
                //Second edit text layout
                LinearLayout.LayoutParams param2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                TextInputLayout textInputLayout1=new TextInputLayout(TicketDetailActivity.this);

                //param2.addRule(RelativeLayout.BELOW,1);

                textInputLayout1.setLayoutParams(param2);

                //second edit text
              TextInputEditText textInputEditText1=new TextInputEditText(TicketDetailActivity.this);
                textInputEditText1.setLayoutParams(new TextInputLayout.LayoutParams(
                        TextInputLayout.LayoutParams.MATCH_PARENT,
                        TextInputLayout.LayoutParams.MATCH_PARENT
                ));
                textInputEditText1.setId(counter);
                textInputEditText1.setHint("price");

                textInputLayout1.addView(textInputEditText1);
                counter+=1;
                relativeLayout.addView(textInputLayout1);
                LinearLayout.LayoutParams param7=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView textView1=new TextView(TicketDetailActivity.this);
                textView.setText("");
                textView1.setLayoutParams(param7);
                relativeLayout.addView(textView1);
                //third edit text layout
                LinearLayout.LayoutParams param4=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                TextInputLayout textInputLayout3=new TextInputLayout(TicketDetailActivity.this);
                textInputLayout3.setLayoutParams(param4);
                //third edit text
                TextInputEditText textInputEditText2=new TextInputEditText(TicketDetailActivity.this);
                textInputEditText2.setLayoutParams(new TextInputLayout.LayoutParams(
                        TextInputLayout.LayoutParams.MATCH_PARENT,
                        TextInputLayout.LayoutParams.MATCH_PARENT
                ));
                textInputEditText2.setId(counter);
                textInputEditText2.setHint("Number");
                textInputLayout3.addView(textInputEditText2);
                counter+=1;
                relativeLayout.addView(textInputLayout3);
                LinearLayout.LayoutParams param8=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView textView2=new TextView(TicketDetailActivity.this);
                textView.setText("");
                textView2.setLayoutParams(param8);
                relativeLayout.addView(textView2);


                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editTextsList.remove(textInputEditText);
                        editTextsList.remove(textInputEditText1);
                        editTextsList.remove(textInputEditText2);
                        linearLayout.removeView(materialCardView2);
                    }
                });
                textInputEditText1.setInputType(InputType.TYPE_CLASS_NUMBER);
                textInputEditText2.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextsList.add(textInputEditText);
                editTextsList.add(textInputEditText1);
                editTextsList.add(textInputEditText2);
                textViewsList.add(textView);
                textViewsList.add(textView1);
                textViewsList.add(textView2);
               // relativeLayout.addView(button);
                materialCardView2.addView(relativeLayout);
                linearLayout.addView(materialCardView2);
                viewCounter+=1;
            }
        });
    }

}
