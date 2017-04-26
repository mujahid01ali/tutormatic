package com.example.android.tutormatic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
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

public class SelectActionTutorList extends AppCompatActivity implements View.OnClickListener {
    TextView txtSlot,txtName,textWelMes;
    Button btnSelect;
    String start,end,email,name;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action_tutor_list);
        txtSlot=(TextView) findViewById(R.id.txtSlot);
        txtName=(TextView) findViewById(R.id.txtName);
        textWelMes=(TextView)findViewById(R.id.txtWelMessage);
        btnSelect=(Button)findViewById(R.id.btnSelect);
        Bundle select=getIntent().getExtras();
        start=select.getString("start");
        end=select.getString("end");
        name=select.getString("name");
        email=select.getString("email");

        txtSlot.setText(start+" , "+end);
        txtName.setText(name);
        btnSelect.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.amu);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");
        Intent notificationIntent = new Intent(this, SelectActionTutorList.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());


        sendToStudentProfile();



        btnSelect.setBackgroundColor(Color.rgb(209,224,224));
        btnSelect.setClickable(false);
        btnSelect.setEnabled(false);
        btnSelect.setText("Email send to your Registered email Id");
        textWelMes.setText("Please Check your Email Inbox For More Information");
        //Toast.makeText(this,"Message Will be send to you",Toast.LENGTH_SHORT).show();
    }

    private void sendToStudentProfile() {

        String urlUpdate = Config.baseUrl2 + "TM_Script/UpdateStudentAfterChooseTutor.php";
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("Aligarh", response);
                if (response.toString().contains("success")) {
                    Toast.makeText(getApplicationContext(), "Successfully Selected", Toast.LENGTH_LONG).show();
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
                param.put("txtEmail",SharedPrefManager.getInstance(getApplicationContext()).getUserName().toString());
                param.put("email",email);
                param.put("name",name);
                return param;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
