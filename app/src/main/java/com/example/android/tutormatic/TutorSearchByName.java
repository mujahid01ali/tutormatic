package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class TutorSearchByName extends AppCompatActivity implements View.OnClickListener {
    EditText inputSearchWild;
    ListView list_view_tutor;
    TextView found;
    Button btnSearch;
    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> tutorList;
    ProgressDialog dialog;
    public ListAdapter listAdaptrTutor;
    int size2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_search_by_name);
        setTitle("Search Tutor By Name");
        inputSearchWild=(EditText) findViewById(R.id.inputSearchWild);
        list_view_tutor=(ListView) findViewById(R.id.list_view_tutor);
        found=(TextView)findViewById(R.id.found);
        btnSearch=(Button) findViewById(R.id.btnSearch);
        tutorList = new ArrayList<HashMap<String, String>>();
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getAllTutorList();

    }





    private void getAllTutorList() {
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
        String url3=Config.baseUrl2+"TM_Script/getTutorList.php?value="+inputSearchWild.getText().toString();
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Slot", response.toString());
                tutorList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray courseArray = jsonObject.getJSONArray("resultName");
                    size2 = courseArray.length();
                    //listViewSizeSlot(size2);
                    for (int j = 0; j < size2; j++) {
                        JSONObject a = courseArray.getJSONObject(j);
                        String first_name=a.getString("fname");
                        String last_name=a.getString("lname");
                        String email=a.getString("email");
                        String landmark=a.getString("landmark");
                        HashMap<String, String> slot = new HashMap<String, String>();
                        slot.put("name",first_name+" "+last_name);
                        slot.put("email",email);
                        slot.put("landmark",landmark);
                        tutorList.add(slot);
                    }
                    //inputSearch.setText(tutorList.toString());
                    // tutorList = new ArrayList<HashMap<String, String>>(tutorList);
                    listAdaptrTutor = new SimpleAdapter(getApplicationContext(), tutorList, R.layout.list_tutor,
                            new String[]{"name","email","landmark"}, new int[]{R.id.tutor_name,R.id.tutor_email,R.id.tutor_landmark});
                    list_view_tutor.setAdapter(listAdaptrTutor);
                    found.setText(size2+"  Records Found");

                    list_view_tutor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String send = list_view_tutor.getItemAtPosition(position).toString();
                            String[] arr = send.split("[{=,()}]");
                            Intent intentSlot = new Intent(getApplicationContext(), TutorProfileAfterSearch.class);
                            intentSlot.putExtra("email", arr[2]);
                            startActivity(intentSlot);

                        }
                    });


                    // setListViewHeightBasedOnChildren(listViewSlot);
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
                           /* String[] arr = send.split("[{=,()}]");
                            Intent intentSlot = new Intent(getContext(), UpdateTutSlot.class);
                            intentSlot.putExtra("slotStart", arr[8]);
                            intentSlot.putExtra("slotEnd",arr[4]);
                            intentSlot.putExtra("slot_id",arr[11]);
                            intentSlot.putExtra("data",send);
                            startActivity(intentSlot);*/
                Toast.makeText(getApplicationContext(),send,Toast.LENGTH_SHORT).show();
            }
        });

    }


}
