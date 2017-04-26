package com.example.android.tutormatic;

import android.app.ProgressDialog;
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
 * Created by Mujahid on 4/21/2017.
 */
public class TutorStudents extends Fragment {
    public ListView listViewStuTutors;
    public ListAdapter listAdapterTutors;
    public ArrayList<HashMap<String, String>> studentArrayList;
    ProgressDialog dialog;
    int size1;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Students List");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tutor_students,container,false);

        studentArrayList= new ArrayList<HashMap<String, String>>();
        listViewStuTutors=(ListView) view.findViewById(R.id.listViewTutorStudents);
        dialog = ProgressDialog.show(getContext(), "", "Please Wait", false, false);
        getList();

        return view;
    }

    private void getList() {
        String url2=Config.baseUrl2+"TM_Script/getTutorStudentsList.php?email="+SharedPrefManager.getInstance(getContext()).getUserName();

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.d("Education", response.toString());
                studentArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray slotArray = jsonObject.getJSONArray("resultTutor");
                    size1 = slotArray.length();
                    for (int j = 0; j < size1; j++) {
                        JSONObject a = slotArray.getJSONObject(j);
                        String id=a.getString("id");
                        String name = a.getString("StudentName");
                        String email = a.getString("StudentEmail");
                        String date1 = a.getString("SeletionDate");
                        HashMap<String, String> edu = new HashMap<String, String>();
                        edu.put("id",id);
                        edu.put("name", name);
                        edu.put("email", email);
                        edu.put("date1", date1);
                        studentArrayList.add(edu);
                    }
                    listAdapterTutors = new SimpleAdapter(getContext(), studentArrayList, R.layout.list_tutor_for_student,
                            new String[]{"id", "name", "email", "date1"}, new int[]{R.id.tutor_id,R.id.tutor_name,
                            R.id.tutor_email, R.id.tutor_date});
                    listViewStuTutors.setAdapter(listAdapterTutors);
                    listViewStuTutors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String send = listViewStuTutors.getItemAtPosition(position).toString();
                            String[] arr = send.split("[{=,}]");
                            Intent intentEdu = new Intent(getContext(), StudentProfileAfterSearch.class);
                            intentEdu.putExtra("email", arr[2]);
                            startActivity(intentEdu);
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
                Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_LONG).show();


            }
        });
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest2);
    }
}
