package com.example.fragments;

import com.example.controller.R;
import com.example.utility.ConstantUtility;
import com.example.utility.StringConst;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment
{

	private SharedPreferences shpref;

	public HomeFragment()
	{
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		shpref = getActivity().getSharedPreferences(StringConst.My_PREFERENCES, 0);
		String userFn = shpref.getString(StringConst.FIRSTNAME, "");
		String userLn = shpref.getString(StringConst.LASTNAME, "");

		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		TextView userName = (TextView) rootView.findViewById(R.id.labelUserName);
		userName.setText(ConstantUtility.toCamelCase(userFn + " " + userLn));

		// Inflate the layout for this fragment
		return rootView;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
	}
}