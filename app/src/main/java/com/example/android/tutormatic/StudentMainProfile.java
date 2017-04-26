package com.example.android.tutormatic;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * Created by Mujahid on 4/6/2017.
 */
public class StudentMainProfile extends Fragment implements View.OnClickListener {
    TextView txtName, txtEmail, txtMobile, txtAddress, txtBasicEdit, txtQualification, txtEduEdit, txtAddSubject,txtAddSlot;
    String urlBasic, qualification;
    public ListView listViewSubject,listViewSlot;
    public ListAdapter listAdapterSubject,listAdapterSlot;
    public ArrayList<HashMap<String, String>> subjectArrayList,slotArrayList;
    ProgressDialog dialog,dialog1;
    public static int size2,size3;
    String[] arrSub,arrSlot;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_main_profile, container, false);
        urlBasic = Config.baseUrl2 + "TM_Script/getStudentProfile.php?email=" + SharedPrefManager.getInstance(getContext()).getUserName().toString();
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtMobile = (TextView) view.findViewById(R.id.txtMobile);
        txtQualification = (TextView) view.findViewById(R.id.txtEducation);
        txtBasicEdit = (TextView) view.findViewById(R.id.txtBasicEdit);
        txtEduEdit = (TextView) view.findViewById(R.id.txtEduEdit);
        txtAddSubject = (TextView) view.findViewById(R.id.txtAddSubject);
        txtAddSlot=(TextView) view.findViewById(R.id.txtAddSlot);
        subjectArrayList = new ArrayList<HashMap<String, String>>();
        slotArrayList = new ArrayList<HashMap<String, String>>();
        listViewSubject = (ListView) view.findViewById(R.id.listViewSubjects);
        listViewSlot=(ListView)view.findViewById(R.id.listStuSlot);
        txtBasicEdit.setOnClickListener(this);
        txtEduEdit.setOnClickListener(this);
        txtAddSubject.setOnClickListener(this);
        txtAddSlot.setOnClickListener(this);
        getStudentBasicInfo();
        getStudentSubject();
        getStudentSlot();
        dialog = ProgressDialog.show(getContext(), "", "Please Wait", false, false);
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

    private void getStudentSubject() {
        String url3 = Config.baseUrl2 + "TM_Script/getStudentSubjects.php?email=" + SharedPrefManager.getInstance(getContext()).getUserName();

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
                    listAdapterSubject = new SimpleAdapter(getActivity(), subjectArrayList, R.layout.list_stu_subject,
                            new String[]{"sub_id", "subjectName"}, new int[]{R.id.txtStuSubId,
                            R.id.txtSubject});
                    listViewSubject.setAdapter(listAdapterSubject);
                    setListViewHeightBasedOnChildren(listViewSubject);
                    listViewSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String send = listViewSubject.getItemAtPosition(position).toString();
                             arrSub = send.split("[{=,}]");

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            // Get the layout inflater
                            LayoutInflater inflater = getActivity().getLayoutInflater();

                            // Inflate and set the layout for the dialog
                            // Pass null as the parent view because its going in the dialog layout
                            builder.setTitle("Action!")
                                    // Add action buttons
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int id) {
                                            //final ProgressDialog dialog1;
                                            //Toast.makeText(getContext(), "Bhak", Toast.LENGTH_SHORT).show();
                                            dialog1 = ProgressDialog.show(getContext(), "", "Please Wait", false, false);


                                            String urlUpdate = Config.baseUrl2 + "TM_Script/DeleteStudentSubject.php";
                                            dialog1 = ProgressDialog.show(getContext(), "", "Please Wait", false, false);
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    dialog1.dismiss();
                                                    Log.e("Aligarh", response);
                                                    if (response.toString().contains("success")) {

                                                        Toast.makeText(getContext(), "Successfully Deleted", Toast.LENGTH_LONG).show();
                                                        Intent intent=new Intent(getContext(),StudentProfile.class);
                                                        startActivity(intent);
                                                    } else if (response.toString().contains("failure")) {
                                                        Toast.makeText(getContext(), "Please try Again", Toast.LENGTH_LONG).show();
                                                    } else if (response.toString().contains("failed")) {
                                                        Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    dialog1.dismiss();
                                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> param = new HashMap<>();
                                                    param.put("subId", arrSub[2]);
                                                    return param;
                                                }
                                            };
                                            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

                                        }
                                    })
                                    .setNegativeButton("Update", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // LoginDialogFragment.this.getDialog().cancel();
                                            Intent intentEdu = new Intent(getContext(),UpdateStudentSubject.class);
                                            intentEdu.putExtra("idSub", arrSub[2]);
                                            intentEdu.putExtra("sub", arrSub[4]);
                                            //intentEdu.putExtra("data",send);
                                            startActivity(intentEdu);
                                            // Toast.makeText(getContext(), "Bhak", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                            builder.create();
                            builder.show();

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
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequestSubject);
    }


    private void getStudentBasicInfo() {

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
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest1);

    }

    private void showResult(String response) {
        String name = "";
        String address = "";
        String mobile = "";
        qualification = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("resultStudentBasic");
            JSONObject basicInfo = jsonArray.getJSONObject(0);
            name = basicInfo.getString("fname") + " " + basicInfo.getString("lname");
            mobile = basicInfo.getString("mobile");
            address = basicInfo.getString("address") + ", "+basicInfo.getString("landmark")+", " + basicInfo.getString("city") + ", " + basicInfo.getString("state") + ", " + basicInfo.getString("country");
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
        String urlSlot=Config.baseUrl2+"TM_Script/getStudentSlot.php?email="+SharedPrefManager.getInstance(getContext()).getUserName();
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

                    listAdapterSlot = new SimpleAdapter(getContext(), slotArrayList, R.layout.list_stu_slot,
                            new String[]{"startTime", "lastTime", "status","slot_id"}, new int[]{R.id.txtStart,
                            R.id.txtLast, R.id.txtStatus,R.id.txtStuSlotId});
                    listViewSlot.setAdapter(listAdapterSlot);
                    setListViewHeightBasedOnChildren(listViewSlot);
                    listViewSlot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String send = listViewSlot.getItemAtPosition(position).toString();
                            arrSlot = send.split("[{=,()}]");



                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            // Get the layout inflater
                            LayoutInflater inflater = getActivity().getLayoutInflater();

                            // Inflate and set the layout for the dialog
                            // Pass null as the parent view because its going in the dialog layout
                            builder.setTitle("Action!")
                                    // Add action buttons
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int id) {
                                            //final ProgressDialog dialog1;
                                            //Toast.makeText(getContext(), "Bhak", Toast.LENGTH_SHORT).show();
                                            dialog1 = ProgressDialog.show(getContext(), "", "Please Wait", false, false);
                                            String urlUpdate = Config.baseUrl2 + "TM_Script/StudentSlotDelete.php";
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    dialog1.dismiss();
                                                    Log.e("Aligarh", response);
                                                    if (response.toString().contains("success")) {
                                                        Toast.makeText(getContext(), "Successfully Deleted", Toast.LENGTH_LONG).show();
                                                        Intent in = new Intent(getContext(), StudentProfile.class);
                                                        startActivity(in);
                                                    } else if (response.toString().contains("failure")) {
                                                        Toast.makeText(getContext(), "Please try Again", Toast.LENGTH_LONG).show();
                                                    } else if (response.toString().contains("failed")) {
                                                        Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    dialog1.dismiss();
                                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> param = new HashMap<>();
                                                    param.put("slotId", arrSlot[11]);
                                                    return param;
                                                }
                                            };
                                            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

                                        }
                                    })
                                    .setNegativeButton("Update", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // LoginDialogFragment.this.getDialog().cancel();
                                            Intent intentSlot = new Intent(getContext(),UpdateStudentSlot.class);
                                            intentSlot.putExtra("slotStart", arrSlot[8]);
                                            intentSlot.putExtra("slotEnd",arrSlot[4]);
                                            intentSlot.putExtra("slot_id",arrSlot[11]);
                                            //intentEdu.putExtra("data",send);
                                            startActivity(intentSlot);
                                            // Toast.makeText(getContext(), "Bhak", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                            builder.create();
                            builder.show();

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
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest3);

    }

    @Override
    public void onClick(View v) {
        if (v == txtBasicEdit) {
            Intent intent = new Intent(getContext(), UpdateStudentBasicInfo.class);
            startActivity(intent);
        }
        if (v == txtEduEdit) {
            Intent intent = new Intent(getContext(), UpdateStudentEducation.class);
            intent.putExtra("qualification", qualification);
            startActivity(intent);
        }
        if (v == txtAddSubject) {
            Intent intent = new Intent(getContext(), StudentAddSubject.class);
            //intent.putExtra("qualification", qualification);
            startActivity(intent);
        }
        if (v == txtAddSlot) {
            Intent intent = new Intent(getContext(), AddStudentSlot.class);
            //intent.putExtra("qualification", qualification);
            startActivity(intent);
        }
    }

}
