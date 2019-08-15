package com.fct.neec.oficial;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fct.neec.oficial.Fragments.ClassesDocsFragment;
import com.fct.neec.oficial.Fragments.ClassesFragment;
import com.fct.neec.oficial.Fragments.ClipLoginFragment;
import com.fct.neec.oficial.Fragments.InfoFragment;
import com.fct.neec.oficial.Fragments.MapaFragment;
import com.fct.neec.oficial.Fragments.NoticiasFragment;
import com.fct.neec.oficial.Fragments.ScheduleViewPager;
import com.fct.neec.oficial.Fragments.SobreNEEC;
import com.fct.neec.oficial.Fragments.StudentNumbersFragment;

import java.util.ArrayList;

/**
 *
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.clear();
        fragments.add(SobreNEEC.newInstance(0));
        fragments.add(InfoFragment.newInstance(1));
        fragments.add(NoticiasFragment.newInstance(2));
        fragments.add(CalendarioFragment.newInstance(3));
        fragments.add(MapaFragment.newInstance(4));
        fragments.add(ClipLoginFragment.newInstance(5));
        fragments.add(StudentNumbersFragment.newInstance(6));
        fragments.add(ScheduleViewPager.newInstance(7));
        fragments.add(ClassesFragment.newInstance(8));
        fragments.add(ClassesDocsFragment.newInstance(9));
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

    public void reload(int i) {
        if (i == 7) {
            fragments.set(7, ScheduleViewPager.newInstance(7));
            fragments.set(8, ClassesFragment.newInstance(8));
            fragments.set(9, ClassesDocsFragment.newInstance(9));
        }

    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}