package com.fct.neec.oficial.Fragments.Calendario;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentCalendarTask;
import com.fct.neec.oficial.MainActivity;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.adapters.CalendarViewPagerAdapter;
import com.fct.neec.oficial.androidutils.AndroidUtils;
import com.google.android.material.tabs.TabLayout;

public class CalendarViewPager extends BaseViewPager
        implements GetStudentCalendarTask.OnTaskFinishedListener<Student> {
    private GetStudentCalendarTask mTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);

        // Start AsyncTask
        mTask = new GetStudentCalendarTask(getActivity(), CalendarViewPager.this);
        AndroidUtils.executeOnPool(mTask);

        return view;
    }

    public static CalendarViewPager newInstance(int index) {
        CalendarViewPager fragment = new CalendarViewPager();
        return fragment;
    }

    @Override
    public void onTaskFinished(Student result) {
        if(!isAdded())
            return;

        showProgressSpinnerOnly(false);

        // Server is unavailable right now
        if(result == null) return;

        // Initialize the ViewPager and set an adapter
        try {
            mViewPager.setAdapter(new CalendarViewPagerAdapter(getChildFragmentManager(),
                    getResources().getStringArray(R.array.exams_tests_tab_array), result));
            mViewPager.setPageTransformer(true, new DepthPageTransformer());


            // Bind the tabs to the ViewPager
            PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
            tabs.setTextColor(Color.WHITE);
            tabs.setIndicatorColor(Color.WHITE);
            tabs.setShouldExpand(true);
            tabs.setViewPager(mViewPager);

        }
        catch (Exception e){

        }
        //tabs superiores
        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(2);
        tab.select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TAB", "clicou em: " + tab.getPosition());
                if (tab.getPosition() == 0) {
                    ((MainActivity) getActivity()).changeFragment(3, false);
                }
                else if (tab.getPosition() == 1) {
                    ((MainActivity) getActivity()).changeFragment(7, false);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelTasks(mTask);
    }

}