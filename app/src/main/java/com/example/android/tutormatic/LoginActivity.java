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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtUserName, txtPassword;
    TextView txtRegister,txtForgot;
    Button btnLogin;
    Spinner spinnerLogin;
    ProgressDialog dialog;
    String url = Config.baseUrl2 + "TM_Script/loginActivityTutorMatic.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tutor Matic");
        txtUserName = (EditText) findViewById(R.id.txtUser);
        txtPassword = (EditText) findViewById(R.id.txtPass);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtForgot=(TextView)findViewById(R.id.txtForget);
        spinnerLogin = (Spinner) findViewById(R.id.spinnerLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        txtForgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            userLogin();
        }
        if (v == txtRegister) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        if (v==txtForgot){
            Intent intent = new Intent(LoginActivity.this, OtpAuth.class);
            startActivity(intent);
        }
    }

    public void userLogin() {
        if (spinnerLogin.getSelectedItem().toString().contains("Choose Any One")) {
            Toast.makeText(getApplicationContext(), "Please Choose Any One", Toast.LENGTH_SHORT).show();
        } else {
            dialog = ProgressDialog.show(this, "", "Please wait...", false, false);
            StringRequest loginRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("student")) {
                        dialog.dismiss();
                        SharedPrefManager.getInstance(getApplicationContext()).login(txtUserName.getText().toString(), response.toString());
                        Intent intent = new Intent(LoginActivity.this, StudentProfile.class);
                        startActivity(intent);
                        finish();
                    } else if (response.contains("tutor")) {
                        dialog.dismiss();
                        SharedPrefManager.getInstance(getApplicationContext()).login(txtUserName.getText().toString(), response.toString());
                        Intent intent = new Intent(LoginActivity.this, TutorProfile.class);
                        startActivity(intent);
                        finish();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Enter Correct User Name and Password", Toast.LENGTH_SHORT).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("txtUsername", txtUserName.getText().toString().toLowerCase().trim());
                    param.put("txtPassword", txtPassword.getText().toString().toLowerCase().trim());
                    param.put("txtSelection", spinnerLogin.getSelectedItem().toString());
                    return param;
                }
            };
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
        }
    }

}
