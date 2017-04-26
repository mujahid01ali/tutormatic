package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateStudentBasicInfo extends AppCompatActivity implements View.OnClickListener {
    EditText txtFName, txtLName, txtMobile, txtAddress, txtLandmark, txtCity, txtState, txtCountry;
    Spinner spinnerState;
    Button btnSave;
    String urlUpdate;
    double lat,lon;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_basic_info);
        txtFName = (EditText) findViewById(R.id.etStuFName);
        txtLName = (EditText) findViewById(R.id.etStuLName);
        txtMobile = (EditText) findViewById(R.id.etStuMobile);
        txtAddress = (EditText) findViewById(R.id.etStuAddress);
        txtLandmark = (EditText) findViewById(R.id.etStuLandmark);
        txtCity = (EditText) findViewById(R.id.etStuCity);
        spinnerState=(Spinner)findViewById(R.id.spinnerState);
       // txtState = (EditText) findViewById(R.id.etStuState);
        txtCountry = (EditText) findViewById(R.id.etStuCountry);
        btnSave = (Button) findViewById(R.id.btnStuBasInfoSave);

        btnSave.setOnClickListener(this);

        getBasicData();
    }

    private void convert() {
        String loc= txtLandmark.getText().toString().trim().replace(" ","")+""+txtCity.getText().toString().trim().replace(" ","");
        String UrlCity = "http://maps.googleapis.com/maps/api/geocode/json?address="+loc+"";

        JsonObjectRequest stateReq = new JsonObjectRequest(Request.Method.GET, UrlCity, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject location;
                try {
                    // Get JSON Array called "results" and then get the 0th
                    // complete object as JSON
                    location = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    // Get the value of the attribute whose name is
                    // "formatted_string"
                    lon=location.getDouble("lng");
                    lat=location.getDouble("lat");
                    // System.out.println(stateLocation.toString());
                    Log.e("Location" ,lon+""+lat);
                } catch (JSONException e1) {
                    e1.printStackTrace();

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        // add it to the RequestQueue
       RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stateReq);



    }

    private void getBasicData() {
        String urlBasic=Config.baseUrl2+"TM_Script/getStudentProfile.php?email="+SharedPrefManager.getInstance(getApplicationContext()).getUserName().toString();
        dialog=ProgressDialog.show(this,"","Please Wait",false,false);

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, urlBasic, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Aligarh", response);
                showResult(response);
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);
    }


    private void showResult(String response) {
        String fname = "";
        String lname = "";
        String address = "";
        String mobile = "";
        String landmark = "";
        String city = "";
        String state = "";
        String country = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("resultStudentBasic");
            JSONObject basicInfo = jsonArray.getJSONObject(0);
            fname = basicInfo.getString("fname");
            lname= basicInfo.getString("lname");
            mobile = basicInfo.getString("mobile");
            address = basicInfo.getString("address");
            landmark=basicInfo.getString("landmark");
            city=basicInfo.getString("city");
           // state=basicInfo.getString("state");
            country=basicInfo.getString("country");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        txtFName.setText(fname);
        txtLName.setText(lname);
        txtLandmark.setText(landmark);
        txtCity.setText(city);
      //  txtState.setText(state);
        txtCountry.setText(country);
        txtMobile.setText(mobile);
        txtAddress.setText(address);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            saveData();
        }
    }

    private void saveData() {
        convert();
        if (txtFName.getText().toString().equals("") || txtLName.getText().toString().equals("") || txtMobile.getText().toString().equals("")
                || txtAddress.getText().toString().equals("") || txtLandmark.getText().toString().equals("")
                || txtCity.getText().toString().equals("") || spinnerState.getSelectedItem().toString().equals("Choose") || txtCountry.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"Please Fill All Entries",Toast.LENGTH_SHORT).show();
        }else {
            urlUpdate=Config.baseUrl2+"TM_Script/UpdateStudentBasicInfo.php";
            dialog=ProgressDialog.show(this,"","Please Wait",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Log.e("Aligarh", response);
                    if (response.toString().contains("success")) {
                        Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(UpdateStudentBasicInfo.this, StudentProfile.class);
                        startActivity(in);
                        finish();
                    } else if (response.toString().contains("failure")) {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
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
                    param.put("txtFName", txtFName.getText().toString());
                    param.put("txtLName", txtLName.getText().toString());
                    param.put("txtMobile", txtMobile.getText().toString());
                    param.put("txtAddress", txtAddress.getText().toString());
                    param.put("txtLandmark", txtLandmark.getText().toString());
                    param.put("lat",lat+"");
                    param.put("lon",lon+"");
                    param.put("txtCity", txtCity.getText().toString());
                    param.put("txtState", spinnerState.getSelectedItem().toString());
                    param.put("txtCountry", txtCountry.getText().toString());
                    return param;
                }
            };
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }

    }
}
