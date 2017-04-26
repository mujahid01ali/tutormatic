package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mujahid on 3/8/2017.
 */
public class SearchStudent extends Fragment implements View.OnClickListener {
    EditText txtSearchWild;
    TextView found,txtLocation;
    static final Integer LOCATION = 0x1;
    ImageButton imgSearch;
    ListView listViewResult;
    ProgressDialog dialog;
    ArrayList<HashMap<String, String>> resultList;
    public ListAdapter listAdapterResult;
    int size2;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search Students");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_student, container, false);
        txtSearchWild=(EditText) view.findViewById(R.id.txtSearchWild);
        found=(TextView)view.findViewById(R.id.found);
        txtLocation=(TextView)view.findViewById(R.id.txtLocation);
        imgSearch=(ImageButton)view.findViewById(R.id.imgSearch);
        resultList = new ArrayList<HashMap<String, String>>();
        listViewResult=(ListView)view.findViewById(R.id.listViewSearch);
        //btnSearch.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        txtLocation.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v==imgSearch){
            getSearchResult();
        }
        if (v==txtLocation){
            askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);

        }
    }



    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);


            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Toast.makeText(getActivity(),"Permission granted",Toast.LENGTH_SHORT).show();
                startActivity(intent);


            }
        } else {
            //Toast.makeText(getContext(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            Intent in=new Intent(getContext(),StudentSearchBasedOnGps.class);
            startActivity(in);
        }
    }

    private void getSearchResult() {
        dialog = ProgressDialog.show(getContext(), "", "Please Wait", false, false);
        String url3=Config.baseUrl2+"TM_Script/getResultStudentSearch.php?value="+txtSearchWild.getText().toString();
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("result", response.toString());
                resultList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray resultArray = jsonObject.getJSONArray("resultSearch");
                    size2 = resultArray.length();
                    //listViewSizeSlot(size2);
                    for (int j = 0; j < size2; j++) {
                        JSONObject a = resultArray.getJSONObject(j);
                        String first_name=a.getString("fname");
                        String last_name=a.getString("lname");
                        String email=a.getString("email");
                        String subject=a.getString("class");
                        String landmark=a.getString("landmark");
                        String city=a.getString("city");
                        HashMap<String, String> result = new HashMap<String, String>();
                        result.put("name",first_name+" "+last_name);
                        result.put("email",email);
                        result.put("class",subject);
                        result.put("landmark",landmark);
                        result.put("city",city);
                        resultList.add(result);
                    }
                    //inputSearch.setText(tutorList.toString());
                    // tutorList = new ArrayList<HashMap<String, String>>(tutorList);
                    listAdapterResult = new SimpleAdapter(getContext(), resultList, R.layout.list_result_search_by_tutor,
                            new String[]{"name","email","class","landmark","city"}, new int[]{R.id.student_name,
                            R.id.student_email,R.id.student_class,R.id.student_city,R.id.student_landmark});
                    listViewResult.setAdapter(listAdapterResult);
                    found.setText(size2+"  Records Found");
                    // setListViewHeightBasedOnChildren(listViewSlot);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest3);

      listViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String send = listViewResult.getItemAtPosition(position).toString();
                String[] arr = send.split("[{=,()}]");
                Intent intentSlot = new Intent(getContext(), StudentProfileAfterSearch.class);
                intentSlot.putExtra("email", arr[2]);
                startActivity(intentSlot);
                Toast.makeText(getContext(),arr[2],Toast.LENGTH_SHORT).show();
            }
        });

    }
}
