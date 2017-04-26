package com.example.android.tutormatic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewAndRatingStudent extends AppCompatActivity implements View.OnClickListener {
    Button btnSave;
    Spinner rateSpinner;
    EditText etReview,etSubject;
    String email;
    ProgressDialog dialog;
    private String blockCharacterSet = "~#={}()^,|$%&*!";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_rating_student);
        etReview=(EditText) findViewById(R.id.etReview);
        etSubject=(EditText)findViewById(R.id.etSubject);
        rateSpinner=(Spinner)findViewById(R.id.rateSpinner);
        etReview.setFilters(new InputFilter[]{filter});
        etSubject.setFilters(new InputFilter[]{filter});
        btnSave=(Button)findViewById(R.id.btnReview);
        btnSave.setOnClickListener(this);
        Bundle extra = getIntent().getExtras();
        email = extra.getString("email");
    }

    @Override
    public void onClick(View v) {

        if ( etReview.getText().toString().equals("") || etSubject.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Fill Entries", Toast.LENGTH_LONG).show();
        } else {
                String urlUpdate = Config.baseUrl2 + "TM_Script/insertReview.php";
                dialog = ProgressDialog.show(this, "", "Please Wait", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.e("Review", response);
                        if (response.toString().contains("success")) {
                            Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(ReviewAndRatingStudent.this, TutorProfileAfterSearch.class);
                            in.putExtra("email", email);
                            startActivity(in);
                            finish();
                        } else if (response.toString().contains("failure")) {
                            Toast.makeText(getApplicationContext(), "Please try Again", Toast.LENGTH_LONG).show();
                        } else if (response.toString().contains("failed")) {
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("studentEmail", SharedPrefManager.getInstance(getApplicationContext()).getUserName());
                        param.put("tutorEmail", email);
                        param.put("rate", rateSpinner.getSelectedItem().toString().trim());
                        param.put("subject", etSubject.getText().toString());
                        param.put("review", etReview.getText().toString());
                        return param;
                    }
                };
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }



    }
}
