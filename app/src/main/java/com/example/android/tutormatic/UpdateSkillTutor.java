package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

public class UpdateSkillTutor extends AppCompatActivity implements View.OnClickListener {
    Button btnSave;
    EditText etSkill, etTutExp, etTutDes;
    String sk_id;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_skill_tutor);
        btnSave = (Button) findViewById(R.id.btnSave);
        etSkill = (EditText) findViewById(R.id.etTutSkill);
        etTutExp = (EditText) findViewById(R.id.etTutExp);
        etTutDes = (EditText) findViewById(R.id.etTutDes);
        Bundle extra = getIntent().getExtras();
        sk_id = extra.getString("id");
        getTutSkill();

        btnSave.setOnClickListener(this);
    }

    private void getTutSkill() {
        String url = Config.baseUrl2 + "TM_Script/getTutorSkillDataForUpdate.php?skill_id=" + sk_id;
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);

        StringRequest srSkillTutUpdate = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                String skill = "";
                String exp = "";
                String des = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("resultSkillUpdate");
                    JSONObject basicInfo = jsonArray.getJSONObject(0);
                    skill = basicInfo.getString("skill");
                    exp = basicInfo.getString("Exp");
                    des = basicInfo.getString("Des");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                etSkill.setText(skill);
                etTutExp.setText(exp);
                etTutDes.setText(des);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

            }
        });
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(srSkillTutUpdate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent backMainTest = new Intent(this, TutorProfile.class);
        startActivity(backMainTest);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (etSkill.getText().toString().equals("") || etTutExp.getText().toString().equals("") || etTutDes.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please Fill All Entries", Toast.LENGTH_LONG).show();
        } else {
            String urlUpdate = Config.baseUrl2 + "TM_Script/updateTutorSkill.php?skill_id=" + sk_id;
            dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Log.e("Aligarh", response);
                    if (response.toString().contains("success")) {
                        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(UpdateSkillTutor.this, TutorProfile.class);
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
                    param.put("txtSkillId", sk_id.toString());
                    param.put("txtSkill", etSkill.getText().toString());
                    param.put("txtExp", etTutExp.getText().toString());
                    param.put("txtDes", etTutDes.getText().toString());
                    return param;
                }
            };
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }
}

