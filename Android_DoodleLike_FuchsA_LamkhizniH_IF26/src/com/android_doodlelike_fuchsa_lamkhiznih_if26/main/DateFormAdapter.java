package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

import java.util.List;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DateFormAdapter extends ArrayAdapter<DateForm>{
    View vue;
	private LayoutInflater inflater;
    List<DateForm> message;
    
    public DateFormAdapter(Activity activity, List<DateForm> messages1){
    	super(activity, R.layout.date_form_row,messages1);
    	vue = null;
        inflater = activity.getWindow().getLayoutInflater();
        message = messages1;
    }


	@Override
    public View getView(int position, View convertView, ViewGroup parent){
    	View v = inflater.inflate(R.layout.date_form_row, null);
    	
    	TextView t = (TextView) v.findViewById(R.id.auteur);
		t.setText(message.get(position).getDay());
    	t = (TextView) v.findViewById(R.id.textView1);
		t.setText(message.get(position).getMonth());
    	 t = (TextView) v.findViewById(R.id.textView2);
		t.setText(message.get(position).getYear());


        return v;
    }

}