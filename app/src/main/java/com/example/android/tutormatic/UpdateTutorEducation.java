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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateTutorEducation extends AppCompatActivity implements View.OnClickListener {
    EditText etTutcourse,etTutCollege,etTutStartYear,etTutEndYear,etTutStream;
    Button btnTutEduSave;
    String edu_id;
    String id;
    ProgressDialog dialog;
    private String blockCharacterSet = "~#={}(),^|$%&*!";
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
        setContentView(R.layout.activity_update_tutor_education);
        etTutcourse=(EditText) findViewById(R.id.etTutCourse);
        etTutCollege=(EditText) findViewById(R.id.etTutCollege);
        etTutStartYear=(EditText) findViewById(R.id.etTutStartYear);
        etTutEndYear=(EditText) findViewById(R.id.etTutEndYear);
        etTutStream=(EditText) findViewById(R.id.etTutStream);
        btnTutEduSave=(Button)findViewById(R.id.btnTutEduSave);
        etTutcourse.setFilters(new InputFilter[] { filter });
        etTutCollege.setFilters(new InputFilter[] { filter });
        etTutStartYear.setFilters(new InputFilter[] { filter });
        etTutEndYear.setFilters(new InputFilter[] { filter });
        etTutStream.setFilters(new InputFilter[] { filter });
        Bundle extra=getIntent().getExtras();
        id=extra.getString("idEdu");
        //String data=extra.getString("data");
        getTutorEducation();
        btnTutEduSave.setOnClickListener(this);
    }

    private void getTutorEducation() {

        String urlGetDetail=Config.baseUrl2 + "TM_Script/getTutorEducationForUpdate.php?edu_id=" + id;
        dialog=ProgressDialog.show(this,"","Please Wait",false,false);
        StringRequest srEduTutUpdate = new StringRequest(Request.Method.GET, urlGetDetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                //Log.d("Update",response.toString());
                String course = "";
                String college = "";
                String startYear = "";
                String endYear = "";
                String stream = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("resultEduUpdate");
                    JSONObject basicInfo = jsonArray.getJSONObject(0);
                    course = basicInfo.getString("course");
                    college = basicInfo.getString("college");
                    startYear = basicInfo.getString("startYear");
                    endYear = basicInfo.getString("endYear");
                    stream = basicInfo.getString("stream");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                etTutcourse.setText(course);
                etTutCollege.setText(college);
                etTutStartYear.setText(startYear);
                etTutEndYear.setText(endYear);
                etTutStream.setText(stream);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

            }
        });
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(srEduTutUpdate);


    }


    @Override
    public void onClick(View v) {

        if (etTutcourse.getText().toString().equals("") || etTutCollege.getText().toString().equals("") || etTutStartYear.getText().toString().equals("") || etTutEndYear.getText().toString().equals("")|| etTutStream.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please Fill All Entries", Toast.LENGTH_LONG).show();
        } else {
            String urlUpdate = Config.baseUrl2 + "TM_Script/updateTutorEducation.php";
            dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Log.e("Aligarh", response);
                    if (response.toString().contains("success")) {
                        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(UpdateTutorEducation.this, TutorProfile.class);
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
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("txtEduId", id);
                    param.put("txtCourse", etTutcourse.getText().toString());
                    param.put("txtCollege", etTutCollege.getText().toString());
                    param.put("txtStartYear", etTutStartYear.getText().toString());
                    param.put("txtEndYear", etTutEndYear.getText().toString());
                    param.put("txtStream", etTutStream.getText().toString());
                    return param;
                }
            };
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }

    }
}
