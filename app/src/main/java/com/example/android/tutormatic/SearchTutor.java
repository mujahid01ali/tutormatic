package com.example.android.tutormatic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mujahid on 3/3/2017.
 */
public class SearchTutor extends Fragment implements View.OnClickListener {

    Button btnLogout;
    TextView txtName;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search Tutors");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.search_tutor,container,false);
        btnLogout=(Button)view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Aloloooooooooooooo",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
