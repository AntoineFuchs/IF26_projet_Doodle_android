package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class FragmentTab2 extends Fragment   {
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.fragmenttab2, container, false);
		return view;
	}

}