package ca.qc.bdeb.c5gm.planistage.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Stage implements Comparable, Parcelable {
    private UUID id;
    private Compte etudiant;
    private Compte prof;
    private Entreprise entreprise;
    private String annee;
    private Priorite priorite = Priorite.BASSE;
    private String timeDiner;
    private String timeStage;
    private boolean[]jourdeStage;
    private int visite;
    private int heureDebut;


    public Stage(Compte etudiant, Compte prof, Entreprise entreprise, String annee,
                 Priorite priorite,String timeDiner,String timeStage, boolean[]jourdeStage,int visite,int heureDebut) {
        this.id = UUID.randomUUID();
        this.etudiant = etudiant;
        this.prof = prof;
        this.entreprise = entreprise;
        this.annee = annee;
        this.priorite = priorite;
        this.timeDiner=timeDiner;
        this.timeStage=timeStage;
        this.jourdeStage=jourdeStage;
        this.visite=visite;
        this.heureDebut=heureDebut;
    }

    public Stage(UUID id, Compte etudiant, Compte prof, Entreprise entreprise, String annee, Priorite priorite) {
        this.id = id;
        this.etudiant = etudiant;
        this.prof = prof;
        this.entreprise = entreprise;
        this.annee = annee;
        this.priorite = priorite;
    }


    protected Stage(Parcel in) {
        id = UUID.fromString(in.readString());
        etudiant = in.readParcelable(Compte.class.getClassLoader());
        prof = in.readParcelable(Compte.class.getClassLoader());
        entreprise = in.readParcelable(Entreprise.class.getClassLoader());
        annee = in.readString();
        priorite = Priorite.values()[in.readInt()];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeParcelable(etudiant, flags);
        dest.writeParcelable(prof, flags);
        dest.writeParcelable(entreprise, flags);
        dest.writeString(annee);
        dest.writeInt(priorite.ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Stage> CREATOR = new Creator<Stage>() {
        @Override
        public Stage createFromParcel(Parcel in) {
            return new Stage(in);
        }

        @Override
        public Stage[] newArray(int size) {
            return new Stage[size];
        }
    };

    public UUID getId() {
        return id;
    }

    public Compte getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Compte etudiant) {
        this.etudiant = etudiant;
    }

    public Compte getProf() {
        return prof;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    //Ajout de tous les getters et les setters pour les nouvelles variables
    //Get et set pour le Temps de diner
    public void setTimeDiner (String timeDiner){ this.timeDiner=timeDiner; };
    public String getTimeDiner(){ return timeDiner; }

    //Get et set pour le Temps de Stage
    public void setTimeStage (String timeStage){ this.timeDiner=timeDiner; };
    public String getTimeStage(){ return timeStage; }

    //get et set pour les jours de Stage
    public boolean[] getJourdeStage() { return jourdeStage; }
    public void setJourdeStage(boolean[] jourdeStage) { this.jourdeStage = jourdeStage; }

    //Get et set pour le nombre de visite
    public int getVisite(){ return visite; }
    public void setVisite(int visite){ this.visite=visite; }

    //Get et set pour l'heure de debut
    public int getHeureDebut(){ return heureDebut; }
    public void setHeureDebut(int heureDebut){ this.heureDebut=heureDebut; }


    @Override
    public int compareTo(Object o) {
        Stage s = null;
        int comparaison = 0;
        if (o == null)
        {
            return comparaison;
        }
        s = (Stage)o;
        if (etudiant.getNom().compareToIgnoreCase(s.getEtudiant().getNom()) == 0)
        {
            comparaison = etudiant.getPrenom().compareToIgnoreCase(s.getEtudiant().getPrenom());
        } else {
            comparaison = etudiant.getNom().compareToIgnoreCase(s.getEtudiant().getNom());
        }
        return comparaison;
    }
}
