package com.fct.neec.oficial.Fragments.Calendario;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.fct.neec.oficial.ClipRequests.entities.StudentCalendar;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.adapters.CalendarViewPagerAdapter;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CalendarFragment extends Fragment implements CalendarPickerView.OnDateSelectedListener {

    public static final String APPOINTMENT_TAG = "appointment_tag";
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");;
    private List<StudentCalendar> calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = getArguments().getParcelableArrayList(CalendarViewPagerAdapter.CALENDAR_TAG);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        CalendarPickerView calendar = (CalendarPickerView) view.findViewById(R.id.calendar_view);


        // Set calendar background color
        Resources resources = getActivity().getResources();
        calendar.setBackgroundColor(Color.parseColor("#ff99cc00"));
        // Set calendar start/end date and highlight dates
        calendar.init(ClipSettings.getSemesterStartDate(getActivity()),
                ClipSettings.getSemesterEndDate(getActivity()))
                .withHighlightedDates(getDatesToHighlight());

        calendar.setOnDateSelectedListener(this);

        return view;
    }

    @Override
    public void onDateSelected(Date date) {
        if(this.calendar == null)
            return;

        Log.d("Calendario" , "Abrir input ??");
    }

    @Override
    public void onDateUnselected(Date date) {}

    private List<Date> getDatesToHighlight() {
        List<Date> dates = new LinkedList<Date>();

        if(calendar == null)
            return dates;

        for (StudentCalendar appointment : calendar) {
            try {
                Date date = format.parse(appointment.getDate());
                dates.add(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return dates;
    }
}