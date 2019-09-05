package com.fct.neec.oficial;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class CalendarioFragment extends Fragment implements CalendarioAdapter.eventoListener {

    private TextView monthText;
    private CompactCalendarView calendar;
    private LinearLayout linearContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Event> itemsData = new ArrayList<>();
    private boolean sc = false;
    private SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    private RecyclerView.OnScrollListener on_scroll = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            try {
                int pos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                long time = itemsData.get(pos).getTimeInMillis();
                if (time > 0 && !calendar.isAnimating() && !sc) {
                    calendar.setCurrentDate(new Date(time));
                    setMonthText();
                }
            } catch (Exception ignored) {

            }
        }


    };

    /**
     * Create a new instance of the fragment
     */
    static CalendarioFragment newInstance(int index) {
        CalendarioFragment fragment = new CalendarioFragment();

        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);

        return fragment;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (!isInternetAvailable()) {
            Intent intent = new Intent(getContext(), SemNet.class);
            startActivity(intent);
        }

        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                Log.d("TAB", "clicou em: " + tab.getPosition());
                if(tab.getPosition() == 2){
                    ((MainActivity) getActivity()).changeFragment(10, false);
                }
                else if (tab.getPosition() == 1) {
                    //mudar de fragmento
                    //call back after permission granted
                    PermissionListener permissionlistener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            CLIP();
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            TabLayout.Tab tab = tabLayout.getTabAt(0);
                            tab.select();
                        }

                    };

                    //check all needed permissions together
                    TedPermission.with(getContext())
                            .setPermissionListener(permissionlistener)
                            .setDeniedMessage("Se recusar não poderá usar o CLIP\n" +
                                    "\n" +
                                    "Por favor vá a [Definições] > [Permissões]")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .check();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        //Calendario
        linearContainer = view.findViewById(R.id.linear);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        monthText = view.findViewById(R.id.monthText);

        calendar = view.findViewById(R.id.calendar);
        calendar.setUseThreeLetterAbbreviation(true);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setEventIndicatorStyle(3);
        calendar.shouldDrawIndicatorsBelowSelectedDays(true);
        calendar.setCurrentDayIndicatorStyle(2);
        calendar.setCurrentSelectedDayIndicatorStyle(2);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    Log.d("Scrool", "1");
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    Log.d("Scrool", "2");
                    sc = false;
                } else {
                    Log.d("Scrool", "3");
                    sc = false;

                }
            }
        });

        recyclerView.addOnScrollListener(on_scroll);


        calendar.setAnimationListener(new CompactCalendarView.CompactCalendarAnimationListener() {
            @Override
            public void onOpened() {

                refresh();
                Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                if (recyclerView != null) {
                    recyclerView.startAnimation(fadeIn);
                    recyclerView.setAlpha(1);
                }
            }

            @Override
            public void onClosed() {

            }
        });

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                sc = true;
                scrollToDate(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setMonthText();
                sc = true;
                scrollToDate(firstDayOfNewMonth);
            }
        });


        setMonthText();
        getEvents();

        return view;

    }

    private void CLIP() {
        if (ClipSettings.getYearSelected(getContext()) != null) {
            Log.d("CLIP", "Vai para o horario");
            ((MainActivity) getActivity()).changeFragment(7, false);
        }
        // If the user has already login, start the StudentNumbersActivity instead
        else if (ClipSettings.isUserLoggedIn(getContext())) {
            Log.d("CLIP", "ConnectClipActivity - user has already login");
            ((MainActivity) getActivity()).changeFragment(6, false);
        } else {
            ((MainActivity) getActivity()).changeFragment(5, false);
        }
    }

    private void getEvents() {
        Log.d("Eventos", "Start");
        String url = "https://fctapp.neec-fct.com/calendar.php";
        if (getContext() == null)
            return;
        RequestQueue queue = Volley.newRequestQueue(getContext());


        itemsData.clear();

        itemsData.add(new Event(0, 0, "A carregar calendário..."));
        refreshAdapter();
//ParseNetwork response para não bloquear a main thread
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Eventos", "Recv");
                        //Log.d("Response is: ", response);
                        try {
                            JSONArray array = new JSONArray(response);
                            Log.d("length", "" + array.length());
                            itemsData.clear();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                int color = Color.parseColor(obj.getString("color"));
                                long time = dateFormat.parse(obj.getString("time")).getTime();
                                String name = obj.getString("name");
                                Event evt = new Event(color, time, name);
                                itemsData.add(evt);
                            }

                        } catch (JSONException e) {
                            Log.e("json", e.getMessage());

                            itemsData.clear();

                            itemsData.add(new Event(0, 0, "Ocorreu um erro a processar o calendário(1)."));
                        } catch (ParseException e) {

                            Log.e("json", e.getMessage());

                            itemsData.clear();

                            itemsData.add(new Event(0, 0, "Ocorreu um erro a processar o calendário(2)."));
                            e.printStackTrace();
                        } finally {

                            Log.d("Eventos", "Added");
                            refreshAdapter();
                            refresh();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            /*   if (error != null)
                  Log.e("Eventos", error.getMessage());
                itemsData.clear();
                itemsData.add(new Event(0, 0, "Ocorreu um erro a carregar o calendário,tente novamente mais tarde"));
                refreshAdapter();
                refresh();*/
            }
        });

        Log.d("Eventos", "Q");
        queue.add(stringRequest);


    }

    private void refreshAdapter() {
        CalendarioAdapter adapter = new CalendarioAdapter(itemsData, this);
        recyclerView.setAdapter(adapter);
        calendar.removeAllEvents();
        calendar.addEvents(itemsData);
    }


    private void setMonthText() {

        if (monthText != null && calendar != null) {
            try {
                Date day = calendar.getFirstDayOfCurrentMonth();
                String string = df.format(day);
                monthText.setText(string);
            } catch (Exception e) {
                Log.e("monthText", e.getMessage());
            }
        }
    }

    private void scrollToDate(Date dateClicked) {
        if (getContext() == null)
            return;
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(dateClicked.getTime());
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        int i;
        for (i = 0; i < itemsData.size(); i++) {
            if (itemsData.get(i).getTimeInMillis() >= date.getTimeInMillis())
                break;
        }
        smoothScroller.setTargetPosition(i);
        layoutManager.startSmoothScroll(smoothScroller);
    }

    /**
     * Refresh
     */
    void refresh() {
        Date date = new Date();
        if (calendar != null) {
            sc = true;
            calendar.setCurrentDate(date);
            scrollToDate(date);
            setMonthText();
        }

    }

    /**
     * Called when a fragment will be displayed
     */
    void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (linearContainer != null) {
            recyclerView.setAlpha(0);
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            linearContainer.startAnimation(fadeIn);
            if (calendar != null) {
                refresh();
                calendar.showCalendarWithAnimation();
            }
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    void willBeHidden() {
        if (linearContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            linearContainer.startAnimation(fadeOut);

        }
    }

    @Override
    public void onEventoClick(int pos) {
        long time = itemsData.get(pos).getTimeInMillis();
        if (time > 0) {
            calendar.setCurrentDate(new Date(time));
            setMonthText();
        }
    }
}
