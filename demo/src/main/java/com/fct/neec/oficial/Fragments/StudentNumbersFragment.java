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
import com.fct.neec.oficial.ClipRequests.entities.User;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentNumbersTask;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentYearsTask;
import com.fct.neec.oficial.ClipRequests.util.tasks.UpdateStudentNumbersTask;
import com.fct.neec.oficial.MainActivity;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.adapters.StudentNumbersAdapter;
import com.fct.neec.oficial.androidutils.AndroidUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class StudentNumbersFragment extends Fragment
        implements GetStudentNumbersTask.OnTaskFinishedListener,
        GetStudentYearsTask.OnTaskFinishedListener,
        UpdateStudentNumbersTask.OnUpdateTaskFinishedListener<User> {

    private StudentNumbersAdapter mListAdapter;
    private List<Student> students;
    ExpandableListView mListView;

    private GetStudentYearsTask mYearsTask;
    private UpdateStudentNumbersTask mUpdateTask;
    private GetStudentNumbersTask mNumbersTask;


    public static StudentNumbersFragment newInstance(int index) {
        StudentNumbersFragment fragment = new StudentNumbersFragment();
        return fragment;
    }


    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_numbers, container, false);
        mListView = view.findViewById(R.id.list_view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Unfinished task around?
        if ( ( mYearsTask != null && mYearsTask.getStatus() != AsyncTask.Status.FINISHED ) ||
                ( mUpdateTask != null && mUpdateTask.getStatus() != AsyncTask.Status.FINISHED ) )
          //  showProgressSpinnerOnly(true);

        // The view has been loaded already
        if(mListAdapter != null) {
            mListView.setAdapter(mListAdapter);
            mListView.setOnGroupClickListener(onGroupClickListener);
            mListView.setOnChildClickListener(onChildClickListener);
            return;
        }

       // showProgressSpinner(true);

        // Start AsyncTask
        mNumbersTask = new GetStudentNumbersTask(getActivity(), StudentNumbersFragment.this);
        AndroidUtils.executeOnPool(mNumbersTask);
    }




    ExpandableListView.OnGroupClickListener onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            Log.d( "CLIP" , "StudentNumbersFragment - group clicked");

            // If the yearsTask is running, do not allow group click
            if( mYearsTask != null && mYearsTask.getStatus() != AsyncTask.Status.FINISHED ) {
                System.out.println("YEARS TASK IS ALREADY RUNNING!");
                return true;
            }

            if(mListView.isGroupExpanded(groupPosition))
                mListView.collapseGroup(groupPosition);

            else {
              //  showProgressSpinnerOnly(true);

                System.out.println("GETSTUDENTS YEARS TASK student.getId() " + students.get(groupPosition).getId() +
                        ", student.getNumberId() " + students.get(groupPosition).getNumberId());

                mYearsTask = new GetStudentYearsTask(getActivity(), StudentNumbersFragment.this);
                AndroidUtils.executeOnPool(mYearsTask, students.get(groupPosition), groupPosition);
            }

            return true;
        }
    };

    ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Log.d( "CLIP" ,"StudentNumbersFragment - child clicked");

            // If the updateTask is running, do not allow child click
            if( mUpdateTask != null && mUpdateTask.getStatus() != AsyncTask.Status.FINISHED ) {
                System.out.println("UPDATE TASK IS RUNNING!");
                return true;
            }

            // Save year, studentId and studentNumberId selected
            String yearSelected = students.get(groupPosition).getYears().get(childPosition).getYear();

            ClipSettings.saveYearSelected(getActivity(), yearSelected);
            ClipSettings.saveStudentIdSelected(getActivity(), students.get(groupPosition).getId());
            ClipSettings.saveStudentNumberId(getActivity(), students.get(groupPosition).getNumberId());
            ClipSettings.saveStudentYearSemesterIdSelected(getActivity(), students.get(groupPosition)
                    .getYears().get(childPosition).getId());

            // Lets go to NavDrawerActivity
            //TODO


            return true;
        }
    };



    @Override
    public void onStudentNumbersTaskFinished(User result) {
        if(!isAdded())
            return;

        Log.d( "CLIP" ,"StudentNumbersFragment - onStudentNumbersTaskFinished");

        students = result.getStudents();
        //showProgressSpinner(false);

        mListAdapter = new StudentNumbersAdapter(getActivity(), this.students);
        mListView.setAdapter(mListAdapter);
        mListView.setOnGroupClickListener(onGroupClickListener);
        mListView.setOnChildClickListener(onChildClickListener);
    }

    @Override
    public void onStudentYearsTaskFinished(Student result, int groupPosition) {
        if(!isAdded())
            return;

        Log.d( "CLIP" ,"StudentNumbersFragment - onStudentYearsTaskFinished");

       // showProgressSpinnerOnly(false);

        // Server is unavailable right now
        if(result == null)
            return;

        // Set new data and notifyDataSetChanged
        students.get(groupPosition).setYears(result.getYears());
        mListAdapter.notifyDataSetChanged();

        // Expand group position
        mListView.expandGroup(groupPosition, true);
    }

    @Override
    public void onUpdateTaskFinished(User result) {
        if(!isAdded())
            return;

        Log.d( "CLIP" ,"StudentNumbersFragment - onUpdateTaskFinished");

        // Server is unavailable right now
        if(result == null)
            return;

        // Set new data and notifyDataSetChanged
        students.clear();
        students.addAll(result.getStudents());
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelTasks(mYearsTask);
        cancelTasks(mUpdateTask);
        cancelTasks(mNumbersTask);
    }

    protected void cancelTasks(AsyncTask mTask) {
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED)
            mTask.cancel(true);
    }
}