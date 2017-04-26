package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TutorProfileAfterSearch extends AppCompatActivity implements View.OnClickListener {
    public ProgressDialog dialog;
    public TextView txtEmail, txtMobile, txtName, txtAddress;
    public Button btnReview;
    public ListView listViewEducation, listViewSlot, listViewSkill, listViewTutReview;
    public ListAdapter listAdapterEdu;
    public ListAdapter listAdaptrSlot;
    public ListAdapter listAdaptrSkill;
    public ListAdapter listAdapterReview;
    public ArrayList<HashMap<String, String>> educationArrayList;
    public ArrayList<HashMap<String, String>> slotArrayList;
    public ArrayList<HashMap<String, String>> skillArrayList;
    public ArrayList<HashMap<String, String>> reviewArrayList;

    public static String email, name;

    public static int size1, size2, size3;
    String url1;
    String url2;
    String url3;
    String url4;
    String url5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile_after_search);
        setTitle("Tutor Matic");


        Bundle extra = getIntent().getExtras();
        email = extra.getString("email");


        url1 = Config.baseUrl2 + "TM_Script/getTutorData.php?email=" + email;
        url2 = Config.baseUrl2 + "TM_Script/getTutorEducation.php?email=" + email;
        url3 = Config.baseUrl2 + "TM_Script/getTutorSlot.php?email=" + email;
        url4 = Config.baseUrl2 + "TM_Script/getTutorSkills.php?email=" + email;
        url5 = Config.baseUrl2 + "TM_Script/getReview.php?email=" + email;
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtName = (TextView) findViewById(R.id.txtName);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        btnReview = (Button) findViewById(R.id.btnReview);
        listViewEducation = (ListView) findViewById(R.id.listEdu);
        listViewSlot = (ListView) findViewById(R.id.listTutSlot);
        listViewSkill = (ListView) findViewById(R.id.listViewSkill1);
        listViewTutReview = (ListView) findViewById(R.id.listTutReview);
        educationArrayList = new ArrayList<HashMap<String, String>>();
        slotArrayList = new ArrayList<HashMap<String, String>>();
        skillArrayList = new ArrayList<HashMap<String, String>>();
        reviewArrayList = new ArrayList<HashMap<String, String>>();
        dialog = ProgressDialog.show(this, "", "Please Wait", false, false);


        btnReview.setOnClickListener(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listViewEducation.setNestedScrollingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listViewSlot.setNestedScrollingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listViewSkill.setNestedScrollingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listViewTutReview.setNestedScrollingEnabled(true);
        }


        getBasicInfo();
        getSlot();
        getSkill();
        getEducation();
        getReview();

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
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);
    }

    private void showResult(String response) {
        name = "";
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
                    for (int j = 0; j < size1; j++) {
                        JSONObject a = slotArray.getJSONObject(j);
                        String edu_id = a.getString("edu_id");
                        String course = a.getString("course");
                        String from = a.getString("from");
                        String to = a.getString("to");
                        String college = a.getString("college");
                        String stream = a.getString("stream");
                        HashMap<String, String> edu = new HashMap<String, String>();
                        edu.put("edu_id", edu_id);
                        edu.put("course", course);
                        edu.put("from", "(" + from + "-");
                        edu.put("to", to + ")");
                        edu.put("college", college);
                        edu.put("stream", stream);
                        educationArrayList.add(edu);
                    }
                    listAdapterEdu = new SimpleAdapter(getApplicationContext(), educationArrayList, R.layout.list_tut_edu_after_search,
                            new String[]{"course", "from", "to", "college", "stream", "edu_id"}, new int[]{R.id.txtCourse,
                            R.id.txtFrom, R.id.txtTo, R.id.txtCollege, R.id.txtStream, R.id.txtTutEduId});
                    listViewEducation.setAdapter(listAdapterEdu);
                    setListViewHeightBasedOnChildren(listViewEducation);
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
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest2);
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


                    listAdaptrSkill = new SimpleAdapter(TutorProfileAfterSearch.this, skillArrayList, R.layout.list_tut_skill_after_search,
                            new String[]{"skill", "experience", "description", "id"}, new int[]{R.id.txtSkill,
                            R.id.txtExperience, R.id.txtDescription, R.id.txtTutSkillId});

                    listViewSkill.setAdapter(listAdaptrSkill);
                    setListViewHeightBasedOnChildren(listViewSkill);
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
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest4);
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
                    for (int j = 0; j < size2; j++) {
                        JSONObject a = courseArray.getJSONObject(j);
                        String slot_id = a.getString("slot_id");
                        String startTime = a.getString("startTime");
                        String lastTime = a.getString("lastTime");
                        String status = a.getString("status");
                        HashMap<String, String> slot = new HashMap<String, String>();
                        slot.put("slot_id", slot_id);
                        slot.put("startTime", "(" + startTime + " , ");
                        slot.put("lastTime", lastTime + ")");
                        slot.put("status", status);
                        slotArrayList.add(slot);
                    }
                    listAdaptrSlot = new SimpleAdapter(getApplicationContext(), slotArrayList, R.layout.list_tut_slot_afetr_search,
                            new String[]{"startTime", "lastTime", "status", "slot_id"}, new int[]{R.id.txtStart,
                            R.id.txtLast, R.id.txtStatus, R.id.txtTutSlotId});
                    listViewSlot.setAdapter(listAdaptrSlot);
                    setListViewHeightBasedOnChildren(listViewSlot);
                    listViewSlot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String send = listViewSlot.getItemAtPosition(position).toString();
                            String[] arr = send.split("[{=,()}]");
                            if (arr[2].equals("Active")) {
                                Intent intentSlot = new Intent(getApplicationContext(), SelectActionTutorList.class);
                            /*intentSlot.putExtra("slotStart", arr[8]);*/
                                intentSlot.putExtra("start", arr[8]);
                                intentSlot.putExtra("end", arr[4]);
                                intentSlot.putExtra("email", email);
                                intentSlot.putExtra("name", name);
                                startActivity(intentSlot);
                            } else {
                                Toast.makeText(getApplicationContext(), "Sorry!\n " + name + " is not Available in this time slot", Toast.LENGTH_SHORT).show();
                            }
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
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest3);
    }


    private void getReview() {

        StringRequest stringRequest5 = new StringRequest(Request.Method.GET, url5, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Review", response.toString());
                reviewArrayList.clear();
                try {
                    JSONObject jsonObjectSkill = new JSONObject(response);
                    JSONArray reviewArray = jsonObjectSkill.getJSONArray("resultReview");

                    for (int j = 0; j < reviewArray.length(); j++) {
                        JSONObject a = reviewArray.getJSONObject(j);
                        String id = a.getString("review_id");
                        String studentEmail = a.getString("studentEmail");
                        String subject = a.getString("subject");
                        String reviewDate = a.getString("reviewDate");
                        String review = a.getString("review");
                        String rating = a.getString("rating");
                        HashMap<String, String> rv = new HashMap<String, String>();
                        rv.put("id", id.toString());
                        rv.put("studentEmail", studentEmail);
                        rv.put("subject", subject);
                        rv.put("reviewDate", "Date & Time: " + reviewDate);
                        rv.put("review", review);
                        rv.put("rating", "Rating: " + rating);
                        reviewArrayList.add(rv);
                    }
                    listAdapterReview = new SimpleAdapter(getApplicationContext(), reviewArrayList, R.layout.list_tut_review,
                            new String[]{"id", "studentEmail", "subject", "reviewDate", "rating", "review"}, new int[]{R.id.txtReviewId,
                            R.id.studentEmail, R.id.txtSubject, R.id.txtDate, R.id.rating, R.id.txtReview,});

                    listViewTutReview.setAdapter(listAdapterReview);
                    setListViewHeightBasedOnChildren(listViewTutReview);
                    listViewTutReview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int pos = parent.getPositionForView(view);
                            String send = listViewTutReview.getItemAtPosition(position).toString();
                            final String[] arr = send.split("[{=}]");
                            if (arr[4].contains(SharedPrefManager.getInstance(getApplicationContext()).getUserName())) {
                                /*Intent intentSlot = new Intent(getApplicationContext(), SelectActionTutorList.class);
                                intentSlot.putExtra("email", email);
                                intentSlot.putExtra("name", name);
                                startActivity(intentSlot);*/
                               // Toast.makeText(getApplicationContext(),arr[7], Toast.LENGTH_SHORT).show();

                                AlertDialog.Builder builder = new AlertDialog.Builder(TutorProfileAfterSearch.this);
                                // Get the layout inflater
                                LayoutInflater inflater = TutorProfileAfterSearch.this.getLayoutInflater();

                                // Inflate and set the layout for the dialog
                                // Pass null as the parent view because its going in the dialog layout
                                builder.setMessage("Are you sure, You wanted to delete this comment?")
                                        // Add action buttons
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int id) {
                                                //final ProgressDialog dialog1;


                                                String urlUpdate = Config.baseUrl2 + "TM_Script/deleteReview.php";
                                               //dialog1 = ProgressDialog.show(getApplicationContext(), "", "Please Wait", false, false);
                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                       // dialog1.dismiss();
                                                        Log.e("Aligarh", response);
                                                        if (response.toString().contains("success")) {
                                                            Toast.makeText(getApplicationContext(), "Successfully Deleted", Toast.LENGTH_LONG).show();
                                                            Intent in = new Intent(TutorProfileAfterSearch.this, TutorProfileAfterSearch.class);
                                                            in.putExtra("email",email);
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
                                                       // dialog1.dismiss();
                                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                                                    }
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String, String> param = new HashMap<>();
                                                        param.put("reviewId", arr[7]);
                                                        return param;
                                                    }
                                                };
                                                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


                                            }
                                        })
                                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // LoginDialogFragment.this.getDialog().cancel();
                                                //Toast.makeText(TutorProfileAfterSearch.this, "Bhak", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                builder.create();
                                builder.show();


                            } else {
                                Toast.makeText(getApplicationContext(), "Sorry! You can not perform any action", Toast.LENGTH_SHORT).show();
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
        //Request Handler
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest5);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(TutorProfileAfterSearch.this, ReviewAndRatingStudent.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
