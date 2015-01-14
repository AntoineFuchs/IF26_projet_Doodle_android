package controlleur;

import java.util.Calendar;
import java.util.List;

import listviews.DateFormAdapter;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import form.DateForm;
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
	
	// dans la creation de RDV fait apparaitre un calendrier pour choisir les dates
	


	List<DateForm> listDatesPicker;
	DateFormAdapter dateFormAdapter;

	public DatePickerFragment(List<DateForm> list,DateFormAdapter adapt) {
		dateFormAdapter = adapt;
		listDatesPicker=list;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view1, int year, int month, int day) { // Lors du choix d'une date la liste des dates est miss à jour

		listDatesPicker.add(new DateForm(year,month,day));
		dateFormAdapter.notifyDataSetChanged();
		

	}
}