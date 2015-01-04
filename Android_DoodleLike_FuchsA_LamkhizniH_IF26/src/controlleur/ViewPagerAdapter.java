package controlleur;

import com.android_doodlelike_fuchsa_lamkhiznih_if26.main.FragmentTab1;
import com.android_doodlelike_fuchsa_lamkhiznih_if26.main.FragmentTab2;
import com.android_doodlelike_fuchsa_lamkhiznih_if26.main.FragmentTab3;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
 
	final int PAGE_COUNT = 3;
	// Tab titre
	private String tabtitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
	Context context;
 
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
 
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
 
	@Override
	public Fragment getItem(int position) {
		switch (position) {
 
			// ouvre FragmentTab1.java
		case 0:
			FragmentTab1 fragmenttab1 = new FragmentTab1();
			return fragmenttab1;
 
			// ouvre FragmentTab2.java
		case 1:
			FragmentTab2 fragmenttab2 = new FragmentTab2();
			return fragmenttab2;
 
			// ouvre FragmentTab3.java
		case 2:
			FragmentTab3 fragmenttab3 = new FragmentTab3();
			return fragmenttab3;
		}
		return null;
	}
 
	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}