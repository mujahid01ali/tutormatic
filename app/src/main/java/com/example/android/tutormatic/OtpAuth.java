package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OtpAuth extends AppCompatActivity implements View.OnClickListener {
    EditText etEmail;
    Spinner spinner;
    Button btnSend;
    ProgressDialog dialog;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Forgot Password");
        etEmail=(EditText)findViewById(R.id.etEmail);
        spinner=(Spinner)findViewById(R.id.spinnerType);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String url = Config.baseUrl3 + "TM_Script/otpAuthn.php";

        if (etEmail.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Registered Email", Toast.LENGTH_SHORT).show();
        } else {
            dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
            StringRequest otpRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("success")) {
                        dialog.dismiss();

                            Intent in=new Intent(OtpAuth.this,OtpRecieveCheck.class);
                        in.putExtra("email",etEmail.getText().toString());
                            startActivity(in);
                        finish();


                        //Toast.makeText(OtpAuth.this, "Your otp", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("fail")) {
                        dialog.dismiss();
                        Toast.makeText(OtpAuth.this, "Sorry! You are not registered user", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(OtpAuth.this, "Check Your Internet connection", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("txtEmail",etEmail.getText().toString());
                    param.put("txtType", spinner.getSelectedItem().toString().trim());
                    return param;
                }
            };
            otpRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(otpRequest);
        }

    }

}
