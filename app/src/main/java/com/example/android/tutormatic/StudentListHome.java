package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class StudentListHome extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();
    ListView listStudentHome;
    ProgressDialog dialog;
    JSONArray student=null;
    String MyJson;
    String url;
    ListAdapter listAdapter;
    ArrayList<HashMap<String, String>> studentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Students");
        listStudentHome=(ListView)findViewById(R.id.listStudentHome);
        dialog=ProgressDialog.show(this,"","Please Wait...",false,false);
        studentList=new ArrayList<HashMap<String, String>>();
        getStudentList();



    }

    private void getStudentList() {

        url=Config.baseUrl2+"TM_Script/studentListHome.php";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                if (response.toString().contains("null")){
                    Toast.makeText(getApplicationContext(),"No Recored Found",Toast.LENGTH_LONG).show();
                }
                else{
                    Log.d(TAG,response);
                    studentList.clear();

                    MyJson=response;
                    String fname;
                    String lname;
                    String city,landmark;
                    try {
                        JSONObject jsonObject=new JSONObject(MyJson);
                        student=jsonObject.getJSONArray("result");
                        for (int j=0;j<student.length();j++){
                            JSONObject a=student.getJSONObject(j);
                            fname=a.getString("fname");
                            lname=a.getString("lname");
                            landmark=a.getString("landmark");
                            city=a.getString("city");
                            HashMap<String,String> tut=new HashMap<String, String>();
                            tut.put("name",fname+" "+lname);
                            tut.put("landmark",landmark);
                            tut.put("city",city);
                            studentList.add(tut);
                        }
                        listAdapter=new SimpleAdapter(getApplicationContext(),studentList,R.layout.tutor_list_home,
                                new String[]{"name", "landmark","city"}, new int[]{R.id.tutName, R.id.tutLandmark,R.id.tutCity});
                        listStudentHome.setAdapter(listAdapter);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();

            }
        });
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            Intent hm=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(hm);
        }
        if(id==R.id.mainSignUp){
            Toast.makeText(this,"Your Option"+item.getTitle(),Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(i);
        }
        if (id==R.id.mainSignIn){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
