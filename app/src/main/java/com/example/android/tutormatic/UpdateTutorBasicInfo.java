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

public class UpdateTutorBasicInfo extends AppCompatActivity implements View.OnClickListener {
    EditText etTutFName,etTutLName, etTutMobile,etTutAddress,etTutLandmark,etTutCity;
    Button btnTutBasicSave;
    Spinner spinnerState;
    ProgressDialog dialog;
    public double lat;
   public double lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tutor_basic_info);
        etTutFName=(EditText)findViewById(R.id.etTutFName);
        etTutLName=(EditText)findViewById(R.id.etTutLName);
        etTutMobile=(EditText) findViewById(R.id.etTutMobile);
        etTutAddress=(EditText)findViewById(R.id.etTutAddress);
        etTutAddress=(EditText)findViewById(R.id.etTutAddress);
        etTutLandmark=(EditText)findViewById(R.id.etTutLandmark);
        spinnerState=(Spinner) findViewById(R.id.spinnerState);
        etTutCity=(EditText)findViewById(R.id.etTutCity);
        btnTutBasicSave=(Button)findViewById(R.id.btnTutBasInfoSave);
        getTutBasicInfo();
        btnTutBasicSave.setOnClickListener(this);
    }


    private void convert() {
        String loc= etTutLandmark.getText().toString().trim().replace(" ","")+""+etTutCity.getText().toString().trim().replace(" ","");
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
                    saveData();
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




    private void getTutBasicInfo() {
       String url1 = Config.baseUrl2 + "TM_Script/getTutorData.php?email=" + SharedPrefManager.getInstance(getApplicationContext()).getUserName();
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);

        StringRequest srTutBasic = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                String mobile = "";
                String fname="";
                String lname="";
                String address = "";
                String city="";
                String state="";
                String landmark="";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject basicInfo = jsonArray.getJSONObject(0);
                    fname=basicInfo.getString("fname");
                    lname=basicInfo.getString("lname");
                    mobile = basicInfo.getString("mobile");
                    address = basicInfo.getString("address");
                    city=basicInfo.getString("city");
                    state=basicInfo.getString("state");
                    landmark=basicInfo.getString("landmark");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                etTutFName.setText(fname);
                etTutLName.setText(lname);
                etTutMobile.setText(mobile);
                etTutAddress.setText(address);
                etTutLandmark.setText(landmark);
                etTutCity.setText(city);

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
        convert();
    }

    private void saveData() {
        if (spinnerState.getSelectedItem().toString().equals("Choose")) {
            Toast.makeText(getApplicationContext(), "Please Select State", Toast.LENGTH_SHORT).show();
        } else {


            Log.e("Latitude", lat + "");
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
                    param.put("txtEmail", SharedPrefManager.getInstance(getApplicationContext()).getUserName().toString());
                    param.put("txtFName", etTutFName.getText().toString());
                    param.put("txtLName", etTutLName.getText().toString());
                    param.put("txtMobile", etTutMobile.getText().toString());
                    param.put("txtAddress", etTutAddress.getText().toString());
                    param.put("txtCity", etTutCity.getText().toString());
                    param.put("txtState", spinnerState.getSelectedItem().toString());
                    param.put("txtLandmark", etTutLandmark.getText().toString());
                    param.put("lat", lat + "");
                    param.put("lon", lon + "");
                    return param;
                }
            };
            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }
    }



}
