package com.example.android.tutormatic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TutorProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtHead,txtType;
    //Button btnLogout;
    //TextView txtUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header1 = LayoutInflater.from(this).inflate(R.layout.nav_header_tutor_profile, null);
        navigationView.addHeaderView(header1);
        txtHead=(TextView)header1.findViewById(R.id.txtHeadName);
        txtType=(TextView)header1.findViewById(R.id.txtHeadType);
        txtHead.setText(SharedPrefManager.getInstance(this).getUserName());
        txtType.setText(SharedPrefManager.getInstance(this).getUserType());
        navigationView.setNavigationItemSelectedListener(this);



        displaySelectedScreen(R.id.nav_mainProfile);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_mainProfile:
                fragment = new TutorMainProfile();
                break;
            case R.id.nav_searchStudents:
                fragment = new SearchStudent();
                break;
            case R.id.nav_logout:
                SharedPrefManager.getInstance(getApplicationContext()).logOut();
                Intent intent = new Intent(TutorProfile.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_student_review:
                fragment = new TutorReview();
                break;
            case R.id.nav_student_list:
                fragment = new TutorStudents();
                break;
            case R.id.nav_change_password:
                fragment = new TutorChangePassword();
                break;
            // Intent intent2=new Intent(TutorProfile.this,TutorReview.class);
            //startActivity(intent2);
        }
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_main_tutor, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

}
