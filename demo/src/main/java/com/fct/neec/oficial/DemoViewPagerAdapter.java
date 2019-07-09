package com.fct.neec.oficial;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 *
 */
public class DemoViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragments = new ArrayList<>();
	private Fragment currentFragment;

	public DemoViewPagerAdapter(FragmentManager fm) {
		super(fm);

        fragments.clear();
        fragments.add(SobreNEEC.newInstance(0));
        fragments.add(InfoFragment.newInstance(1));
        fragments.add(NoticiasFragment.newInstance(2));
        fragments.add(CalendarioFragment.newInstance(3));
        fragments.add(MapaFragment.newInstance(4));
    }

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = (Fragment) object;
		}
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public Fragment getCurrentFragment() {
		return currentFragment;
	}
}