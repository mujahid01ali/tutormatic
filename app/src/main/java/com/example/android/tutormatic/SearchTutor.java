package com.example.android.tutormatic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Mujahid on 3/3/2017.
 */
public class SearchTutor extends Fragment implements View.OnClickListener {
    Button btnSearchByName,btnSearchSkill,btnSearchByQualification,btnSearchByLoaction,locationS;
    static final Integer LOCATION = 0x1;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search Tutors");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.search_tutor,container,false);

        btnSearchByName=(Button) view.findViewById(R.id.btnSearchName);
        btnSearchSkill=(Button)view.findViewById(R.id.btnSearchSkill);
        btnSearchByQualification=(Button) view.findViewById(R.id.btnSearchQualification);
        btnSearchByLoaction=(Button)view.findViewById(R.id.location);
         locationS=(Button)view.findViewById(R.id.location);
        btnSearchByName.setOnClickListener(this);
        btnSearchSkill.setOnClickListener(this);
        btnSearchByQualification.setOnClickListener(this);
        btnSearchByLoaction.setOnClickListener(this);
        locationS.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        if (v==btnSearchByName){
            Intent intentName=new Intent(getContext(),TutorSearchByName.class);
            startActivity(intentName);
        }
        if (v==btnSearchSkill){
            Intent intentName=new Intent(getContext(),TutorSearchBySkill.class);
            startActivity(intentName);
        }
        if (v==btnSearchByQualification){
            Intent intentName=new Intent(getContext(),TutorSearchByQualification.class);
            startActivity(intentName);
        }
        if (v==btnSearchByLoaction){
            askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);

        }


    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);


            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Toast.makeText(getActivity(),"Permission granted",Toast.LENGTH_SHORT).show();
                startActivity(intent);


            }
        } else {
            //Toast.makeText(getContext(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            Intent in=new Intent(getContext(),LocatioSearchByGPS.class);
            startActivity(in);
        }
    }





}
