package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class StudentAddSubject extends AppCompatActivity implements View.OnClickListener {
    EditText txtSubject;
    Button btnSave;
    ProgressDialog dialog;
    String urlUpdate;
    private String blockCharacterSet = "~#={}()^,|$%&*!";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_subject);
        txtSubject=(EditText) findViewById(R.id.etStuSubject);
        txtSubject.setFilters(new InputFilter[]{filter});
        btnSave=(Button) findViewById(R.id.btnStuEduInfoSave);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (txtSubject.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"Please Fill All Entries",Toast.LENGTH_SHORT).show();
        }else {
            if (txtSubject.getText().toString().contains(",") || txtSubject.getText().toString().contains("=") || txtSubject.getText().toString().contains("{")
                    || txtSubject.getText().toString().contains("}") || txtSubject.getText().toString().contains("(") ||
                    txtSubject.getText().toString().contains(")")) {
                Toast.makeText(this, "'{},()' not allowed", Toast.LENGTH_SHORT).show();
            } else {
                urlUpdate = Config.baseUrl2 + "TM_Script/InsertStudentSubject.php";
                dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.e("Aligarh", response);
                        if (response.toString().contains("success")) {
                            Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(StudentAddSubject.this, StudentProfile.class);
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
                        param.put("txtEmail", SharedPrefManager.getInstance(getApplicationContext()).getUserName().toString());
                        param.put("txtSubject", txtSubject.getText().toString());
                        return param;
                    }
                };
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        }
    }
}
