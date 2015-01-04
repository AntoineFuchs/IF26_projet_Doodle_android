package controlleur;

import java.util.Calendar;
import java.util.List;

import form.DateForm;
import listviews.DateFormAdapter;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import android.app.TimePickerDialog;;

public  class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	int position;
	List<DateForm> listDatesPicker;
	DateFormAdapter dateFormAdapter;
	
	public TimePickerFragment (int pos,List<DateForm> list,DateFormAdapter adapt){
		position = pos;
		dateFormAdapter = adapt;
		listDatesPicker=list;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		listDatesPicker.get(position).addHours(hourOfDay);
		listDatesPicker.get(position).addMinutes(minute);
		   dateFormAdapter.notifyDataSetChanged();
	}
}