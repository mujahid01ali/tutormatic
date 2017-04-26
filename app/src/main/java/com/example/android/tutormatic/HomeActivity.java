package com.example.android.tutormatic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    TextView seeTutors,seeStudents;
    public static int splash_time_out = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Tutor Matic");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        seeTutors=(TextView)findViewById(R.id.seeTutors);
        seeStudents=(TextView)findViewById(R.id.seeStudents);
        seeStudents.setOnClickListener(this);
        seeTutors.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v==seeTutors){
            Intent tutor=new Intent(HomeActivity.this,TutorListHome.class);
            startActivity(tutor);
        }
        if (v==seeStudents){
            Intent student=new Intent(HomeActivity.this,StudentListHome.class);
            startActivity(student);

        }

    }

    @Override
    protected void onStart() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefManager.getInstance(getApplicationContext()).isLogin())  {
                    if (SharedPrefManager.getInstance(getApplicationContext()).getUserType().contains("tutor")) {
                        Intent in = new Intent(HomeActivity.this, TutorProfile.class);
                        startActivity(in);
                        finish();
                    } else{
                        Intent intent = new Intent(HomeActivity.this, StudentProfile.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }, splash_time_out);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.mainSignUp){
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
