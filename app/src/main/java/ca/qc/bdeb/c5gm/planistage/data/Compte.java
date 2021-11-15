package ca.qc.bdeb.c5gm.planistage.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Compte implements Parcelable {
    // champs du compte:
    private final Integer id;
    private final String nom;
    private final String prenom;
    private final String email;
    private final TypeCompte typeCompte;
    private Bitmap photo;

    public Compte(Integer id, String nom, String prenom, String email, TypeCompte typeCompte, Bitmap photo) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.typeCompte = typeCompte;
        this.photo = photo;
    }

    public Compte(String nom, String prenom, String email, TypeCompte typeCompte, Bitmap photo) {
        this.id = 0;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.typeCompte = typeCompte;
        this.photo = photo;
    }

    protected Compte(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        nom = in.readString();
        prenom = in.readString();
        email = in.readString();
        typeCompte = TypeCompte.values()[in.readInt()];
        photo = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(email);
        dest.writeInt(typeCompte.ordinal());
        dest.writeParcelable(photo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Compte> CREATOR = new Creator<Compte>() {
        @Override
        public Compte createFromParcel(Parcel in) {
            return new Compte(in);
        }

        @Override
        public Compte[] newArray(int size) {
            return new Compte[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public TypeCompte getTypeCompte() {
        return typeCompte;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    @NonNull
    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}
