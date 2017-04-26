package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OtpRecieveCheck extends AppCompatActivity {
    String email;
    EditText etOtp;
    Button btnSubmit;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_recieve_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etOtp=(EditText)findViewById(R.id.etOtp);
        btnSubmit=(Button) findViewById(R.id.btnSubmit);
        Bundle extra = getIntent().getExtras();
         email= extra.getString("email");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Config.baseUrl3 + "TM_Script/otpCheck.php";

                if (etOtp.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(OtpRecieveCheck.this, "", "Please wait...", false, false);
                    StringRequest otpRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("success")) {
                                dialog.dismiss();

                                Intent in=new Intent(OtpRecieveCheck.this,ChangeForgotPassword.class);
                                 in.putExtra("email",email);
                                startActivity(in);
                                finish();


                                //Toast.makeText(OtpAuth.this, "Your otp", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("fail")) {
                                dialog.dismiss();
                                Toast.makeText(OtpRecieveCheck.this, "Sorry! You have Enter wrong OTP", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            Toast.makeText(OtpRecieveCheck.this, "Check Your Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<>();
                            param.put("txtEmail",email);
                            param.put("otp", etOtp.getText().toString());
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
        });
    }

}
