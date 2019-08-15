package com.fct.neec.oficial.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentScheduleTask;
import com.fct.neec.oficial.MainActivity;
import com.fct.neec.oficial.ProximaAula;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.adapters.ScheduleViewPagerAdapter;
import com.fct.neec.oficial.androidutils.AndroidUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class ScheduleViewPager extends BaseViewPager
        implements GetStudentScheduleTask.OnTaskFinishedListener<Student> {

    private GetStudentScheduleTask mTask;

    /**
     * Create a new instance of the fragment
     */
    public static ScheduleViewPager newInstance(int index) {
        ScheduleViewPager fragment = new ScheduleViewPager();
        return fragment;
    }


    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);

        // Start AsyncTask
        mTask = new GetStudentScheduleTask(getActivity(), ScheduleViewPager.this);
        AndroidUtils.executeOnPool(mTask);

        return view;
    }

    @Override
    public void onTaskFinished(Student result) {
        if(!isAdded())
            return;

        showProgressSpinnerOnly(false);

        // Server is unavailable right now
        if(result == null) return;

        ProximaAula.sethorario(result);
        // Initialize the ViewPager and set the adapter
        mViewPager.setAdapter(new ScheduleViewPagerAdapter(getChildFragmentManager(),
                getResources().getStringArray(R.array.schedule_tab_array), result));
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs =  view.findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        //vou para tab do dia atual
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
        if( dayOfWeek >= 0 && dayOfWeek <= 4){
            Log.d("CLIP" , "Vou para a tab: " +dayOfWeek);
            mViewPager.setCurrentItem(dayOfWeek);
        }


        //tabs superiores
        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TAB", "clicou em: " + tab.getPosition());
                if (tab.getPosition() == 0) {
                    ((MainActivity) getActivity()).changeFragment(3 , false);
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