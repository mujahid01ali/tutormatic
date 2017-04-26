package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class StudentProfileAfterSearch extends AppCompatActivity implements View.OnClickListener {
    TextView txtName, txtEmail, txtMobile, txtAddress, txtQualification;
    public String urlBasic, qualification,email,name;
    public ListView listViewSubject,listViewSlot;
    public ListAdapter listAdapterSubject,listAdapterSlot;
    public ArrayList<HashMap<String, String>> subjectArrayList,slotArrayList;
    public ProgressDialog dialog;
    public static int size2,size3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_after_search);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtQualification = (TextView) findViewById(R.id.txtEducation);
        subjectArrayList = new ArrayList<HashMap<String, String>>();
        slotArrayList = new ArrayList<HashMap<String, String>>();
        listViewSubject = (ListView) findViewById(R.id.listViewSubjects);
        listViewSlot=(ListView) findViewById(R.id.listStuSlot);
        Bundle extra = getIntent().getExtras();
        email=extra.getString("email");
        getStudentBasicInfo();
        getStudentSubject();
        getStudentSlot();
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
        txtEmail.setText(email);


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void getStudentSubject() {
        String url3 = Config.baseUrl2 + "TM_Script/getStudentSubjects.php?email=" + email;

        StringRequest stringRequestSubject = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Subjects", response.toString());
                subjectArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray courseArray = jsonObject.getJSONArray("resultSubject");
                    size2 = courseArray.length();
                    //listViewSizeSlot(size2);
                    for (int j = 0; j < size2; j++) {
                        JSONObject a = courseArray.getJSONObject(j);
                        String sub_id = a.getString("sub_id");
                        String subjectName = a.getString("subject");
                        HashMap<String, String> subject = new HashMap<String, String>();
                        subject.put("sub_id", sub_id);
                        subject.put("subjectName", subjectName);
                        subjectArrayList.add(subject);
                    }
                    listAdapterSubject = new SimpleAdapter(getApplicationContext(), subjectArrayList, R.layout.list_stu_subject,
                            new String[]{"sub_id", "subjectName"}, new int[]{R.id.txtStuSubId,
                            R.id.txtSubject});
                    listViewSubject.setAdapter(listAdapterSubject);
                    setListViewHeightBasedOnChildren(listViewSubject);
                    listViewSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Toast.makeText(getApplicationContext(),"Scroll Down for slots",Toast.LENGTH_SHORT).show();
                        }
                    });
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
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequestSubject);
    }


    private void getStudentBasicInfo() {
        urlBasic = Config.baseUrl2 +"TM_Script/getStudentProfile.php?email="+email;
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
         name = "";
        String address = "";
        String mobile = "";
        qualification = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("resultStudentBasic");
            JSONObject basicInfo = jsonArray.getJSONObject(0);
            name = basicInfo.getString("fname") + " " + basicInfo.getString("lname");
            mobile = basicInfo.getString("mobile");
            address = basicInfo.getString("address") + " " + basicInfo.getString("city") + " " + basicInfo.getString("state") + " " + basicInfo.getString("country");
            qualification = basicInfo.getString("qualification");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        txtName.setText(name);
        txtMobile.setText(mobile);
        txtAddress.setText(address);
        txtQualification.setText(qualification);
    }




    private void getStudentSlot() {
        String urlSlot=Config.baseUrl2+"TM_Script/getStudentSlot.php?email="+email;
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, urlSlot, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Slot", response.toString());
                slotArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray courseArray = jsonObject.getJSONArray("result2");
                    size2 = courseArray.length();
                    //listViewSizeSlot(size2);
                    for (int j = 0; j < size2; j++) {
                        JSONObject a = courseArray.getJSONObject(j);
                        String slot_id=a.getString("slot_id");
                        String startTime = a.getString("startTime");
                        String lastTime = a.getString("lastTime");
                        String status = a.getString("status");
                        HashMap<String, String> slot = new HashMap<String, String>();
                        slot.put("slot_id",slot_id);
                        slot.put("startTime", "(" + startTime + " , ");
                        slot.put("lastTime", lastTime + ")");
                        slot.put("status", status);
                        slotArrayList.add(slot);
                    }

                    listAdapterSlot = new SimpleAdapter(getApplicationContext(), slotArrayList, R.layout.list_stu_slot,
                            new String[]{"startTime", "lastTime", "status","slot_id"}, new int[]{R.id.txtStart,
                            R.id.txtLast, R.id.txtStatus,R.id.txtStuSlotId});
                    listViewSlot.setAdapter(listAdapterSlot);
                    setListViewHeightBasedOnChildren(listViewSlot);
                    listViewSlot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String send = listViewSlot.getItemAtPosition(position).toString();
                            String[] arr = send.split("[{=,()}]");
                            if (arr[2].equals("Active")) {
                                Intent intentSlot = new Intent(getApplicationContext(), SelectStudentByTutor.class);
                                intentSlot.putExtra("name",name);
                                intentSlot.putExtra("email",email);
                                intentSlot.putExtra("start", arr[8]);
                                intentSlot.putExtra("end", arr[4]);
                                startActivity(intentSlot);
                            }else {
                                Toast.makeText(getApplicationContext(),"Sorry!\n"+name+" is not available in this time slot", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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

    }

    @Override
    public void onClick(View v) {

    }



}
