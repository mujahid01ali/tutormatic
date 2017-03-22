package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InserttutorSlot extends AppCompatActivity implements View.OnClickListener {
    EditText time,time2;
    Button btnSlotSave;
    public String url=Config.baseUrl2+"TM_Script/inserttutorSlot.php";
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserttutor_slot);
        //  initiate the edit text
        time = (EditText) findViewById(R.id.time);
        time2=(EditText)findViewById(R.id.time2);
        btnSlotSave=(Button)findViewById(R.id.slotTutorSave);

        // perform click event listener on edit text
        time.setOnClickListener(this);
        time2.setOnClickListener(this);
        btnSlotSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==time){
            getTime1();
        }
        if (v==time2){
            getTime2();
        }
        if (v==btnSlotSave){
            saveSlot();
        }
    }

    public void getTime1(){
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker1;
        mTimePicker1 = new TimePickerDialog(InserttutorSlot.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time1 = selectedHour + ":" + selectedMinute;

                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = fmt.parse(time1 );
                } catch (ParseException e) {

                    e.printStackTrace();
                }
                SimpleDateFormat frmt=new SimpleDateFormat("h.mm a");
                String fotmattedTime=frmt.format(date);
                time.setText(fotmattedTime);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker1.setTitle("Select Time");
        mTimePicker1.show();
    }

    public void getTime2(){

        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(InserttutorSlot.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time1 = selectedHour + ":" + selectedMinute;

                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = fmt.parse(time1 );
                } catch (ParseException e) {

                    e.printStackTrace();
                }
                SimpleDateFormat frmt=new SimpleDateFormat("h.mm a");
                String fotmattedTime=frmt.format(date);
                time2.setText(fotmattedTime);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void saveSlot(){
        dialog= ProgressDialog.show(this,"","Please Wait",false,false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.toString().contains("success")){
                    //Log.d("Education",response.toString());
                    Toast.makeText(getApplicationContext(),"Data Insert Successfully",Toast.LENGTH_SHORT).show();
                    Intent intEdu=new Intent(InserttutorSlot.this,TutorProfile.class);
                    startActivity(intEdu);
                    finish();
                }
                if (response.toString().contains("failure")){
                    //Log.d("Educatiovjhjhgjhgjhn",response.toString());
                    Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("txtEmail",SharedPrefManager.getInstance(getApplicationContext()).getUserName().toString());
                param.put("txtTime1", time.getText().toString());
                param.put("txtTime2", time2.getText().toString());
                return param;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
