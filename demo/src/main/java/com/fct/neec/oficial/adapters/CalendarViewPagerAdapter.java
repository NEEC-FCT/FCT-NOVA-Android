package com.fct.neec.oficial.adapters;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.entities.StudentCalendar;
import com.fct.neec.oficial.Fragments.Calendario.CalendarFragment;

import java.util.ArrayList;
import java.util.List;

public class CalendarViewPagerAdapter extends FragmentPagerAdapter {
    public static final String CALENDAR_TAG = "calendar_tag";
    private final String[] tabNames;
    private Student student;

    public CalendarViewPagerAdapter(FragmentManager fm, String[] tabNames, Student student) {
        super(fm);
        this.tabNames = tabNames;
        this.student = student;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

    @Override
    public Fragment getItem(int position) {
        List<StudentCalendar> calendar = student.getStudentCalendar().get(position == 1);

        Fragment fragment = new CalendarFragment();
        fragment.setArguments(getBundle(calendar));

        return fragment;
    }

    @Override
    public int getCount() {
        return tabNames.length;
    }

    private Bundle getBundle(List<StudentCalendar> calendar) {
        Bundle bundle = new Bundle();

        if(calendar != null) {
            // LinkedList to ArrayList 'conversion'
            ArrayList<StudentCalendar> list = new ArrayList<StudentCalendar>();
            list.addAll(calendar);

            bundle.putParcelableArrayList(CALENDAR_TAG, new ArrayList<Parcelable>(list));
        }

        return bundle;
    }

}