package com.fct.neec.oficial.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.entities.StudentClassDoc;
import com.fct.neec.oficial.ClipRequests.network.StudentClassesDocsRequest;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentClassesDocsTask;
import com.fct.neec.oficial.MainActivity;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.adapters.StudentClassesDocsAdapter;
import com.fct.neec.oficial.androidutils.AndroidUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.LinkedList;
import java.util.List;

public class ClassesDocsFragment extends Fragment
        implements GetStudentClassesDocsTask.OnTaskFinishedListener {

    public static final String FRAGMENT_TAG = "classes_docs_tag";
    private int lastExpandedGroupPosition;
    private ExpandableListView mListView;
    private StudentClassesDocsAdapter mListAdapter;
    private List<StudentClassDoc> classDocs;
    ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            String name = classDocs.get(childPosition).getName();
            String url = classDocs.get(childPosition).getUrl();

            // Download document
            StudentClassesDocsRequest.downloadDoc(getActivity(), name, url);

            return true;
        }
    };
    private GetStudentClassesDocsTask mDocsTask;
    ExpandableListView.OnGroupClickListener onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            if (mListView.isGroupExpanded(groupPosition))
                mListView.collapseGroup(groupPosition);

            else {
                // showProgressSpinnerOnly(true);

                mDocsTask = new GetStudentClassesDocsTask(getActivity(), ClassesDocsFragment.this);
                AndroidUtils.executeOnPool(mDocsTask, groupPosition);
            }

            return true;
        }
    };

    public static ClassesDocsFragment newInstance(int index) {
        ClassesDocsFragment fragment = new ClassesDocsFragment();
        return fragment;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lastExpandedGroupPosition = -1;
        classDocs = new LinkedList<StudentClassDoc>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_classes_docs, container, false);

        mListView = (ExpandableListView) view.findViewById(R.id.list_view);

        //Tab
        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TAB", "clicou em: " + tab.getPosition());
                if (tab.getPosition() == 0) {
                    ((MainActivity) getActivity()).changeFragment(1, false);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListAdapter = new StudentClassesDocsAdapter(getActivity(),
                getResources().getStringArray(R.array.classes_docs_array), classDocs);
        mListView.setAdapter(mListAdapter);
        mListView.setOnGroupClickListener(onGroupClickListener);
        mListView.setOnChildClickListener(onChildClickListener);

        // Unfinished task around?
        // if (mDocsTask != null && mDocsTask.getStatus() != AsyncTask.Status.FINISHED)
        // showProgressSpinnerOnly(true);

        /*// The view has been loaded already
        if(mListAdapter != null) {
            mListView.setAdapter(mListAdapter);
            mListView.setOnGroupClickListener(onGroupClickListener);
            mListView.setOnChildClickListener(onChildClickListener);
            return;
        }*/
    }

    @Override
    public void onTaskFinished(Student result, int groupPosition) {
        if (!isAdded())
            return;

        //showProgressSpinnerOnly(false);

        // Server is unavailable right now
        if (result == null || result.getClassesDocs().size() == 0)
            return;

        // Collapse last expanded group
        if (lastExpandedGroupPosition != -1)
            mListView.collapseGroup(lastExpandedGroupPosition);

        lastExpandedGroupPosition = groupPosition;

        // Set new data and notify adapter
        classDocs.clear();
        classDocs.addAll(result.getClassesDocs());
        mListAdapter.notifyDataSetChanged();

        // Expand group position
        mListView.expandGroup(groupPosition, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelTasks(mDocsTask);
    }

    protected void cancelTasks(AsyncTask mTask) {
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED)
            mTask.cancel(true);
    }
}