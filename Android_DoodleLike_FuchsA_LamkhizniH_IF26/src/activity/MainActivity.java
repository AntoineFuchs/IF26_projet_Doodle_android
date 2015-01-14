package activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.android_doodlelike_fuchsa_lamkhiznih_if26.R;

import controlleur.ObscuredSharedPreferences;
import controlleur.SyncAdapter;
import controlleur.ViewPagerAdapter;

public class MainActivity extends FragmentActivity {

	ViewPagerAdapter viewPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		setContentView(R.layout.activity_main);
		
		// met à jour le security provider de android  (eviter les m.i.m.)
		 new SyncAdapter(this, true);
		
		 

		 
		// Locate the viewpager in activity_main.xml
		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		// Set the ViewPagerAdapter into ViewPager
		viewPager.setAdapter(viewPagerAdapter);

	}

}