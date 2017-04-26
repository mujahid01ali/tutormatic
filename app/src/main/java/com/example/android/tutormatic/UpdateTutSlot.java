package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class UpdateTutSlot extends AppCompatActivity implements View.OnClickListener {
    EditText timeUpdate,timeUpdate2;
    Button slotTutUpdateSave;
    Spinner slotSpinner;
    String slotStart,slotEnd,slot_id;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tut_slot);
        timeUpdate=(EditText) findViewById(R.id.timeUpdate);
        timeUpdate2=(EditText) findViewById(R.id.timeUpadte2);
        slotSpinner=(Spinner) findViewById(R.id.slotSpinner);
        slotTutUpdateSave=(Button)findViewById(R.id.slotTutUpdateSave);
        Bundle extra = getIntent().getExtras();
        slotStart = extra.getString("slotStart");
        slotEnd = extra.getString("slotEnd");
        slot_id=extra.getString("slot_id");
        timeUpdate.setOnClickListener(this);
        timeUpdate2.setOnClickListener(this);
        slotTutUpdateSave.setOnClickListener(this);
        timeUpdate.setText(slotStart);
        timeUpdate2.setText(slotEnd);


    }

    @Override
    public void onClick(View v) {
        if (v==timeUpdate){
            getTime1();
        }
        if (v==timeUpdate2){
            getTime2();
        }
        if (v==slotTutUpdateSave){
            saveSlot();

        }
    }


    public void getTime1(){
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker1;
        mTimePicker1 = new TimePickerDialog(UpdateTutSlot.this, new TimePickerDialog.OnTimeSetListener() {
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
                timeUpdate.setText(fotmattedTime);
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
        mTimePicker = new TimePickerDialog(UpdateTutSlot.this, new TimePickerDialog.OnTimeSetListener() {
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
                timeUpdate2.setText(fotmattedTime);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void saveSlot(){
        if (slotSpinner.getSelectedItem().toString().contains("Choose") || timeUpdate.getText().toString().equals("") ||timeUpdate2.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please fill all Entries",Toast.LENGTH_LONG).show();
        }
        else{
            String urlUpdate = Config.baseUrl2 + "TM_Script/updateTutSlot.php";
            dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Log.e("Aligarh", response);
                    if (response.toString().contains("success")) {
                        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(UpdateTutSlot.this, TutorProfile.class);
                        startActivity(in);
                        finish();
                    } else if (response.toString().contains("failure")) {
                        Toast.makeText(getApplicationContext(), "Please try Again", Toast.LENGTH_LONG).show();
                    } else if (response.toString().contains("failed")) {
                        Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("txtSlotId", slot_id);
                    param.put("txtStartTime", timeUpdate.getText().toString());
                    param.put("txtEndTime", timeUpdate2.getText().toString());
                    param.put("txtStatus", slotSpinner.getSelectedItem().toString());
                    return param;
                }
            };
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }

    }


}
