package ca.qc.bdeb.c5gm.planistage;

import android.graphics.RectF;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    private WeekView mWeekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        // Get a reference for the week view in the layout.
        mWeekView = findViewById(R.id.weekView);

// Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                Toast.makeText(getApplicationContext(), "EventClick", Toast.LENGTH_SHORT).show();
            }
        });

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                // List<WeekViewEvent> events = getEvents(newYear, newMonth);
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
                return events;
            }
        });

// Set long press listener for events.
        mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
                Toast.makeText(getApplicationContext(), "onEventLongPress", Toast.LENGTH_SHORT).show();

            }
        });
    }
}