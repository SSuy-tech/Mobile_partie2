package ca.qc.bdeb.c5gm.planistage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CalendarActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


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
