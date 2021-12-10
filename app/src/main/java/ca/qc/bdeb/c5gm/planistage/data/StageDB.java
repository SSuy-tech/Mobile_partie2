package ca.qc.bdeb.c5gm.planistage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.UUID;

import ca.qc.bdeb.c5gm.planistage.StageUtils;

public class StageDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "planistage.db";
    private static final int DB_VERSION = 1;
    private static StageDB instance = null;
    private final Context context;


    // Definition de la table des entreprises
    private static class TblEntreprise implements BaseColumns {
        public static final String _NAME = "entreprise";
        public static final String NOM = "nom";
        public static final String ADRESSE = "adresse";
        public static final String VILLE = "ville";
        public static final String PROVINCE = "province";
        public static final String CODE_POSTAL = "code_postal";
    }

    // Definition de la table des comptes
    private static class TblCompte implements BaseColumns {
        public static final String _NAME = "compte";
        public static final String CREATED_AT = "created_at";
        public static final String DELETED_AT = "deleted_at";
        public static final String EMAIL = "email";
        public static final String EST_ACTIF = "est_actif";
        public static final String MOT_DE_PASSE = "mot_de_passe";
        public static final String NOM = "nom";
        public static final String PRENOM = "prenom";
        public static final String PHOTO = "photo";
        public static final String UPDATED_AT = "updated_at";
        public static final String TYPE_COMPTE = "type_compte";
    }

    // Definition de la table des stages
    private static class TblStage implements BaseColumns {
        public static final String _NAME = "stage";
        public static final String ETUDIANT_ID = "etudiant_id";
        public static final String PROFESSEUR_ID = "professeur_id";
        public static final String ENTREPRISE_ID = "entreprise_id";
        public static final String ANNEE_SCOLAIRE = "annee_scolaire";
        public static final String PRIORITE = "priorite";
    }

    // Definition de la table des visites
    private static class TblVisite implements BaseColumns {
        public static final String _NAME = "visite";
        public static final String DATE = "date";
        public static final String HEURE_DEBUT = "heure_debut";
        public static final String DUREE = "duree";
        public static final String STAGE_ID = "stage_id";
    }

    // création de la table des entreprises
    private static final String CREER_ENTREPRISES =
            "CREATE TABLE " + TblEntreprise._NAME + " (" +
                    TblEntreprise._ID + " TEXT PRIMARY KEY," +
                    TblEntreprise.NOM + " TEXT," +
                    TblEntreprise.ADRESSE + " TEXT," +
                    TblEntreprise.VILLE + " TEXT," +
                    TblEntreprise.PROVINCE + " TEXT," +
                    TblEntreprise.CODE_POSTAL + " TEXT)";

    // création de la table des comptes
    private static final String CREER_COMPTES =
            "CREATE TABLE " + TblCompte._NAME + " (" +
                    TblCompte._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TblCompte.NOM + " TEXT," +
                    TblCompte.PRENOM + " TEXT," +
                    TblCompte.EMAIL + " TEXT," +
                    TblCompte.EST_ACTIF + " INTEGER," +
                    TblCompte.MOT_DE_PASSE + " TEXT," +
                    TblCompte.PHOTO + " BLOB," +
                    TblCompte.CREATED_AT + " TEXT," +
                    TblCompte.DELETED_AT + " TEXT," +
                    TblCompte.UPDATED_AT + " TEXT," +
                    TblCompte.TYPE_COMPTE + " INTEGER)";

    // création de la table des stages
    private static final String CREER_STAGES =
            "CREATE TABLE " + TblStage._NAME + " (" +
                    TblStage._ID + " TEXT PRIMARY KEY," +
                    TblStage.ETUDIANT_ID + " INTEGER," +
                    TblStage.PROFESSEUR_ID + " INTEGER," +
                    TblStage.ENTREPRISE_ID + " TEXT," +
                    TblStage.ANNEE_SCOLAIRE + " TEXT," +
                    TblStage.PRIORITE + " INTEGER)";

    // création de la table des visites
    private static final String CREER_VISITES =
            "CREATE TABLE " + TblVisite._NAME + " (" +
                    TblVisite._ID + " TEXT PRIMARY KEY," +
                    TblVisite.STAGE_ID + " TEXT," +
                    TblVisite.DATE + " TEXT," +
                    TblVisite.HEURE_DEBUT + " TEXT," +
                    TblVisite.DUREE + " INTEGER)";


    public static StageDB getInstance(Context ctx) {
        if (instance == null) {
            instance = new StageDB(ctx);
        }
        return instance;
    }

    private StageDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREER_COMPTES);
        sqLiteDatabase.execSQL(CREER_ENTREPRISES);
        sqLiteDatabase.execSQL(CREER_STAGES);
        sqLiteDatabase.execSQL(CREER_VISITES);

        insererDonneesDepart(sqLiteDatabase);
    }

    private void insererDonneesDepart(SQLiteDatabase db) {

        // Entreprises :
        Entreprise entreprise = new Entreprise("Jean Coutu", "4885 Henri-Bourassa Blvd W 731", "Montreal", "QC", "H3L 1P3");
        ajoutEntreprise(db, entreprise);
        entreprise = new Entreprise("Garage Tremblay", "10142 Boul. Saint-Laurent", "Montreal", "QC", "H3L 2N7");
        ajoutEntreprise(db, entreprise);
        entreprise = new Entreprise("Pharmaprix", "3611 Rue Jarry E", "Montreal", "QC", "H1Z 2G1");
        ajoutEntreprise(db, entreprise);
        entreprise = new Entreprise("Alimentation Générale", "1853 Chem. Rockland", "Mont-Royal", "QC", "H3P 2Y7");
        ajoutEntreprise(db, entreprise);
        entreprise = new Entreprise("Auto Repair", "8490 Rue Saint-Dominique", "Montreal", "QC", "H2P 2L5");
        ajoutEntreprise(db, entreprise);
        entreprise = new Entreprise("Subway", "775 Rue Chabanel O", "Montreal", "QC", "H4N 3J7");
        ajoutEntreprise(db, entreprise);
        entreprise = new Entreprise("Métro", "1331 Blvd. de la Côte-Vertu", "Saint-Laurent", "QC", "H4L 1Z1");
        ajoutEntreprise(db, entreprise);
        entreprise = new Entreprise("Épicerie les Jardinières", "10345 Ave Christophe-Colomb", "Montreal", "QC", "H2C 2V1");
        ajoutEntreprise(db, entreprise);
        entreprise = new Entreprise("Boucherie Marien", "1499-1415 Rue Jarry E", "Montreal", "QC", "XXX XXX");
        ajoutEntreprise(db, entreprise);
        entreprise = new Entreprise("IGA", "8921 Rue Lajeunesse", "Montreal", "QC", "H2M 1S1");
        ajoutEntreprise(db, entreprise);

        // Comptes:
        Compte compte = new Compte("Prades", "Pierre", "pierre.prades@test.com", TypeCompte.PROF, null);
        ajoutCompte(db, compte);
        compte = new Compte("Boucher", "Mikaël", "mikael.boucher@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
        compte = new Compte("Gagnon", "Thomas", "thomas.gagnon@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
        compte = new Compte("Gingras", "Simon", "simon.gingras@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
        compte = new Compte("Leblanc", "Kevin", "kevin.leblanc@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
        compte = new Compte("Masson", "Cédric", "cedric.masson@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
        compte = new Compte("Monette", "Vanessa", "vanessa.monette@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
        compte = new Compte("Picard", "Vincent", "vincent.picard@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
        compte = new Compte("Poulain", "Mélissa", "melissa.poulain@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
        compte = new Compte("Vargas", "Diego", "diego.vargas@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
        compte = new Compte("Tremblay", "Geneviève", "genevieve.tremblay@test.com", TypeCompte.ELEVE, null);
        ajoutCompte(db, compte);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TblVisite._NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TblStage._NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TblEntreprise._NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TblCompte._NAME);
        onCreate(sqLiteDatabase);
    }

    private void ajoutEntreprise(SQLiteDatabase db, Entreprise ent) {
        ContentValues values = new ContentValues();

        values.put(TblEntreprise._ID, ent.getId().toString());
        values.put(TblEntreprise.NOM, ent.getNom());
        values.put(TblEntreprise.ADRESSE, ent.getAdresse());
        values.put(TblEntreprise.VILLE, ent.getVille());
        values.put(TblEntreprise.PROVINCE, ent.getProvince());
        values.put(TblEntreprise.CODE_POSTAL, ent.getCodePostal());

        long id = db.insert(TblEntreprise._NAME, null, values);
    }

    private void ajoutCompte(SQLiteDatabase db, Compte compte) {
        ContentValues values = new ContentValues();

        values.put(TblCompte.NOM, compte.getNom());
        values.put(TblCompte.PRENOM, compte.getPrenom());
        values.put(TblCompte.EMAIL, compte.getEmail());
        values.put(TblCompte.EST_ACTIF, true);
        values.put(TblCompte.TYPE_COMPTE, compte.getTypeCompte().ordinal());

        long id = db.insert(TblCompte._NAME, null, values);
    }

    /*
     * getting all todos under single tag
     * */
    public ArrayList<Compte> getLesElevesSansStage() {
        ArrayList<Compte> listeEleves = new ArrayList<>();

        String selectQuery = "SELECT " + TblCompte._ID + "," + TblCompte.NOM + "," + TblCompte.PRENOM + "," +
                TblCompte.EMAIL + "," + TblCompte.TYPE_COMPTE + " FROM " + TblCompte._NAME +
                " WHERE " + TblCompte.TYPE_COMPTE + " = " + TypeCompte.ELEVE.ordinal() + " AND "
                + TblCompte._ID + " NOT IN (SELECT " + TblStage.ETUDIANT_ID + " FROM " + TblStage._NAME + ")";

        Log.e("TAG", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                listeEleves.add(new Compte(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        TypeCompte.values()[cursor.getInt(4)], null));
            }
            cursor.close();
        }

        // Retourner tous les eleves
        return listeEleves;
    }

    public ArrayList<Compte> getTousLesEleves() {

        ArrayList<Compte> listeEleves = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD
        String[] columns = {
                TblCompte._ID,
                TblCompte.NOM,
                TblCompte.PRENOM,
                TblCompte.EMAIL,
                TblCompte.TYPE_COMPTE
        };
        Cursor cursor = db.query(TblCompte._NAME, columns, TblCompte.TYPE_COMPTE + " = ?", new String[]{"" + TypeCompte.ELEVE.ordinal()}, null, null, null, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                listeEleves.add(new Compte(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        TypeCompte.values()[cursor.getInt(4)], null));
            }
            cursor.close();
        }

        // Retourner tous les eleves
        return listeEleves;
    }

    public ArrayList<Entreprise> getToutesLesEntreprises() {

        ArrayList<Entreprise> listeEntreprises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD

        String[] columns = {
                TblEntreprise._ID,
                TblEntreprise.NOM,
                TblEntreprise.ADRESSE,
                TblEntreprise.VILLE,
                TblEntreprise.PROVINCE,
                TblEntreprise.CODE_POSTAL
        };

        Cursor cursor = db.query(TblEntreprise._NAME, null, null, null, null, null, null, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                listeEntreprises.add(new Entreprise(UUID.fromString(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            }
            cursor.close();
        }

        // Retourner toutes les entreprises
        return listeEntreprises;
    }

    public ArrayList<Stage> getTousLesStages(boolean chargerPhotos) {

        ArrayList<Stage> listeStages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD

        String[] columns = {
                TblStage._ID,
                TblStage.ETUDIANT_ID,
                TblStage.PROFESSEUR_ID,
                TblStage.ENTREPRISE_ID,
                TblStage.ANNEE_SCOLAIRE,
                TblStage.PRIORITE
        };

        Cursor cursor = db.query(TblStage._NAME, null, null, null, null, null, null, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                listeStages.add(new Stage(UUID.fromString(cursor.getString(0)),
                        getCompte(db, cursor.getString(1), chargerPhotos),
                        getCompte(db, cursor.getString(2), chargerPhotos),
                        getEntreprise(db, cursor.getString(3)),
                        cursor.getString(4),
                        Priorite.values()[cursor.getInt(5)]));

            }
            cursor.close();
        }

        // Retourner tous les stages
        return listeStages;
    }


    public ArrayList<Stage> getTousLesStages() {
        return getTousLesStages(true);
    }

    public ArrayList<Stage> getTousLesStagesSansPhoto() {
        return getTousLesStages(false);
    }

    public Compte getCompte(UUID id) {
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD
        return getCompte(db, id.toString(), true);
    }

    private Compte getCompte(SQLiteDatabase db, String id, boolean chargerPhoto) {
        Compte compte = null;
        String[] columns = {
                TblCompte._ID,
                TblCompte.NOM,
                TblCompte.PRENOM,
                TblCompte.EMAIL,
                TblCompte.TYPE_COMPTE,
                TblCompte.PHOTO
        };
        Cursor cursor = db.query(TblCompte._NAME, columns, TblCompte._ID + " = ?", new String[]{id}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                if (chargerPhoto){
                compte = new Compte(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        TypeCompte.values()[cursor.getInt(4)], StageUtils.getImage(cursor.getBlob(5)));
                } else {
                    compte = new Compte(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            TypeCompte.values()[cursor.getInt(4)], null);
                }
            }
            cursor.close();
        }

        // Retourner le compte
        return compte;
    }


    private Entreprise getEntreprise(SQLiteDatabase db, String id) {

        Entreprise entreprise = null;

        String[] columns = {
                TblEntreprise._ID,
                TblEntreprise.NOM,
                TblEntreprise.ADRESSE,
                TblEntreprise.VILLE,
                TblEntreprise.PROVINCE,
                TblEntreprise.CODE_POSTAL
        };

        Cursor cursor = db.query(TblEntreprise._NAME, null, TblEntreprise._ID + " = ?", new String[]{id}, null, null, null, null);
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                entreprise = new Entreprise(UUID.fromString(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
            }
            cursor.close();
        }

        // Retourner toutes les entreprises
        return entreprise;
    }

    public boolean modifStage(Stage stage) {
        SQLiteDatabase db = this.getWritableDatabase(); // On veut ecrire dans la BD

        if (stage == null)
            return false;

        ContentValues values = new ContentValues();
        values.put(TblStage.ANNEE_SCOLAIRE, stage.getAnnee());
        values.put(TblStage.ENTREPRISE_ID, stage.getEntreprise().getId().toString());
        values.put(TblStage.PRIORITE, stage.getPriorite().ordinal());

        return db.update(TblStage._NAME, values, TblStage._ID + " = ?", new String[]{stage.getId().toString()}) > 0;
    }

    public boolean ajoutStage(Stage stage) {
        SQLiteDatabase db = this.getWritableDatabase(); // On veut ecrire dans la BD


        if (stage == null || stage.getEtudiant() == null || stage.getEntreprise() == null)
            return false;

        ContentValues values = new ContentValues();
        values.put(TblStage._ID, stage.getId().toString());
        values.put(TblStage.ANNEE_SCOLAIRE, stage.getAnnee());
        values.put(TblStage.ETUDIANT_ID, stage.getEtudiant().getId());
        values.put(TblStage.ENTREPRISE_ID, stage.getEntreprise().getId().toString());
        values.put(TblStage.PROFESSEUR_ID, 1);
        values.put(TblStage.PRIORITE, stage.getPriorite().ordinal());


        ContentValues values2 = new ContentValues();
        values2.put(TblVisite._ID,stage.getId().toString());
        values2.put(TblVisite.STAGE_ID,UUID.randomUUID().toString());
        //values2.put(TblVisite.DUREE,stage.getVisite());
        values2.put(TblVisite.DUREE,30);
        values2.put(TblVisite.HEURE_DEBUT,stage.getTimeStage());

        //Vérifie si les inserts marchent
        ArrayList<Stage> toustages=new ArrayList<Stage>();
        toustages = getTousLesStages();

        return db.insert(TblStage._NAME, null, values) == toustages.size()+1
        && db.insert(TblVisite._NAME,null,values2)== toustages.size()+1;
    }

    public void modifierImageEleve( Compte eleve ) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new  ContentValues();
        values.put(TblCompte.PHOTO, StageUtils.getBytes(eleve.getPhoto()) );
        database.update( TblCompte._NAME, values,TblCompte._ID + " = ?", new String[]{eleve.getId().toString()} );
    }

}
