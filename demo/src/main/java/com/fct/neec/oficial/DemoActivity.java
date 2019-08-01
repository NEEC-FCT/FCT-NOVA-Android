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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import com.fct.neec.AHBottomNavigation;
import com.fct.neec.AHBottomNavigationAdapter;
import com.fct.neec.AHBottomNavigationItem;
import com.fct.neec.AHBottomNavigationViewPager;
import com.fct.neec.notification.AHNotification;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class DemoActivity extends AppCompatActivity {

    private Fragment currentFragment;
    private DemoViewPagerAdapter adapter;
    private AHBottomNavigationAdapter navigationAdapter;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    private boolean useMenuResource = true;
    private int[] tabColors;
    private Handler handler = new Handler();

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
            Intent intent = new Intent(DemoActivity.this, MyIntro.class);
            DemoActivity.this.startActivity(intent);

        }
        setContentView(R.layout.activity_home);
        initUI();
    }


    public void changeFragment(int position){
        initUI();
        viewPager.setCurrentItem(position, false);
        bottomNavigation.setCurrentItem(position);
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

        if (useMenuResource) {
            tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
            navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
            navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
        } else {
            AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_apps_black_24dp, R.color.color_tab_1);
            AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_maps_local_bar, R.color.color_tab_2);
            AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_maps_local_restaurant, R.color.color_tab_3);

            bottomNavigationItems.add(item1);
            bottomNavigationItems.add(item2);
            bottomNavigationItems.add(item3);

            bottomNavigation.addItems(bottomNavigationItems);
        }

        bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setColored(true);
        bottomNavigation.setCurrentItem(2);
        updateBottomNavigationItems(true);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (!isInternetAvailable()) {
                    Intent intent = new Intent(DemoActivity.this, SemNet.class);
                    startActivity(intent);
                }

                if(position == 0){
                    //Sugestoes
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Setting custom colors for notification
                /*
				AHNotification notification = new AHNotification.Builder()
						.setText(":)")
						.setBackgroundColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_back))
						.setTextColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_text))
						.build();
				bottomNavigation.setNotification(notification, 0);*/
                            Snackbar.make(bottomNavigation, "Tens alguma sugestão ? \n"
                                            +"Envia um email para geral@neec-fct.com",
                                    Snackbar.LENGTH_SHORT).setDuration(5000).show();

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

                return true;
            }
        });


        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                Log.d("DemoActivity", "BottomNavigation Position: " + y);

            }
        });


        viewPager.setOffscreenPageLimit(4);
        adapter = new DemoViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        currentFragment = adapter.getCurrentFragment();






		//Verifica se foi redirect
        SharedPreferences sharedPrefs = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        if(sharedPrefs.contains("Separador")){

            //Obtem valor
            int position = sharedPrefs.getInt("Separador", 2);
            //apaga
            editor.remove("Separador");
            editor.apply();
            viewPager.setCurrentItem(position, false);
            bottomNavigation.setCurrentItem(position);
        }
        else {
            viewPager.setCurrentItem(2, false);
        }

        bottomNavigation.setDefaultBackgroundResource(R.drawable.bottom_navigation_background);
        bottomNavigation.setSelectedBackgroundVisible(true);

    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent k = new Intent(DemoActivity.this, MenuPrincipal.class);
        startActivity(k);
    }

    /**
     * Add or remove items of the bottom navigation
     */
    public void updateBottomNavigationItems(boolean addItems) {

        if (useMenuResource) {
            if (addItems) {
                navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_5);
                navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
            } else {
                navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
                navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
            }

        } else {
            if (addItems) {
                AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.tab_4),
                        ContextCompat.getDrawable(this, R.drawable.ic_maps_local_bar),
                        ContextCompat.getColor(this, R.color.color_tab_4));
                AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.tab_5),
                        ContextCompat.getDrawable(this, R.drawable.map_25),
                        ContextCompat.getColor(this, R.color.color_tab_5));

                bottomNavigation.addItem(item4);
                bottomNavigation.addItem(item5);
            } else {
                bottomNavigation.removeAllItems();
                bottomNavigation.addItems(bottomNavigationItems);
            }
        }
    }


}
