package ca.qc.bdeb.c5gm.planistage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

import ca.qc.bdeb.c5gm.planistage.data.Compte;
import ca.qc.bdeb.c5gm.planistage.data.Entreprise;
import ca.qc.bdeb.c5gm.planistage.data.Priorite;
import ca.qc.bdeb.c5gm.planistage.data.Stage;
import ca.qc.bdeb.c5gm.planistage.data.StageDB;

public class EditStage extends AppCompatActivity {

    private Spinner eleveSp;
    private Spinner entrepriseSp;

    private ArrayList<Compte> listeEleves = null;
    private ArrayList<Entreprise> listeEntreprises = null;
    private ArrayAdapter<Compte> eleveAdapter;
    private ArrayAdapter<Entreprise> entrepriseAdapter;

    private Compte eleveSeletion = null;
    private Compte prof = null;
    private Entreprise entrepriseSeletion = null;
    private Stage stage = null;
    private int position = -1;

    private StageDB db = null;

    private ImageView ivPhoto;
    private ImageView ivFlag;
    private TextView tvAdresse;
    private TextView tvVille;
    private TextView tvProvince;
    private TextView tvCodePostal;

    private Button timePickStage;
    private Button timePickDiner;
    private int hourS, minuteS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stage);

        initData();

        initViews();
    }

    private void initData() {

        db = StageDB.getInstance(getApplicationContext());

        stage = (Stage) getIntent().getParcelableExtra(MainActivity.EXTRA_STAGE);
        // stage différent de null mode édition, sinon mode création
        if (stage != null) {
            listeEleves = new ArrayList<>();
            listeEleves.add(stage.getEtudiant());
            position = getIntent().getIntExtra(MainActivity.EXTRA_POSITION, 0);
            eleveSeletion = stage.getEtudiant();
            entrepriseSeletion = stage.getEntreprise();
            prof = stage.getProf();
        } else {
            listeEleves = db.getLesElevesSansStage();
            stage = new Stage(eleveSeletion, prof, entrepriseSeletion, StageUtils.getAnnee(), Priorite.BASSE);
        }

        listeEntreprises = db.getToutesLesEntreprises();
    }

    private void initViews() {
        ivFlag = findViewById(R.id.ivFlag);
        ivFlag.setImageResource(StageUtils.getImageFromPriorite(stage.getPriorite()));
        ivPhoto = findViewById(R.id.ivPhoto);
        ivPhoto.setImageResource(R.drawable.ic_image_24);

        tvAdresse = findViewById(R.id.adresseView);
        tvVille = findViewById(R.id.villeView);
        tvCodePostal = findViewById(R.id.codePostalView);
        tvProvince = findViewById(R.id.provinceView);
        // spinner élèves
        eleveSp = findViewById(R.id.eleve_spinner);
        //Buttons
        timePickStage = findViewById(R.id.buttonHStage);
        timePickDiner = findViewById(R.id.buttonHDiner);
        eleveAdapter = new ArrayAdapter<Compte>(
                this, android.R.layout.simple_spinner_item, listeEleves);
        eleveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eleveSp.setAdapter(eleveAdapter);
        eleveSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eleveSeletion = (Compte) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if (position >= 0) {
            eleveSp.setEnabled(false);
        }

        // spinner entreprises
        entrepriseSp = findViewById(R.id.entreprise_spinner);
        entrepriseAdapter = new ArrayAdapter<Entreprise>(
                this, android.R.layout.simple_spinner_item, listeEntreprises);
        entrepriseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        entrepriseSp.setAdapter(entrepriseAdapter);
        if (entrepriseSeletion != null) {
            for (int pos = 0; pos < listeEntreprises.size(); pos++) {
                if (listeEntreprises.get(pos).getId().compareTo(entrepriseSeletion.getId()) == 0) {
                    entrepriseSp.setSelection(pos);
                    break;
                }
            }
        }
        entrepriseSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                entrepriseSeletion = (Entreprise) adapterView.getSelectedItem();
                tvAdresse.setText(entrepriseSeletion.getAdresse());
                tvVille.setText(entrepriseSeletion.getVille());
                tvProvince.setText(entrepriseSeletion.getProvince());
                tvCodePostal.setText(entrepriseSeletion.getCodePostal());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void changePriorite(View view) {
        StageUtils.changerPriorite(stage);

        ivFlag.setImageResource(StageUtils.getImageFromPriorite(stage.getPriorite()));

    }

    public void clickAppliquer(View view) {

        stage.setEntreprise(entrepriseSeletion);
        stage.setEtudiant(eleveSeletion);

        Intent intentMessage = new Intent();
        intentMessage.putExtra(MainActivity.EXTRA_STAGE_RESULT, stage);
        intentMessage.putExtra(MainActivity.EXTRA_POSITION, position);
        setResult(RESULT_OK, intentMessage);

        finish();
    }

    /**
     * Fait un pop-up qui permet l'utilisateur de choisir un temps pour le stage de l'étudiant
     * @param view
     */
    public void timePickerStage(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int SelectedH, int SelectedM) {
                hourS = SelectedH;
                minuteS = SelectedM;
                timePickStage.setText(String.format(Locale.getDefault(), "%02d:%02d", hourS, minuteS));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hourS, minuteS, true);
        timePickerDialog.setTitle("Temps?");
        timePickerDialog.show();
    }

    /**
     * Crée un pop-up qui permet l'utilisateur de choisir un temps pour le stage d'un étudiant
     * @param view
     */
    public void timePickerDiner(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int SelectedH, int SelectedM) {
                hourS = SelectedH;
                minuteS = SelectedM;
                timePickDiner.setText(String.format(Locale.getDefault(), "%02d:%02d", hourS, minuteS));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hourS, minuteS, true);
        timePickerDialog.setTitle("Temps?");
        timePickerDialog.show();
    }
}