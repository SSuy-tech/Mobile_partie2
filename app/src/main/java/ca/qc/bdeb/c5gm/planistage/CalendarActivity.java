package ca.qc.bdeb.c5gm.planistage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;

import ca.qc.bdeb.c5gm.planistage.data.Stage;
import ca.qc.bdeb.c5gm.planistage.data.StageDB;


public class CalendarActivity extends Activity {

    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private ListView hourListView;
    private StageDB DB;
    private ArrayList<Stage> Stages;
    private boolean [][] meetingTimes;
    private boolean [][] dispo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horaire);
        //10heures * 4 quart de session de 15 minutes 40
        meetingTimes=new boolean[3][40];
        initWidgets();
        DB.getInstance(this);
        Stages=DB.getTousLesStages();
        for (int i = 0; i < Stages.size(); i++) {
            addmeetings(Stages.get(i));
        }
    }
    private void initWidgets()
    {
        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
    }
    public void addmeetings(Stage S){
        dispo=S.getJourdeDispoTuteur();

        //Pour le jour
        for (int j = 0; j < dispo.length; j++) {
            //Pour le temps de la journee
            for (int i = 0; i < meetingTimes[0].length; i++) {
                if(dispo[j][0]||dispo[j][1]){
                    //trouve un disponibilité
                    for (int x = 0; x < S.getVisite()/4; x++) {
                        if(meetingTimes[j][])
                    }
                }
            }
        }

    }

    public void buttonClicked(View view){
        if(view.getId() == R.id.boutonListe){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if(view.getId() == R.id.boutonCarte){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        } else if(view.getId() == R.id.boutonCalendrier){
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

}
