package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class StudentSlotAction extends AppCompatActivity implements View.OnClickListener {
    Button btnUpdate,btnDelete;
    TextView value;
    String startTime,endTime,id;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_slot_action);
        btnUpdate=(Button) findViewById(R.id.btnUpdate);
        btnDelete=(Button) findViewById(R.id.btnDelete);
        value=(TextView) findViewById(R.id.value);

        Bundle extra = getIntent().getExtras();
        id=extra.getString("slot_id");
        startTime=extra.getString("slotStart");
        endTime=extra.getString("slotEnd");
        value.setText(startTime+" "+endTime+" "+id);

        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==btnUpdate){
            Intent intentSlot = new Intent(getApplicationContext(),UpdateStudentSlot.class);
            intentSlot.putExtra("slotStart", startTime);
            intentSlot.putExtra("slotEnd",endTime);
            intentSlot.putExtra("slot_id",id);
            //intentEdu.putExtra("data",send);
            startActivity(intentSlot);

        }
        if (v==btnDelete){
            subDelete();
        }
    }

    private void subDelete() {



        String urlUpdate = Config.baseUrl2 + "TM_Script/StudentSlotDelete.php";
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("Aligarh", response);
                if (response.toString().contains("success")) {
                    Toast.makeText(getApplicationContext(), "Successfully Deleted", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(StudentSlotAction.this, StudentProfile.class);
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
                param.put("slotId", id);
                return param;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


}
