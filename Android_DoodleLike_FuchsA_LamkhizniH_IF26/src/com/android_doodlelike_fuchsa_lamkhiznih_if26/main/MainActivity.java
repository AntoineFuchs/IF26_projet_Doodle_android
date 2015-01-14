package com.android_doodlelike_fuchsa_lamkhiznih_if26.main;

import java.util.ArrayList;
import java.util.List;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;










import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	private DateFormAdapter messageItemArrayAdapter;
	List<DateForm> messages1 = new ArrayList<DateForm>();
	ListView myList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		setContentView(R.layout.activity_main);

		// Locate the viewpager in activity_main.xml
		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

		// Set the ViewPagerAdapter into ViewPager
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		messageItemArrayAdapter=new DateFormAdapter(this,
				messages1);
		myList=null;

	}


	public void showDatePickerDialog(View v) {
		if(myList == null)
			{	
				myList=(ListView)this.findViewById(R.id.listView_items);
				myList.setAdapter(messageItemArrayAdapter);
				Log.v("", "DON?E");
				myList.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 180));
			}
		DialogFragment newFragment = new DatePickerFragment(this, getWindow().getDecorView().findViewById(android.R.id.content),messages1, messageItemArrayAdapter);
		newFragment.show(getSupportFragmentManager(), "datePicker");

	}

}