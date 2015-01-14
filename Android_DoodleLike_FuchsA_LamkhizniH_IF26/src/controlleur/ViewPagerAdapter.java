package controlleur;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
 
// gere les fragment dans la mainActivity
public class ViewPagerAdapter extends FragmentStatePagerAdapter  {
 
	final int PAGE_COUNT = 3;
	
	FragmentTab1 fragmenttab1 ;
	FragmentTab2 fragmenttab2 ;
	FragmentTab3 fragmenttab3 ;

	
	// Tab titre
	private String tabtitles[] = new String[] { "Mes rendez-vous", "Créer rendez-vous", "Contacts" };
	Context context;
 
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
		 fragmenttab1 = new FragmentTab1();
		 fragmenttab2 = new FragmentTab2();
		 fragmenttab3 = new FragmentTab3();
	}
 
	@Override
	public int getCount() {
		return PAGE_COUNT;
	}
 
	public void resetFrag1(){
		fragmenttab1 = new FragmentTab1();
	}
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
 
			// ouvre FragmentTab1.java
		case 0:
			return new FragmentTab1();
 
			// ouvre FragmentTab2.java
		case 1:

			return fragmenttab2;
 
			// ouvre FragmentTab3.java
		case 2:
			return fragmenttab3;
		}
		return null;
	}

 
	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
}