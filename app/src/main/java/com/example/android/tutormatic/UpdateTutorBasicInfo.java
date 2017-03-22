package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UpdateTutorBasicInfo extends AppCompatActivity implements View.OnClickListener {
    EditText etTutMobile,etTutAddress,etTutLandmark,etTutCity,etTutState;
    Button btnTutBasicSave;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tutor_basic_info);
        etTutMobile=(EditText) findViewById(R.id.etTutMobile);
        etTutAddress=(EditText)findViewById(R.id.etTutAddress);
        etTutAddress=(EditText)findViewById(R.id.etTutAddress);
        etTutLandmark=(EditText)findViewById(R.id.etTutLandmark);
        etTutState=(EditText)findViewById(R.id.etTutState);
        etTutCity=(EditText)findViewById(R.id.etTutCity);
        btnTutBasicSave=(Button)findViewById(R.id.btnTutBasInfoSave);
        getTutBasicInfo();
        btnTutBasicSave.setOnClickListener(this);
    }

    private void getTutBasicInfo() {
       String url1 = Config.baseUrl2 + "TM_Script/getTutorData.php?email=" + SharedPrefManager.getInstance(getApplicationContext()).getUserName();
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);

        StringRequest srTutBasic = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                String mobile = "";
                String address = "";
                String city="";
                String state="";
                String landmark="";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject basicInfo = jsonArray.getJSONObject(0);
                    mobile = basicInfo.getString("mobile");
                    address = basicInfo.getString("address");
                    city=basicInfo.getString("city");
                    state=basicInfo.getString("state");
                    landmark=basicInfo.getString("landmark");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                etTutMobile.setText(mobile);
                etTutAddress.setText(address);
                etTutLandmark.setText(landmark);
                etTutCity.setText(city);
                etTutState.setText(state);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

            }
        });
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(srTutBasic);

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
        String urlUpdate = Config.baseUrl2 + "TM_Script/updatetutorBasicInfo.php";
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("Aligarh", response);
                if (response.toString().contains("success")) {
                    Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(UpdateTutorBasicInfo.this, TutorProfile.class);
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
                param.put("txtEmail",SharedPrefManager.getInstance(getApplicationContext()).getUserName().toString());
                param.put("txtMobile", etTutMobile.getText().toString());
                param.put("txtAddress", etTutAddress.getText().toString());
                param.put("txtCity", etTutCity.getText().toString());
                param.put("txtState", etTutState.getText().toString());
                param.put("txtLandmark",etTutLandmark.getText().toString());
                return param;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
