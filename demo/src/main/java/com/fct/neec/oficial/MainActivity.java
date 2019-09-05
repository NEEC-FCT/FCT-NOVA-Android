package com.fct.neec.oficial;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.fct.neec.AHBottomNavigation;
import com.fct.neec.AHBottomNavigationAdapter;
import com.fct.neec.AHBottomNavigationViewPager;
import com.fct.neec.oficial.RegrasSegurança.MyIntro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;
    private MainViewPagerAdapter adapter;
    private AHBottomNavigationAdapter navigationAdapter;
    private int[] tabColors;
    private Handler handler = new Handler();
    private Boolean show = true;

    // UI
    private AHBottomNavigationViewPager viewPager;
    private AHBottomNavigation bottomNavigation;
    private FloatingActionButton floatingActionButton;

    //internet
    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean enabledTranslucentNavigation = getSharedPreferences("shared", Context.MODE_PRIVATE)
                .getBoolean("translucentNavigation", false);
        setTheme(enabledTranslucentNavigation ? R.style.AppTheme_TranslucentNavigation : R.style.AppTheme);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        int idName = pref.getInt("intro", 0);
        if (idName == 0) {
            Intent intent = new Intent(MainActivity.this, MyIntro.class);
            MainActivity.this.startActivity(intent);

        }
        setContentView(R.layout.activity_home);
        initUI();
    }


    public Context getContext() {
        return getContext();
    }

    public void changeFragment(int position, boolean initUI) {

        // if(initUI)
        initUI();

        if (position >= 5 && position <= 7) {
            bottomNavigation.setCurrentItem(3);
            viewPager.setCurrentItem(position, false);


        } else if (position >= 8) {
            bottomNavigation.setCurrentItem(1);
            viewPager.setCurrentItem(position, false);
        } else {
            viewPager.setCurrentItem(position, false);
            bottomNavigation.setCurrentItem(position);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * Init UI
     */
    private void initUI() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }


        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);
        floatingActionButton = findViewById(R.id.floating_action_button);


        tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
        navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_5);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);


        bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setColored(true);
        bottomNavigation.setCurrentItem(2);
        updateBottomNavigationItems(true);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (!isInternetAvailable()) {
                    Intent intent = new Intent(MainActivity.this, SemNet.class);
                    startActivity(intent);
                }

                if(position == 0){
                    Intent k = new Intent(MainActivity.this, MenuFCT.class);
                    startActivity(k);
                }else{
                if (position == 0 && show) {
                    //Sugestoes
                    show = false;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            SharedPreferences sp = getSharedPreferences("SHOW", MainActivity.MODE_PRIVATE);
                            if(   sp.getBoolean("HELP", true)){
                                Snackbar.make(bottomNavigation, "Tens alguma sugestão ? \n"
                                                + "Envia um email para geral@neec-fct.com",
                                        Snackbar.LENGTH_SHORT).setDuration(4000).show();
                                //salvar
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("HELP", false);
                                editor.commit();
                            }

                        }
                    }, 1500);
                }

                if (currentFragment == null) {
                    currentFragment = adapter.getCurrentFragment();
                }

                if (position == 4) {
                    currentFragment = adapter.getItem(position);

                }



                 viewPager.setCurrentItem(position, false);

                if (currentFragment == null) {
                    return true;
                }

                //animacoes tab calendario 1/2
                if (currentFragment instanceof CalendarioFragment && !wasSelected) {
                    ((CalendarioFragment) currentFragment).willBeHidden();
                }

                currentFragment = adapter.getCurrentFragment();

                //animacoes na tab calendario 2/2
                if (currentFragment instanceof CalendarioFragment) {
                    if (wasSelected) {
                        ((CalendarioFragment) currentFragment).refresh();
                    } else {
                        ((CalendarioFragment) currentFragment).willBeDisplayed();
                    }
                }


                Log.d("index", "clicou em " + position);


                if (position == 1) {

                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setAlpha(0f);
                    floatingActionButton.setScaleX(0f);
                    floatingActionButton.setScaleY(0f);
                    floatingActionButton.animate()
                            .alpha(1)
                            .scaleX(1)
                            .scaleY(1)
                            .setDuration(300)
                            .setInterpolator(new OvershootInterpolator())
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    floatingActionButton.animate()
                                            .setInterpolator(new LinearOutSlowInInterpolator())
                                            .start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .start();

                } else {
                    if (floatingActionButton.getVisibility() == View.VISIBLE) {
                        floatingActionButton.animate()
                                .alpha(0)
                                .scaleX(0)
                                .scaleY(0)
                                .setDuration(300)
                                .setInterpolator(new LinearOutSlowInInterpolator())
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        floatingActionButton.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                        floatingActionButton.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .start();
                    }
                }
                }
                return true;
            }
        });


        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                Log.d("MainActivity", "BottomNavigation Position: " + y);

            }
        });


        viewPager.setOffscreenPageLimit(4);
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        currentFragment = adapter.getCurrentFragment();


        //Verifica se foi redirect
        SharedPreferences sharedPrefs = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        if (sharedPrefs.contains("Separador")) {

            //Obtem valor
            int position = sharedPrefs.getInt("Separador", 2);
            //apaga
            editor.remove("Separador");
            editor.apply();
            if(position == 7 || position == 10){
                Log.d("Changed" , "Vou para o 3 ou 10");
                bottomNavigation.setCurrentItem(3);
            }
            else if(position == 9){
                Log.d("Changed" , "Vou para o 1");
                bottomNavigation.setCurrentItem(1);
            }
            else{
                bottomNavigation.setCurrentItem(position);
            }
            viewPager.setCurrentItem(position, false);

        } else {
            viewPager.setCurrentItem(2, false);
        }

        bottomNavigation.setDefaultBackgroundResource(R.drawable.bottom_navigation_background);
        bottomNavigation.setSelectedBackgroundVisible(true);

    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent k = new Intent(MainActivity.this, MenuPrincipal.class);
        startActivity(k);
    }

    /**
     * Add or remove items of the bottom navigation
     */
    public void updateBottomNavigationItems(boolean addItems) {



            navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_5);
            navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);



    }


    public void reloadFragment(int i) {
        if (i == 7) {
            adapter.reload(i);
            changeFragment(7, false);

        }

    }
}
