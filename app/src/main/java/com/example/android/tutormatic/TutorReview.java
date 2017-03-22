package com.example.android.tutormatic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Mujahid on 3/16/2017.
 */
public class TutorReview extends Fragment {
TextView txtView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Reviews");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tutor_review_show,container,false);
       txtView=(TextView)view.findViewById(R.id.txtWelComeText);
       txtView.setText("Review");
        return view;
    }
}
