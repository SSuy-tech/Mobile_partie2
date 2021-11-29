package ca.qc.bdeb.c5gm.planistage;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Collections;

import ca.qc.bdeb.c5gm.planistage.data.Stage;
import ca.qc.bdeb.c5gm.planistage.data.StageDB;

public class MainActivity extends AppCompatActivity {
    //Vues
    private RecyclerView recyclerView;
    private StageAdapter adapter;

    //données utilisateur
    private ArrayList<Stage> stages;
    private StageDB db=null;

    //constantes:
    public static final String EXTRA_STAGE = "ca.qc.bdeb.c5gm.planistage.EXTRA_STAGE";
    public static final String EXTRA_STAGE_RESULT = "ca.qc.bdeb.c5gm.planistage.EXTRA_STAGE_RESULT";
    public static final String EXTRA_POSITION = "ca.qc.bdeb.c5gm.planistage.EXTRA_POSITION";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        initViews();
    }

    private void initData() {
        db = StageDB.getInstance(getApplicationContext());
        stages = db.getTousLesStages();
        trierStages();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rv_stages);
        recyclerView.setHasFixedSize(true);
        adapter = new StageAdapter(stages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // add click listener
        adapter.setOnStageClickListener(new StageAdapter.OnStageClickListener() {

            @Override
            public void onEditClick(int position) {
                editStage(position);
            }

            @Override
            public void onPrioriteClick(int position) {
                StageUtils.changerPriorite(stages.get(position));
                db.modifStage(stages.get(position));
                adapter.notifyItemChanged(position);
            }
        });
        adapter.filtrer(StageUtils.filtre);
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    /**
     * Remplacement de startActivityForResult qui est deprecated
     *
     * @param position
     */
    ActivityResultLauncher<Intent> mEditStage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();

                        int position = intent.getIntExtra(EXTRA_POSITION, -1);

                        Stage stage = intent.getParcelableExtra(EXTRA_STAGE_RESULT);

                        if (position >= 0) {
                            stages.set(position,stage);
                            db.modifStage(stage);
                            adapter.notifyItemChanged(position);
                        } else {
                            stages.add(stage);
                            db.ajoutStage(stage);

                            trierStages();

                            for (int i = 0; i < stages.size(); i++) {
                                if (stages.get(i) == stage) {
                                    adapter.notifyItemInserted(i);
                                    break;
                                }
                            }
                        }
                    }
                }
            });

    private void trierStages() {
        Collections.sort(stages);
    }

    public void ajoutStage(View view) {
        Intent intent = new Intent(this, EditStage.class);
        mEditStage.launch(intent);
    }

    public void buttonClicked(View view){
        if(view.getId() == R.id.boutonListe){
            setContentView(R.layout.activity_main);
        } else if(view.getId() == R.id.boutonCarte){
            setContentView(R.layout.activity_maps);
        } else if(view.getId() == R.id.boutonCalendrier){
            setContentView(R.layout.activity_schedule);
        } else {
            setContentView(R.layout.activity_main);
        }
    }


    /**
     * édition du contact
     *
     * @param position
     */
    public void editStage(int position) {
        Log.v("RecyclerView", "" + position + " a été sélectionné: " + stages.get(position).getEtudiant());

        Intent intent = new Intent(this, EditStage.class);
        intent.putExtra(EXTRA_STAGE, stages.get(position));
        intent.putExtra(EXTRA_POSITION, position);
        mEditStage.launch(intent);
    }

    public void filtrerPriorite(View view) {
        StageUtils.setFlagFromId(view.getId(), ((CheckBox) view).isChecked());
        adapter.filtrer(StageUtils.filtre);
    }
}