package com.fct.neec.oficial;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 *
 */
public class DemoViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<NoticiasFragment> fragments = new ArrayList<>();
	private NoticiasFragment currentFragment;

	public DemoViewPagerAdapter(FragmentManager fm) {
		super(fm);

		fragments.clear();
		fragments.add(NoticiasFragment.newInstance(0));
		fragments.add(NoticiasFragment.newInstance(1));
		fragments.add(NoticiasFragment.newInstance(2));
		fragments.add(NoticiasFragment.newInstance(3));
		fragments.add(NoticiasFragment.newInstance(4));
	}

	@Override
	public NoticiasFragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = ((NoticiasFragment) object);
		}
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public NoticiasFragment getCurrentFragment() {
		return currentFragment;
	}
}