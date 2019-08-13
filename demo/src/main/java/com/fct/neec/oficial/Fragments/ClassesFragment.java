package com.fct.neec.oficial.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.entities.StudentClass;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentClassesTask;
import com.fct.neec.oficial.MainActivity;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.adapters.ClassListViewAdapter;
import com.fct.neec.oficial.androidutils.AndroidUtils;

import java.util.List;

public class ClassesFragment extends Fragment
        implements GetStudentClassesTask.OnTaskFinishedListener<Student> {

    private ListView mListView;
    private GetStudentClassesTask mTask;
    private ClassListViewAdapter adapter;


    public static ClassesFragment newInstance(int index) {
        ClassesFragment fragment = new ClassesFragment();
        return fragment;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classes, container, false);
        mListView = (ListView) view.findViewById(R.id.list_view);

        // Show progress spinner
       // showProgressSpinner(true);

        // Start AsyncTask
        mTask = new GetStudentClassesTask(getActivity(), ClassesFragment.this);
        AndroidUtils.executeOnPool(mTask);

        return view;
    }

    @Override
    public void onTaskFinished(Student result) {
        if(!isAdded())
            return;

       // showProgressSpinner(false);

        // Server is unavailable right now
        if(result == null) return;

        mListView.setAdapter(getAdapterItems(result));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) adapter.getItem(position);

                // Save class selected and internal classId
                ClipSettings.saveStudentClassSelected(getActivity(), item.number);
                ClipSettings.saveStudentClassIdSelected(getActivity(), item.id);

                ((MainActivity) getActivity()).changeFragment(9, false);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelTasks(mTask);
    }

    private ClassListViewAdapter getAdapterItems(Student result) {
        adapter = new ClassListViewAdapter(getActivity());

        int semester = ClipSettings.getSemesterSelected(getActivity());
        if (result.getClasses() == null || result.getClasses().get(semester) == null)
            return adapter;

        List<StudentClass> classes = result.getClasses().get(semester);
        for (StudentClass c : classes)
            adapter.add(new ListViewItem(c.getId(), c.getName(), c.getNumber()));

        return adapter;
    }

    public static class ListViewItem {
        public String id, name, number;

        public ListViewItem(String id, String name, String number) {
            this.id = id;
            this.name = name;
            this.number = number;
        }
    }

    protected void cancelTasks(AsyncTask mTask) {
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED)
            mTask.cancel(true);
    }

}