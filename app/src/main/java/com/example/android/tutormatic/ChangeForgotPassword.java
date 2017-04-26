package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ChangeForgotPassword extends AppCompatActivity implements View.OnClickListener {
    EditText etPass1,etPass2;
    Button btnSave;
    String email;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_forgot_password);
        etPass1=(EditText)findViewById(R.id.etPass1);
        etPass2=(EditText)findViewById(R.id.etPass2);
        btnSave=(Button)findViewById(R.id.btnSave);
        Bundle extra = getIntent().getExtras();
        email= extra.getString("email");
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String url = Config.baseUrl3 + "TM_Script/forgetPasswordChanged.php";

        if (etPass1.getText().toString().equals("") || etPass2.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (etPass1.getText().toString().equals(etPass2.getText().toString())){

            dialog = ProgressDialog.show(ChangeForgotPassword.this, "", "Please wait...", false, false);
            StringRequest otpRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("success")) {
                        dialog.dismiss();

                        Intent in=new Intent(ChangeForgotPassword.this,LoginActivity.class);
                        startActivity(in);
                        finish();
                        Toast.makeText(ChangeForgotPassword.this, "Your Password Successfully changed ", Toast.LENGTH_SHORT).show();

                        //Toast.makeText(OtpAuth.this, "Your otp", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("fail")) {
                        dialog.dismiss();
                        Toast.makeText(ChangeForgotPassword.this, "Please try Again", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(ChangeForgotPassword.this, "Check Your Internet connection", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("txtEmail",email);
                    param.put("txtPass", etPass1.getText().toString());
                    return param;
                }
            };
            otpRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(otpRequest);
        }
        else {
            Toast.makeText(ChangeForgotPassword.this, "Both Password must be same", Toast.LENGTH_SHORT).show();
        }
    }
}
