package com.fct.neec.oficial.Fragments.Horario;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.fct.neec.oficial.R;

public class BaseViewPager extends Fragment {


    protected View view;
    public ViewPager mViewPager;
    public SwipeRefreshLayout sw_refresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);
        mViewPager = view.findViewById(R.id.view_pager);

        // Show progress spinner
        showProgressSpinnerOnly(true);

        return view;
    }

    /**
     * Shows the progress spinner
     */
    protected void showProgressSpinnerOnly(final boolean show) {
        // mProgressSpinner.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    protected void cancelTasks(AsyncTask mTask) {
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED)
            mTask.cancel(true);
    }

}