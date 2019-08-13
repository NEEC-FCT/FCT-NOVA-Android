package com.fct.neec.oficial.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentScheduleTask;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.adapters.ScheduleViewPagerAdapter;
import com.fct.neec.oficial.androidutils.AndroidUtils;

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

        // Initialize the ViewPager and set the adapter
        mViewPager.setAdapter(new ScheduleViewPagerAdapter(getChildFragmentManager(),
                getResources().getStringArray(R.array.schedule_tab_array), result));
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        // Bind the tabs to the ViewPager
     //   PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
     //   tabs.setViewPager(mViewPager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelTasks(mTask);
    }
}