package ca.qc.bdeb.c5gm.planistage.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.UUID;

public class Entreprise implements Parcelable {
    private final UUID id;
    private final String nom;
    private final String adresse;
    private final String ville;
    private final String province;
    private final String codePostal;

    public Entreprise(String nom, String adresse, String ville, String province, String codePostal) {
        this.id = UUID.randomUUID();
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.province = province;
        this.codePostal = codePostal;
    }

    public Entreprise(UUID id, String nom, String adresse, String ville, String province, String codePostal) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.province = province;
        this.codePostal = codePostal;
    }

    protected Entreprise(Parcel in) {
        id = UUID.fromString(in.readString());
        nom = in.readString();
        adresse = in.readString();
        ville = in.readString();
        province = in.readString();
        codePostal = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeString(nom);
        dest.writeString(adresse);
        dest.writeString(ville);
        dest.writeString(province);
        dest.writeString(codePostal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Entreprise> CREATOR = new Creator<Entreprise>() {
        @Override
        public Entreprise createFromParcel(Parcel in) {
            return new Entreprise(in);
        }

        @Override
        public Entreprise[] newArray(int size) {
            return new Entreprise[size];
        }
    };

    public UUID getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getProvince() {
        return province;
    }

    public String getCodePostal() {
        return codePostal;
    }

    @NonNull
    @Override
    public String toString() {
        return nom;
    }

    @NonNull
    public String getAdresseComplete() {
        return adresse + ", " + ville + ", " + province + ", " + codePostal;
    }
}
