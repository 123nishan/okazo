package com.example.okazo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TicketDetailActivity extends AppCompatActivity {
MaterialButton buttonAddMore;
LinearLayout linearLayout=null;
int counter=1;
int viewCounter=1;
RadioGroup radioGroup;
RadioButton radioButton;
LinearLayout linearLayoutTicketTypes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ticket_detail);
       // getSupportActionBar().hide();
        Toolbar toolbar=findViewById(R.id.toolbar_ticket);
        setSupportActionBar(toolbar);
        buttonAddMore=findViewById(R.id.event_ticket_add_more);
        linearLayoutTicketTypes=findViewById(R.id.event_ticket_type_layout);
        radioGroup=findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton=findViewById(checkedId);
                if(radioButton.getText().toString().toLowerCase().equals("yes")){
                        linearLayoutTicketTypes.setVisibility(View.VISIBLE);
                        buttonAddMore.setVisibility(View.VISIBLE);
                }else {
                    linearLayoutTicketTypes.setVisibility(View.GONE);
                    buttonAddMore.setVisibility(View.GONE);
                }
                //Toast.makeText(TicketDetailActivity.this, "te"+radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonAddMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                linearLayout=findViewById(R.id.event_ticket_type_layout);

                MaterialCardView materialCardView2=new MaterialCardView(TicketDetailActivity.this);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                materialCardView2.setLayoutParams(layoutParams);
                layoutParams.setMargins(0,8,0,0);


                //Relative layout
                LinearLayout relativeLayout=new LinearLayout(TicketDetailActivity.this);
                relativeLayout.setLayoutParams(new MaterialCardView.LayoutParams(
                        MaterialCardView.LayoutParams.MATCH_PARENT,
                        MaterialCardView.LayoutParams.WRAP_CONTENT
                ));
relativeLayout.setOrientation(LinearLayout.VERTICAL);

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

                relativeLayout.addView(textInputLayout);
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
                    //remove button
                LinearLayout.LayoutParams param3=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
                Button button=new Button(TicketDetailActivity.this);
                button.setId(counter);
                button.setText("Remove");
                button.setLayoutParams(param3);
                counter+=1;

                button.setGravity(Gravity.RIGHT);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayout.removeView(materialCardView2);
                    }

                });

                relativeLayout.addView(button);
                materialCardView2.addView(relativeLayout);
                linearLayout.addView(materialCardView2);
                viewCounter+=1;
            }
        });
    }

}
