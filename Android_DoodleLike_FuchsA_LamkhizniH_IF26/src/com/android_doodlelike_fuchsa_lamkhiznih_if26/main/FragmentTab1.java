package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;
import java.util.ArrayList;
import java.util.List;

import listviews.EventListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import form.InfoEvent;
 
public class FragmentTab1 extends ListFragment  {
	private EventListAdapter messageItemArrayAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		View view = inflater.inflate(R.layout.fragmenttab1, container, false);
		interfaceBuilder();
		return view;
	}
 
	
	
	public void interfaceBuilder() {
		// Fait le JSonarray des contacts

			List<InfoEvent> messages1 = new ArrayList<InfoEvent>();
			InfoEvent message;
			for ( int i = 0 ; i<4;i++ ) {
				message = new InfoEvent();
				messages1.add(message);
			}
			messageItemArrayAdapter =new EventListAdapter(this.getActivity(),messages1);
			setListAdapter(messageItemArrayAdapter);
			
		
	}
	
	 public void onListItemClick(ListView l, View v, int position, long id) {
	        // TODO Auto-generated method stub
	        super.onListItemClick(l, v, position, id);
	        
			Intent IntentEvent = new Intent(this.getActivity(), EventActivity.class);
			startActivityForResult(IntentEvent, 0);
	    }
}