package com.example.fragments;

import com.custom.CalendarView;
import com.example.controller.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AttendenceFragment extends Fragment {
 
    public AttendenceFragment() {
        // Required empty public constructor
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendence, container, false);
 
        CalendarView cv = ((CalendarView)rootView.findViewById(R.id.calendar_view));
 
        // Inflate the layout for this fragment
        return rootView;
    }
 
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
 
    @Override
    public void onDetach() {
        super.onDetach();
    }
}