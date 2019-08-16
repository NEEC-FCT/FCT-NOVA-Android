package com.fct.neec.oficial.Fragments.Horario;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.astuetz.PagerSlidingTabStrip;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentScheduleTask;
import com.fct.neec.oficial.ClipRequests.util.tasks.UpdateStudentPageTask;
import com.fct.neec.oficial.MainActivity;
import com.fct.neec.oficial.ProximaAula;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.adapters.ScheduleViewPagerAdapter;
import com.fct.neec.oficial.androidutils.AndroidUtils;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class ScheduleViewPager extends BaseViewPager
        implements GetStudentScheduleTask.OnTaskFinishedListener<Student>,
        UpdateStudentPageTask.OnUpdateTaskFinishedListener<Student> {

    public Student resultado;
    public static boolean update;
    private GetStudentScheduleTask mTask;
    private UpdateStudentPageTask mUpdateTask;

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
        if (!isAdded())
            return;


        showProgressSpinnerOnly(false);

        // Server is unavailable right now
        if (result == null) return;

        this.resultado = result;
        ProximaAula.sethorario(result);
        ProximaAula.SaveState();
        // Initialize the ViewPager and set the adapter
        mViewPager.setAdapter(new ScheduleViewPagerAdapter(getChildFragmentManager(),
                getResources().getStringArray(R.array.schedule_tab_array), this.resultado));
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = view.findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        //vou atualizar a view
        if(update){
            update = false;
            Log.d("CLIP" , "Atualiza view");
            mViewPager.getAdapter().notifyDataSetChanged();
            ProximaAula.sethorario(result);
            ProximaAula.SaveState();
        }

        //vou para tab do dia atual
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
        if (dayOfWeek >= 0 && dayOfWeek <= 4) {
            Log.d("CLIP", "Vou para a tab: " + dayOfWeek);
            mViewPager.setCurrentItem(dayOfWeek);
        }

        //FAB listeners
        //Semestre
        FloatingActionButton Semestre = view.findViewById(R.id.semestre);
        Semestre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] semestres = {"1º Semestre", "2º Semestre", "2º Trimestre"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Escolha um semestre");
                builder.setItems(semestres, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if (which == 0)
                            ClipSettings.saveSemesterSelected(getContext(), 1);
                        else if (which == 1)
                            ClipSettings.saveSemesterSelected(getContext(), 2);
                        else
                            ClipSettings.saveSemesterSelected(getContext(), 3);
                        //atualizer
                        mUpdateTask = new UpdateStudentPageTask(getContext(), ScheduleViewPager.this);
                        AndroidUtils.executeOnPool(mUpdateTask);
                    }
                });
                builder.show();
            }
        });

        //ano
        FloatingActionButton cadeiras = view.findViewById(R.id.cadeiras);
        cadeiras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CLIP", "Ver cadeiras");
                // Clear user personal data
                ((MainActivity) getActivity()).changeFragment(8, false);
            }
        });


        //ano
        FloatingActionButton ano = view.findViewById(R.id.ano);
        ano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CLIP", "Mudar ano");
                // Clear user personal data
                ((MainActivity) getActivity()).changeFragment(6, false);
            }
        });

        //Logout
        FloatingActionButton Logout = view.findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CLIP", "Logout");
                // Clear user personal data
                ClipSettings.logoutUser(getContext());
                ((MainActivity) getActivity()).changeFragment(3, false);
            }
        });


        //tabs superiores
        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TAB", "clicou em: " + tab.getPosition());
                if (tab.getPosition() == 0) {
                    ((MainActivity) getActivity()).changeFragment(3, false);
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
    public void onUpdateTaskFinished(Student result) {

        // Refresh current view
        ProximaAula.sethorario(result);
        ProximaAula.SaveState();
        ((MainActivity) getActivity()).reloadFragment(7);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTasks(mTask);
        cancelTasks(mUpdateTask);
    }
}