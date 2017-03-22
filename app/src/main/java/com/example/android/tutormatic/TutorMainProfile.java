package com.example.android.tutormatic;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

/**
 * Created by Mujahid on 3/8/2017.
 */
public class TutorMainProfile extends Fragment implements View.OnClickListener {
    ProgressDialog dialog;
    public TextView txtEmail, txtMobile, txtName, txtAddress, txtAddEdu, txtAddSlot, txtAddSkill, txtBasicEdit;
    public ListView listViewEducation, listViewSlot, listViewSkill;
    public ListAdapter listAdapterEdu;
    public ListAdapter listAdaptrSlot;
    public ListAdapter listAdaptrSkill;
    public ArrayList<HashMap<String, String>> educationArrayList;
    public ArrayList<HashMap<String, String>> slotArrayList;
    public ArrayList<HashMap<String, String>> skillArrayList;

    public static int size1, size2, size3;
    String url1;
    String url2;
    String url3;
    String url4;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutor_main_prifile, container, false);
        url1 = Config.baseUrl2 + "TM_Script/getTutorData.php?email=" + SharedPrefManager.getInstance(getContext()).getUserName();
        url2 = Config.baseUrl2 + "TM_Script/getTutorEducation.php?email=" + SharedPrefManager.getInstance(getContext()).getUserName();
        url3 = Config.baseUrl2 + "TM_Script/getTutorSlot.php?email=" + SharedPrefManager.getInstance(getContext()).getUserName();
        url4 = Config.baseUrl2 + "TM_Script/getTutorSkills.php?email=" + SharedPrefManager.getInstance(getContext()).getUserName();
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtMobile = (TextView) view.findViewById(R.id.txtMobile);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtAddEdu = (TextView) view.findViewById(R.id.txtAddEdu);
        txtAddSlot = (TextView) view.findViewById(R.id.txtAddSlot);
        txtAddSkill = (TextView) view.findViewById(R.id.txtAddSkill);
        txtBasicEdit = (TextView) view.findViewById(R.id.txtBasicEdit);
        listViewEducation = (ListView) view.findViewById(R.id.listEdu);
        listViewSlot = (ListView) view.findViewById(R.id.listTutSlot);
        listViewSkill = (ListView) view.findViewById(R.id.listViewSkill);
        educationArrayList = new ArrayList<HashMap<String, String>>();
        slotArrayList = new ArrayList<HashMap<String, String>>();
        skillArrayList = new ArrayList<HashMap<String, String>>();
        dialog = ProgressDialog.show(getContext(), "", "Please Wait", false, false);
        listViewEducation.setNestedScrollingEnabled(true);
        listViewSlot.setNestedScrollingEnabled(true);
        listViewSkill.setNestedScrollingEnabled(true);
        txtAddEdu.setOnClickListener(this);
        txtAddSlot.setOnClickListener(this);
        txtAddSkill.setOnClickListener(this);
        txtBasicEdit.setOnClickListener(this);
        listViewEducation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listViewSlot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listViewSkill.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        getBasicInfo();
        getSlot();
        getSkill();
        getEducation();

        txtEmail.setText(SharedPrefManager.getInstance(getContext()).getUserName().toString());
        return view;
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

    private void getBasicInfo() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
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
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest1);
    }

    private void showResult(String response) {
        String name = "";
        String address = "";
        String mobile = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            JSONObject basicInfo = jsonArray.getJSONObject(0);
            name = basicInfo.getString("fname") + " " + basicInfo.getString("lname");
            mobile = basicInfo.getString("mobile");
            address = basicInfo.getString("address") + " " + basicInfo.getString("city") + " " + basicInfo.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        txtName.setText(name);
        txtMobile.setText(mobile);
        txtAddress.setText(address);
    }

    private void getEducation() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Education", response.toString());
                educationArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray slotArray = jsonObject.getJSONArray("result1");
                    size1 = slotArray.length();
                    //listViewSizeEducation(size1);
                    for (int j = 0; j < size1; j++) {
                        JSONObject a = slotArray.getJSONObject(j);
                        String course = a.getString("course");
                        String from = a.getString("from");
                        String to = a.getString("to");
                        String college = a.getString("college");
                        String stream = a.getString("stream");
                        HashMap<String, String> edu = new HashMap<String, String>();
                        edu.put("course", course);
                        edu.put("from", "(" + from + "-");
                        edu.put("to", to + ")");
                        edu.put("college", college);
                        edu.put("stream", stream);
                        educationArrayList.add(edu);
                    }
                    listAdapterEdu = new SimpleAdapter(getActivity(), educationArrayList, R.layout.list_edu_tutor,
                            new String[]{"course", "from", "to", "college", "stream"}, new int[]{R.id.txtCourse,
                            R.id.txtFrom, R.id.txtTo, R.id.txtCollege, R.id.txtStream});
                    listViewEducation.setAdapter(listAdapterEdu);
                    setListViewHeightBasedOnChildren(listViewEducation);
                    listViewEducation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String send = listViewEducation.getItemAtPosition(position).toString();
                            String[] arr = send.split("[{=,}]");
                            Intent intentEdu = new Intent(getContext(), UpdateTutorEducation.class);
                            intentEdu.putExtra("id", arr[2]);
                           // startActivity(intentEdu);
                            //Toast.makeText(getContext(),"Oho0",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest2);
       /* RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest2);*/

    }


    @Override
    public void onClick(View v) {
        if (v == txtAddEdu) {
            Intent intent = new Intent(getContext(), InsertEducationTutor.class);
            startActivity(intent);
        }
        if (v == txtAddSlot) {
            Intent intent = new Intent(getContext(), InserttutorSlot.class);
            startActivity(intent);
        }
        if (v == txtAddSkill) {
            Intent intent = new Intent(getContext(), InsertTutSkill.class);
            startActivity(intent);
        }
        if (v == txtBasicEdit) {
            Intent intent = new Intent(getContext(), UpdateTutorBasicInfo.class);
            startActivity(intent);
        }

    }

    public void getSlot() {
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
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
                        String startTime = a.getString("startTime");
                        String lastTime = a.getString("lastTime");
                        String status = a.getString("status");
                        HashMap<String, String> slot = new HashMap<String, String>();
                        slot.put("startTime", "(" + startTime + "-");
                        slot.put("lastTime", lastTime + ")");
                        slot.put("status", status);
                        slotArrayList.add(slot);
                    }
                    listAdaptrSlot = new SimpleAdapter(getActivity(), slotArrayList, R.layout.list_tut_slot,
                            new String[]{"startTime", "lastTime", "status"}, new int[]{R.id.txtStart,
                            R.id.txtLast, R.id.txtStatus});
                    listViewSlot.setAdapter(listAdaptrSlot);
                    setListViewHeightBasedOnChildren(listViewSlot);
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
       /* RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest3);*/
    }


    private void showResult3(String response2) {

    }


    public void getSkill() {
        StringRequest stringRequest4 = new StringRequest(Request.Method.GET, url4, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Skill", response.toString());
                skillArrayList.clear();
                try {
                    JSONObject jsonObjectSkill = new JSONObject(response);
                    JSONArray skillArray = jsonObjectSkill.getJSONArray("result3");
                    size3 = skillArray.length();
                    // listViewSizeSkill(size3);
                    for (int j = 0; j < size3; j++) {
                        JSONObject a = skillArray.getJSONObject(j);
                        Integer id = a.getInt("skill_id");
                        String skill = a.getString("skill");
                        String experience = a.getString("experience");
                        String description = a.getString("description");
                        HashMap<String, String> sk = new HashMap<String, String>();
                        sk.put("id", id.toString());
                        sk.put("skill", skill);
                        sk.put("experience", experience);
                        sk.put("description", description);
                        skillArrayList.add(sk);
                    }
                    listAdaptrSkill = new SimpleAdapter(getActivity(), skillArrayList, R.layout.tut_skill_list,
                            new String[]{"skill", "experience", "description", "id"}, new int[]{R.id.txtSkill,
                            R.id.txtExperience, R.id.txtDescription, R.id.txtTutSkillId});

                    listViewSkill.setAdapter(listAdaptrSkill);
                    setListViewHeightBasedOnChildren(listViewSkill);
                    //listViewSkill.setOnGroupExpandListener();
                    listViewSkill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String send = listViewSkill.getItemAtPosition(position).toString();
                            String[] arr = send.split("[{=,}]");
                            Intent intent5 = new Intent(getContext(), UpdateSkillTutor.class);
                            intent5.putExtra("id", arr[2]);
                            startActivity(intent5);
                            //Toast.makeText(getContext(),"Oho0",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest4);
      /*  RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest4);*/
    }





    /*public void listViewSizeEducation(int size){
        ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams)listViewEducation.getLayoutParams();
        listViewParams.height = size*186;
        listViewEducation.requestLayout();
    }
    public void listViewSizeSlot(int size){
        ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams)listViewSlot.getLayoutParams();
        listViewParams.height = size*130;
        listViewSlot.requestLayout();
    }
    public void listViewSizeSkill(int size){
        ViewGroup.LayoutParams listViewParams = (ViewGroup.LayoutParams)listViewSkill.getLayoutParams();
        listViewParams.height = size*186;
        listViewSkill.requestLayout();
    }
*/


}
