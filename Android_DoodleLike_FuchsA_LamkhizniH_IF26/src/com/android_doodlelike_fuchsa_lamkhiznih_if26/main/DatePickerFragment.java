package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


	Activity activ;
	View v;
	List<DateForm> myList;
	DateFormAdapter messageItemArrayAdapter;

	public DatePickerFragment(Activity act ,View vi,List<DateForm> list,DateFormAdapter adapt) {
		activ = act;
		v=vi;
		messageItemArrayAdapter = adapt;
		myList=list;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user

			myList.add(new DateForm(year,month,day));
		messageItemArrayAdapter.notifyDataSetChanged();
		
		Log.v("ze :", " "+myList.size());
		TextView t = (TextView) v.findViewById(R.id.EditTextName);
		t.setText("y : "+year+" ok");
	}
}