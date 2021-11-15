package ca.qc.bdeb.c5gm.planistage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import ca.qc.bdeb.c5gm.planistage.data.Priorite;
import ca.qc.bdeb.c5gm.planistage.data.Stage;

public class StageUtils {

    public static final byte FLAG_ELEVEE = 0x01;
    public static final byte FLAG_MOYEN = 0x02;
    public static final byte FLAG_BAS = 0x04;
    public static final byte FLAG_ALL = FLAG_ELEVEE | FLAG_MOYEN | FLAG_BAS;


    public static byte filtre = StageUtils.FLAG_ALL;

    public static String getAnnee() {
        String anneeScolaire = "";
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        if (month > 7){
            anneeScolaire = year+"-"+(year+1);
        } else {
            anneeScolaire = (year-1)+"-"+year;
        }

        return anneeScolaire;
    }

    public static int getImageFromPriorite(Priorite p){
        int imgResId = R.drawable.ic_green_flag_24;

        switch (p){
            case MOYENNE:
                imgResId = R.drawable.ic_yellow_flag_24;
                break;
            case ELEVEE:
                imgResId = R.drawable.ic_red_flag_24;
                break;
        }
        return imgResId;
    }

    public static float getHueFromPriorite(Priorite p){
        float hueTint = BitmapDescriptorFactory.HUE_GREEN;

        switch (p){
            case MOYENNE:
                hueTint = BitmapDescriptorFactory.HUE_YELLOW;
                break;
            case ELEVEE:
                hueTint = BitmapDescriptorFactory.HUE_RED;
                break;
        }
        return hueTint;
    }

    public static void changerPriorite(Stage stage){
        int priorite = stage.getPriorite().ordinal();

        priorite--;
        if (priorite < Priorite.ELEVEE.ordinal()){
            priorite = Priorite.BASSE.ordinal();
        }
        stage.setPriorite(Priorite.values()[priorite]);
    }


    public static void setFlagFromId(int id, boolean checked){
        byte flag = 0;
        switch (id){
            case R.id.cb_basse:
                flag = StageUtils.FLAG_BAS;
                break;
            case R.id.cb_moyen:
                flag = StageUtils.FLAG_MOYEN;
                break;
            case R.id.cb_elevee:
                flag = StageUtils.FLAG_ELEVEE;
                break;
            default:
                Log.e("TAG", "Erreur de priorité");

        }
        if (checked){
            StageUtils.filtre |= flag;
        } else {
            StageUtils.filtre -= flag;
        }
    }

    // code inspiré de : https://developer.android.com/training/camera/photobasics
    // pass the dimensions of the View
    // largeurCible = imageView.getWidth();
    // hauteurCible = imageView.getHeight();
    private Bitmap reduirePhoto(int largeurCible, int hauteurCible, String photoPath) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/largeurCible, photoH/hauteurCible));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        if (image != null){
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } else{
            return null;
        }
    }
}
