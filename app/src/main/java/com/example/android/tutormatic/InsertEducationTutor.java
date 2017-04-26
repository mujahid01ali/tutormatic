package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
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

public class InsertEducationTutor extends AppCompatActivity implements View.OnClickListener {
    EditText txtCourse, txtCollege, txtStartYear, txtEndYear, txtStream;
    Button btnSaveEdu;
    String url = Config.baseUrl2+"TM_Script/addTutorEducation.php";
    ProgressDialog dialog;
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
        setContentView(R.layout.activity_insert_education_tutor);
        txtCourse = (EditText) findViewById(R.id.etCourse);
        txtCollege = (EditText) findViewById(R.id.etCollege);
        txtStartYear = (EditText) findViewById(R.id.etStartYear);
        txtEndYear = (EditText) findViewById(R.id.etEndYear);
        txtStream = (EditText) findViewById(R.id.etStream);
        txtCourse.setFilters(new InputFilter[]{filter});
        txtCollege.setFilters(new InputFilter[]{filter});
        txtStartYear.setFilters(new InputFilter[]{filter});
        txtEndYear.setFilters(new InputFilter[]{filter});
        txtStream.setFilters(new InputFilter[]{filter});
        btnSaveEdu = (Button) findViewById(R.id.eduSave);

        btnSaveEdu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        dialog=ProgressDialog.show(this,"","Please Wait",false,false);
        StringRequest stringRequestEdu = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.toString().contains("success")){
                    //Log.d("Education",response.toString());
                    Toast.makeText(getApplicationContext()," Successfully Added",Toast.LENGTH_SHORT).show();
                    Intent intEdu=new Intent(InsertEducationTutor.this,TutorProfile.class);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("txtEmail",SharedPrefManager.getInstance(getApplicationContext()).getUserName().toString());
                param.put("txtCourse", txtCourse.getText().toString());
                param.put("txtStartYear", txtStartYear.getText().toString());
                param.put("txtEndYear", txtEndYear.getText().toString());
                param.put("txtCollege", txtCollege.getText().toString());
                param.put("txtStream",txtStream.getText().toString());
                return param;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequestEdu);
    }
}
