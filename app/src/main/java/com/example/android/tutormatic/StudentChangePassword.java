package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mujahid on 4/23/2017.
 */
public class StudentChangePassword extends Fragment implements View.OnClickListener {
    EditText oldPassword,newPassword1,newPassword2;
    Button changePassword;
    public String new1,new2;
    ProgressDialog dialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Change Password");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.change_password,container,false);
        oldPassword=(EditText)view.findViewById(R.id.oldPassword);
        newPassword1=(EditText)view.findViewById(R.id.newPassword1);
        newPassword2=(EditText)view.findViewById(R.id.newPassword2);
        changePassword=(Button)view.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);


        return view;
    }
    @Override
    public void onClick(View v) {
        change();
    }

    private void change() {

        new1=newPassword1.getText().toString();
        new2=newPassword2.getText().toString();
        if (oldPassword.getText().toString().equals("")){
            oldPassword.setError("Enter Old Password");
        }else if (new1.equals("")){
            newPassword1.setError("Enter new password");
        }else if (new2.equals("")){
            newPassword2.setError("Re-Enter password");
        }else{
            if ( new1.equals(new2)){
                updatePassword();
                // Toast.makeText(getContext(),"ok processed",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"Password Not Matched", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updatePassword() {

        String urlUpdate = Config.baseUrl2 + "TM_Script/changePasswordStudent.php";
        dialog = ProgressDialog.show(getContext(), "", "Please Wait", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.e("Password", response);
                if (response.toString().contains("success")) {
                    oldPassword.setText("");
                    newPassword1.setText("");
                    newPassword2.setText("");
                    Toast.makeText(getContext(), "Successfully Changed", Toast.LENGTH_LONG).show();
                } else if (response.toString().contains("failed")) {
                    Toast.makeText(getContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                } else if (response.toString().contains("failure")) {
                    Toast.makeText(getContext(), "Enter correct old password", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getContext(), "check Internet Connection", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email", SharedPrefManager.getInstance(getContext()).getUserName());
                param.put("oldPassword", oldPassword.getText().toString());
                param.put("newPassword", new1);
                return param;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }



}
