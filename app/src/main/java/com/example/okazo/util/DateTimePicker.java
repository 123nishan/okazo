package com.example.okazo.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.okazo.R;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



public class DateTimePicker implements DatePickerDialog.OnDateSetListener{
    Activity activity;
    TextView finalTxt;
    private String selectedDate;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private int currentHour;
    private int currentMinu;
    private String amPm;
    private String  preferredTime;
    private String type;

    public void showDatePickerDialog(Activity mActivity, TextView targetTextView,String type){
        activity =  mActivity;
        finalTxt = targetTextView;
        if(type.equals("date")) {


            type = type;
            DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 100);
            datePickerDialog.show();

        }
        else {
            showTimePickerDialog(activity,targetTextView);
        }

    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        GregorianCalendar GregorianCalendar = new GregorianCalendar(year, month, dayOfMonth-1);
        int dayOfWeek=GregorianCalendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekTxt=null;

        if(dayOfWeek==1){
            dayOfWeekTxt = "MON";
        }
        else if(dayOfWeek ==2){
            dayOfWeekTxt = "TUE";
        }
        else if(dayOfWeek ==3){
            dayOfWeekTxt = "WED";
        }
        else if(dayOfWeek ==4){
            dayOfWeekTxt = "THU";
        }
        else if(dayOfWeek ==5){
            dayOfWeekTxt = "FRI";
        }
        else if(dayOfWeek ==6){
            dayOfWeekTxt = "SAT";
        }
        else if(dayOfWeek ==7){
            dayOfWeekTxt = "SUN";
        }
        String dateTxt =dayOfWeekTxt+ " "+ year+"-" + (month+1)+ "-"+dayOfMonth;
        selectedDate = dateTxt;

        finalTxt.setText(selectedDate);
//        if (isTimepicker) {
//            showTimePickerDialog(activity);
//        }
//        else {
//            preferredTime = selectedDate;
//            finalTxt.setText(preferredTime);
//
//        }
    }

    public void showTimePickerDialog(Activity activity,TextView targetTextView){

        timePickerDialog = new TimePickerDialog(activity,android.R.style.Theme_Material_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay , int minutes) {

//                        if (hourOfDay >= 12) {
//                            amPm = "PM";
//                        } else {
//                            amPm = "AM";
//                        }
                        String time = hourOfDay + ":" + minutes;
                        finalTxt.setText(time);
//                        if (hourOfDay < 10 || hourOfDay >17) {
//                            DynamicToast.makeWarning(activity, "Please select time after 10AM and before 5PM", Toast.LENGTH_SHORT).show();
//                           // showTimePickerDialog(activity);
//                        }
//
//                        else{
//                        try {
//                            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
//                            final Date dateObj = sdf.parse(time);
//                            preferredTime =  new SimpleDateFormat("K:mm").format(dateObj) ;
//                            finalTxt.setText(preferredTime);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                     //   }
//                    }
                    }
                },0,0,true);
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();

        }

}
