package ca.qc.bdeb.c5gm.planistage;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
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
    private boolean[][] timeSlots = new boolean[3][600];
    private int visite=30;

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
                    for(int x=0;x<10;x++){
                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.DAY_OF_MONTH, 8+x*7);
                        startTime.set(Calendar.HOUR_OF_DAY, 8+i);//Commence à 8h et incremente
                        startTime.set(Calendar.MINUTE, 0);
                        startTime.set(Calendar.MONTH, 11);//0 = january 11=december
                        startTime.set(Calendar.YEAR, 2021);
                        Calendar endTime = (Calendar) startTime.clone();
                        endTime.set(Calendar.DAY_OF_MONTH, 8+x*7);
                        endTime.set(Calendar.HOUR_OF_DAY, 8+i);
                        endTime.set(Calendar.MINUTE, 30);//endTime.set(Calendar.MINUTE,Stages.get(i).getVisite(); //Ca devrait être ceci mais parce que la base de donnée ne voit pas le bon insert.
                        endTime.set(Calendar.MONTH, 11);
                        endTime.set(Calendar.YEAR, 2021);

                        WeekViewEvent event = new WeekViewEvent(0, Stages.get(i).getEtudiant().toString()+" "+(8+i)+":"+30, startTime, endTime);
                        event.setColor(R.color.orange);
                        events.add(event);
                    }

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
    /**
     * Get TimeSlot
     * @param view
     */
    public void pickTimeSlot(View view){

        boolean picked;
        boolean done=false;
        for(int jours=0;jours<3 &&!done;jours++){//garde les jours entre mercredi et vendredi
            for (int i = 0; i < timeSlots[jours].length &&!done; i++) {
                picked=false;
                if(timeSlots[jours][i] == true && !done){
                    for (int x = 0; x < visite; x++) {
                        if(timeSlots[jours][x+i]==true&&!picked){
                            picked=true;
                        }
                    }
                    if(!picked){
                        for (int x = 0; x < 30; x++) {
                            timeSlots[jours][x+i]=true;
                            done=true;
                        }
                    }
                }
            }
        }
    }
}