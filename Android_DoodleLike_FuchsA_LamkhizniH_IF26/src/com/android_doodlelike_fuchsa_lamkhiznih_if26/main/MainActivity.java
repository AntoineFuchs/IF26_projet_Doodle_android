package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

import java.util.ArrayList;
import java.util.List;

import listviews.ContactFormAdapter;
import listviews.DateFormAdapter;
import listviews.InviteFormAdapter;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import bdd.ContactsBDD;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import controlleur.DatePickerFragment;
import controlleur.InvitePickerFragment;
import controlleur.TimePickerFragment;
import controlleur.ViewPagerAdapter;
import form.ContactForm;
import form.DateForm;
import form.InviteForm;

public class MainActivity extends FragmentActivity {
	int prems = 0 ;

	DateFormAdapter dateFormAdapter;
	ContactFormAdapter contactFormAdapter;
	TimePickerFragment timePickerFragment;
	InviteFormAdapter inviteFormAdapter;

	List<DateForm> listDatesPicker;
	List<InviteForm> listInvitesPicker;
	List<ContactForm> listContactPicker;
	ListView myListDates, myListInvites, myListContact;

	DialogFragment datePickerFragment;
	InvitePickerFragment invitePickerFragment;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		setContentView(R.layout.activity_main);

		// Locate the viewpager in activity_main.xml
		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

		// Set the ViewPagerAdapter into ViewPager
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));



		ContactsBDD contactBdd  = new ContactsBDD(this.getApplicationContext());

		//ContactForm message = new ContactForm("trt","rze","erz");
		contactBdd.open();
		listContactPicker = contactBdd.getAllComments();
		contactFormAdapter = new ContactFormAdapter(this,listContactPicker);
		myListContact = null;
		contactBdd.close();

		listDatesPicker = new ArrayList<DateForm>();
		dateFormAdapter = new DateFormAdapter(this,listDatesPicker);
		myListDates=null;

		listInvitesPicker = new ArrayList<InviteForm>();
		inviteFormAdapter = new InviteFormAdapter(this,listInvitesPicker);
		myListInvites = null;



	}


	public void addNewContact (View v){
		if(prems == 0){
			myListContact=(ListView)this.findViewById(R.id.listContacts);
			myListContact.setAdapter(contactFormAdapter);
			myListContact.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long arg3) {


					ContactsBDD contactBdd  = new ContactsBDD(view.getContext());
					contactBdd.open();
					contactBdd.removeWithEmail(listContactPicker.get(position).geteMail().toString());
					contactBdd.close();

					contactFormAdapter.remove(listContactPicker.get(position));
					contactFormAdapter.notifyDataSetChanged();

					return false;
				}

			});


			prems++;
		}



		EditText edTxT = (EditText)this.findViewById(R.id.newContactEmail);

		if( ! isValidEmail(edTxT.getText().toString()))
			edTxT.setError( "exemple@exemple.exemple" );

		else{
			ContactForm message = new ContactForm(edTxT.getText().toString());

			listContactPicker.add(message);
			contactFormAdapter.notifyDataSetChanged();

			ContactsBDD contactBdd  = new ContactsBDD(v.getContext());
			contactBdd.open();
			contactBdd.insert(message);
			contactBdd.close();
		}
		edTxT.setText("");
	}

	public void showDatePickerDialog(View v) {
		if(myListDates == null)
		{	
			myListDates=(ListView)this.findViewById(R.id.listView_dates);
			myListDates.setAdapter(dateFormAdapter);
			myListDates.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 120));
			myListDates.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long arg3) {

					dateFormAdapter.remove(listDatesPicker.get(position));
					dateFormAdapter.notifyDataSetChanged();

					return false;
				}

			});
			myListDates.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
					timePickerFragment = new TimePickerFragment(position,listDatesPicker, dateFormAdapter);
					timePickerFragment.show(getSupportFragmentManager(), "timePicker");
				} 
			});

			datePickerFragment = new DatePickerFragment(listDatesPicker, dateFormAdapter);

		}
		datePickerFragment.show(getSupportFragmentManager(), "datePicker");

	}


	public void showInvitePickerDialog(View v) {
		if(myListInvites == null){

			myListInvites=(ListView)this.findViewById(R.id.listView_invites);
			myListInvites.setAdapter(inviteFormAdapter);
			myListInvites.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 95));
			myListInvites.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long arg3) {
					invitePickerFragment.removeListString(listInvitesPicker.get(position).getNomInvite());

					inviteFormAdapter.remove(listInvitesPicker.get(position));
					inviteFormAdapter.notifyDataSetChanged();
					return false;
				}

			});
			if(invitePickerFragment == null )
			{
					invitePickerFragment = new InvitePickerFragment(listInvitesPicker,inviteFormAdapter, new ContactsBDD(this.getApplicationContext()));
				
			}
			
		}
		if(! invitePickerFragment.isAdded())
			invitePickerFragment.show(getSupportFragmentManager(), "invitePicker");
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

}