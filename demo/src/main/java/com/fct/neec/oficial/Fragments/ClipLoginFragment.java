package com.fct.neec.oficial.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.fct.neec.oficial.ClipRequests.enums.Result;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.tasks.ConnectClipTask;
import com.fct.neec.oficial.MainActivity;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.androidutils.AndroidUtils;
import com.google.android.material.tabs.TabLayout;

public class ClipLoginFragment extends Fragment implements ConnectClipTask.OnTaskFinishedListener<Result> {

    /**
     * Create a new instance of the fragment
     */

    private ConnectClipTask mTask;

    public static ClipLoginFragment newInstance(int index) {
        ClipLoginFragment fragment = new ClipLoginFragment();
        return fragment;
    }


    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cliplogin_fragment, container, false);

        final EditText nome = view.findViewById(R.id.nome);
        final EditText password = view.findViewById(R.id.password);
        Button login = view.findViewById(R.id.login);

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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get username and password text
                Editable editableUsername = nome.getText();
                String username = editableUsername != null ?
                        editableUsername.toString().trim() : null;
                Editable editablePassword = password.getText();
                String password = editablePassword != null ?
                        editablePassword.toString().trim() : null;

                // Check if the username field is not empty
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getContext(),getString(R.string.error_fields_required),Toast.LENGTH_LONG).show();
                }

                // Check if the password field is not empty
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(),getString(R.string.error_fields_required),Toast.LENGTH_LONG).show();
                }


                  //  showProgressSpinner(true);

                    // Start AsyncTask
                    mTask = new ConnectClipTask(getActivity(), ClipLoginFragment.this);
                    AndroidUtils.executeOnPool(mTask, username, password);


            }
        });

        
        return  view;
    }

    @Override
    public void onTaskFinished(Result result) {
        if(!isAdded())
            return;
        //showProgressSpinner(false);
        // If there was no errors, lets go to StudentNumbersActivity
        if(result == Result.SUCCESS) {
            //mudar de fragmento
            Toast.makeText(getContext(),"Sucesso!!" , Toast.LENGTH_LONG).show();
            ((MainActivity) getActivity()).changeFragment(6, false);
        }
        else {
            Toast.makeText(getContext(),"Login incorrecto" , Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //cancelTasks(mTask);
    }


}