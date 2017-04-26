package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Mujahid on 3/16/2017.
 */
public class TutorReview extends Fragment {
    ListView list_view_tutor;
    public ListAdapter listAdapterReview;
    public ArrayList<HashMap<String, String>> reviewArrayList;
    ProgressDialog dialog;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Reviews");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tutor_review_show,container,false);
        list_view_tutor=(ListView)view.findViewById(R.id.list_view_review);
        reviewArrayList = new ArrayList<HashMap<String, String>>();
        dialog = ProgressDialog.show(getContext(), "", "Please Wait", false, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            list_view_tutor.setNestedScrollingEnabled(true);
        }
        getReview();
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

    private void getReview() {
            String url5 = Config.baseUrl2 + "TM_Script/getReview.php?email=" + SharedPrefManager.getInstance(getActivity()).getUserName();
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
                        listAdapterReview = new SimpleAdapter(getActivity(), reviewArrayList, R.layout.list_tut_review,
                                new String[]{"id", "studentEmail", "subject", "reviewDate", "rating", "review"}, new int[]{R.id.txtReviewId,
                                R.id.studentEmail, R.id.txtSubject, R.id.txtDate, R.id.rating, R.id.txtReview,});

                        list_view_tutor.setAdapter(listAdapterReview);
                        setListViewHeightBasedOnChildren(list_view_tutor);

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
            //Request Handler
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest5);

    }
}
