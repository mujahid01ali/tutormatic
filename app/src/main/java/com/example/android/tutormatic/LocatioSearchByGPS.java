package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LocatioSearchByGPS extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    ListView list_view_tutor;
    TextView found;
    static float distance;
    ImageButton btn;
    TextView textView;
    public SeekBar seekbar1;
    protected GoogleApiClient googleApiClient;
    protected Location location;
    protected Location tutorLocation;
    ArrayList<HashMap<String, String>> tutorList;
    ProgressDialog dialog;
    public ListAdapter listAdaptrTutor;
    String city;
    int size2;

    public void buildClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locatio_search_by_gps);
        seekbar1=(SeekBar)findViewById(R.id.seekBar1);
        btn=(ImageButton)findViewById(R.id.btn);
        textView=(TextView)findViewById(R.id.seekBarValue);
        list_view_tutor=(ListView) findViewById(R.id.list_view_tutor);
        found=(TextView)findViewById(R.id.found);
        seekbar1.setMax(100);
        seekbar1.setProgress(0);
        tutorList = new ArrayList<HashMap<String, String>>();
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                textView.setText("Radius (In Kilometers): " + progress + "/" + seekBar.getMax());
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    distance = Float.parseFloat(String.valueOf(seekbar1.getProgress()));
                    distance = distance * 1000;
                    getListTutorGPSBased();
               }
        });

        buildClient();
    }



    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        }




        if(location!=null){
             double lat=location.getLatitude();
             double lon=location.getLongitude();
            getCityLine(lat,lon);

        }

    }

    public void getCityLine(double lat, double lon) {

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
       // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public void getListTutorGPSBased() {

        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
        String url3 = Config.baseUrl2 + "TM_Script/getTutorListBasedOnGps.php?value=" +city.replace(" ","").trim() ;
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                int count=0;

                Log.d("tutorList", response.toString());
                tutorList.clear();
                tutorLocation=new Location("tutorLocation");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray courseArray = jsonObject.getJSONArray("resultLocation");
                    size2 = courseArray.length();
                    for (int j = 0; j < size2; j++) {
                        JSONObject a = courseArray.getJSONObject(j);
                        String first_name = a.getString("fname");
                        String last_name = a.getString("lname");
                        String email = a.getString("email");
                        String lat1 = a.getString("lat");
                        String lon1 = a.getString("lon");

                        if (lat1.equals("") && lon1.equals("") ){
                            //Toast.makeText(getApplicationContext(),"No Tutor found at your location",Toast.LENGTH_SHORT).show();
                        }else{
                            tutorLocation.setLatitude(Double.parseDouble(lat1));
                        tutorLocation.setLongitude(Double.parseDouble(lon1));
                        String landmark = a.getString("landmark");
                        String city = a.getString("city");
                        if (tutorLocation.distanceTo(location) < distance) {
                            count = count + 1;
                            HashMap<String, String> slot = new HashMap<String, String>();
                            slot.put("name", first_name + " " + last_name);
                            slot.put("email", email);
                            slot.put("landmark", landmark);
                            slot.put("city", city);
                            tutorList.add(slot);
                        }
                    }
                    }
                    listAdaptrTutor = new SimpleAdapter(getApplicationContext(), tutorList, R.layout.list_tutor_location,
                            new String[]{"city", "name", "email", "landmark"}, new int[]{R.id.tutor_city, R.id.tutor_name, R.id.tutor_email, R.id.tutor_landmark});
                    list_view_tutor.setAdapter(listAdaptrTutor);
                    found.setText(count+ "  Records Found");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest3);

        list_view_tutor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String send = list_view_tutor.getItemAtPosition(position).toString();
                String[] arr = send.split("[{=,()}]");
                Intent intentSlot = new Intent(getApplicationContext(), TutorProfileAfterSearch.class);
                intentSlot.putExtra("email", arr[2]);
                startActivity(intentSlot);
                Toast.makeText(getApplicationContext(), arr[2], Toast.LENGTH_SHORT).show();
            }
        });

    }
}
