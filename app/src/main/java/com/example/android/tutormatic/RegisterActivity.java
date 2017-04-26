package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public RegisterActivity() {
    }

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btnRegister;
    EditText txtEmail, txtPassword, txtRePassword;
    String password, rePassword, emailId;
    String userType;
    ProgressDialog dialog;
    String url = Config.baseUrl2 + "TM_Script/userRegistration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtEmail = (EditText) findViewById(R.id.txtEmail);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        txtPassword = (EditText) findViewById(R.id.txtPass);
        txtRePassword = (EditText) findViewById(R.id.txtRePass);
        //Toast.makeText(RegisterActivity.this,radioButton.getText().toString(),Toast.LENGTH_SHORT).show();
        // emailId = txtEmail.getText().toString().trim();
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            //radioButton.setError(null);
            Toast.makeText(RegisterActivity.this, "select", Toast.LENGTH_SHORT).show();
        } else {
            userType = radioButton.getText().toString();
            //Toast.makeText(RegisterActivity.this,userType,Toast.LENGTH_SHORT).show();

        }

        if (!isEmailValid(txtEmail.getText().toString())) {
            txtEmail.setError("Invalid Email");
        } else {
            //Toast.makeText(getApplicationContext(),"Valid Email",Toast.LENGTH_SHORT).show();
            emailId = txtEmail.getText().toString();
        }
        if (!isValidPassword(txtPassword.getText().toString())) {
            txtPassword.setError("Invalid Password");
            password = "";
        } else {
            // Toast.makeText(getApplicationContext(),"Valid Password",Toast.LENGTH_SHORT).show();
            password = txtPassword.getText().toString();
        }
        if (!isValidPassword(txtRePassword.getText().toString())) {
            txtRePassword.setError("Invalid Password");
            rePassword = "";
        } else {
            //Toast.makeText(getApplicationContext(),"Valid Password",Toast.LENGTH_SHORT).show();
            rePassword = txtRePassword.getText().toString();
            if (password.compareTo(rePassword) == 0) {
                //Toast.makeText(getApplicationContext(),"Match Password",Toast.LENGTH_SHORT).show();

                dialog=ProgressDialog.show(this,"","Please Wait",false,false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.e("Aligarh", response);
                        if (response.toString().contains("success")) {
                            Toast.makeText(getApplicationContext(), "Please Check your registered email for account activation", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(in);
                            finish();
                        } else if (response.toString().contains("already")) {
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Try Again later", Toast.LENGTH_LONG).show();
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
                        param.put("txtUserType", userType.trim());
                        param.put("txtEmail", emailId.toLowerCase().trim());
                        param.put("txtPassword", password.toLowerCase().trim());
                        return param;
                    }
                };
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            } else {
                txtRePassword.setError("Password Not Matched");
            }
        }


    }

    public boolean isEmailValid(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 3) {
            return true;
        } else {
            return false;
        }
    }
}
