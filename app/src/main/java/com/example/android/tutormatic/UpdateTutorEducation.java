package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class UpdateTutorEducation extends AppCompatActivity {
    EditText etTutcourse,etTutCollege,etTutStartYear,etTutEndYear,etTutStream;
    Button btnTutEduSave;
    String edu_id;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tutor_education);
        etTutcourse=(EditText) findViewById(R.id.etTutCourse);
        etTutCollege=(EditText) findViewById(R.id.etTutCollege);
        etTutStartYear=(EditText) findViewById(R.id.etTutStartYear);
        etTutEndYear=(EditText) findViewById(R.id.etTutEndYear);
        etTutStream=(EditText) findViewById(R.id.etTutStream);
        getTutorEducation();
        Bundle extra=getIntent().getExtras();
        String id=extra.getString("id");
        etTutcourse.setText(id);
        //btnTutEduSave.setOnClickListener(this);
    }

    private void getTutorEducation() {
/*
        String urlGetDetail="";
        dialog=ProgressDialog.show(this,"","Please Wait",false,false);
        StringRequest srEduTutUpdate = new StringRequest(Request.Method.GET, urlGetDetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                String course = "";
                String college = "";
                String startYear = "";
                String endYear = "";
                String stream = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("resultSkillUpdate");
                    JSONObject basicInfo = jsonArray.getJSONObject(0);
                    course = basicInfo.getString("skill");
                    college = basicInfo.getString("Exp");
                    startYear = basicInfo.getString("Des");
                    endYear = basicInfo.getString("Des");
                    stream = basicInfo.getString("Des");
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
        */

    }


}
