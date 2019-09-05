package com.fct.neec.oficial.Fragments.Calendario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentCalendarTask;
import com.fct.neec.oficial.Fragments.Horario.BaseViewPager;
import com.fct.neec.oficial.Fragments.Horario.DepthPageTransformer;
import com.fct.neec.oficial.Fragments.Horario.ScheduleViewPager;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.adapters.CalendarViewPagerAdapter;
import com.fct.neec.oficial.androidutils.AndroidUtils;

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
        mViewPager.setAdapter(new CalendarViewPagerAdapter(getChildFragmentManager(),
                getResources().getStringArray(R.array.exams_tests_tab_array), result));
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelTasks(mTask);
    }

}