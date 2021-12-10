package ca.qc.bdeb.c5gm.planistage;

import android.graphics.RectF;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.qc.bdeb.c5gm.planistage.data.Stage;
import ca.qc.bdeb.c5gm.planistage.data.StageDB;

public class WeekActivity extends AppCompatActivity {

    private WeekView mWeekView;
    private int i=0;
    private StageDB DB;
    private ArrayList<Stage> Stages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        Stages=new ArrayList<Stage>();
        DB=DB.getInstance(this);
        Stages=DB.getTousLesStages();
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
                // Code pris du git de alamkanak
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
                //Pour que les noms se montrent une fois on fait un if statement
                if(i<1){
                    events = addAllMeetings(events);
                    i++;
                }
                return events;
            }

            private List<WeekViewEvent> addAllMeetings(List<WeekViewEvent> events) {
                for (int i = 0; i < Stages.size(); i++) {
                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.DAY_OF_MONTH, i);
                    startTime.set(Calendar.HOUR_OF_DAY, 1);
                    startTime.set(Calendar.MINUTE, 0);
                    startTime.set(Calendar.MONTH, 11);//0 = january 11=december
                    startTime.set(Calendar.YEAR, 2021);
                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.set(Calendar.DAY_OF_MONTH, i);
                    endTime.set(Calendar.HOUR_OF_DAY, 5);
                    endTime.set(Calendar.MINUTE, 0);
                    endTime.set(Calendar.MONTH, 11);
                    endTime.set(Calendar.YEAR, 2021);

                    WeekViewEvent event = new WeekViewEvent(0, Stages.get(i).getEtudiant().toString(), startTime, endTime);
                    event.setColor(R.color.orange);
                    events.add(event);
                }
                return  events;
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