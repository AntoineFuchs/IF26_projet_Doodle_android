package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

import java.util.ArrayList;
import java.util.List;

import listviews.DateChooseAdapter;
import org.apache.http.HttpResponse;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import form.DateForm;


//  Lorsque l'on choisie un RDV dans le menu principale.
//  Permet de choisir les date qui nous interesse

public class EventActivity extends Activity{
	
	List<DateForm> listDatesChoose;
	ListView myListDates;
	DateChooseAdapter dateChooseAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		
		
		/*
		listContactPicker = contactBdd.getAllComments();
		contactFormAdapter = new ContactFormAdapter(this,listContactPicker);
		 */
		DateForm date = new DateForm(2002,12,5);
		date.addHours(18);date.addMinutes(24);date.addnbrParticipant(2); date.addHours(5);date.addMinutes(17);date.addnbrParticipant(8);
		DateForm date1 = new DateForm(2014,8,24);
		date1.addHours(6);date1.addMinutes(4);date1.addnbrParticipant(0);
		
		listDatesChoose = new ArrayList<DateForm>(); listDatesChoose.add(date);listDatesChoose.add(date1);
		dateChooseAdapter = new DateChooseAdapter(this,listDatesChoose);
		myListDates=(ListView)this.findViewById(R.id.listView_dates);
		myListDates.setAdapter(dateChooseAdapter);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void postResult(HttpResponse asyncresult) {
		// TODO Auto-generated method stub
		
	}
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
