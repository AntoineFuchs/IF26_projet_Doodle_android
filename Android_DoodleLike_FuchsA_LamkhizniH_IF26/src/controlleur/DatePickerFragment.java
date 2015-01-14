package controlleur;

import java.util.Calendar;
import java.util.List;

import modele.DateForm;
import android.app.ActionBar.LayoutParams;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;

//Utiliser dans Fragment 2 pour ajouter une date

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
	
	// dans la creation de RDV fait apparaitre un calendrier pour choisir les dates
	

	FragmentActivity fragmentActivity;
	FragmentTab2 viewPagerAdapter;
	List<DateForm> listDatesPicker;
	DateFormAdapter dateFormAdapter;
	ListView myListDates;
	

	public DatePickerFragment(FragmentActivity fActv,ListView viewPagerAdapte,List<DateForm> list,DateFormAdapter adapt) {
		fragmentActivity = fActv;
		myListDates =viewPagerAdapte ;
		dateFormAdapter = adapt;
		listDatesPicker=list;
		this.build();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	private void build(){
		

		myListDates.setAdapter(dateFormAdapter);
		
		myListDates.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 90));
		myListDates.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long arg3) {

				listDatesPicker.remove(listDatesPicker.get(position));
				dateFormAdapter.notifyDataSetChanged();

				return false;
			}

		});
		myListDates.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				TimePickerFragment timePickerFragment = new TimePickerFragment(position,listDatesPicker, dateFormAdapter);
				timePickerFragment.show(fragmentActivity.getSupportFragmentManager(), "timePicker");
			} 
		});
	}

	public void onDateSet(DatePicker view1, int year, int month, int day) { // Lors du choix d'une date la liste des dates est miss à jour
		listDatesPicker.add(new DateForm(year,month,day));
		dateFormAdapter.notifyDataSetChanged();
		
	}

	public List<DateForm> getListDatesPicker() {
		return listDatesPicker;
	}

	public void setListDatesPicker(List<DateForm> listDatesPicker) {
		this.listDatesPicker = listDatesPicker;
	}

	public DateFormAdapter getDateFormAdapter() {
		return dateFormAdapter;
	}

	public void setDateFormAdapter(DateFormAdapter dateFormAdapter) {
		this.dateFormAdapter = dateFormAdapter;
	}
	
}