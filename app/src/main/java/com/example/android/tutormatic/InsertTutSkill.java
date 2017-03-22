package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class InsertTutSkill extends AppCompatActivity implements View.OnClickListener {
    EditText txtSkill, txtSkillExp, txtSkillDes;
    Button btnSkillSave;
    ProgressDialog dialog;
    String url = Config.baseUrl2 + "TM_Script/addTutorSkill.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_tut_skill);
        txtSkill = (EditText) findViewById(R.id.txtSkill);
        txtSkillExp = (EditText) findViewById(R.id.txtSkillExp);
        txtSkillDes = (EditText) findViewById(R.id.txtSkillDes);
        btnSkillSave = (Button) findViewById(R.id.btnSkillSave);
        btnSkillSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSkillSave) {

                insertSkill();

        }
    }

    public void insertSkill() {
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.toString().contains("success")) {
                    Toast.makeText(getApplicationContext(), "You Are SuccessFully save your Skill", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please retry Again", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("txtSkill", txtSkill.getText().toString());
                param.put("txtSkillExp", txtSkillExp.getText().toString());
                param.put("txtSkillDes", txtSkillDes.getText().toString());
                param.put("txtEmail",SharedPrefManager.getInstance(getApplicationContext()).getUserName());
                return param;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
