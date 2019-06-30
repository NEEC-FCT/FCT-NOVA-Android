package com.fct.neec.oficial;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 *
 */
public class DemoViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<DemoFragment> fragments = new ArrayList<>();
	private DemoFragment currentFragment;

	public DemoViewPagerAdapter(FragmentManager fm) {
		super(fm);

		fragments.clear();
		fragments.add(DemoFragment.newInstance(0));
		fragments.add(DemoFragment.newInstance(1));
		fragments.add(DemoFragment.newInstance(2));
		fragments.add(DemoFragment.newInstance(3));
		fragments.add(DemoFragment.newInstance(4));
	}

	@Override
	public DemoFragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = ((DemoFragment) object);
		}
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public DemoFragment getCurrentFragment() {
		return currentFragment;
	}
}