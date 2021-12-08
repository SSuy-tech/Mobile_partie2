package ca.qc.bdeb.c5gm.planistage;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.qc.bdeb.c5gm.planistage.data.Stage;
import ca.qc.bdeb.c5gm.planistage.data.StageDB;
import ca.qc.bdeb.c5gm.planistage.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 101;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private StageDB db = null;
    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<Stage> stages = null;
    private ArrayList<Marker> reperes = null;
    private LatLng position;
    // Demander la permission d'activer la localisation
    private boolean isLocationEnabled = false;
    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private FusedLocationProviderClient client;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                isLocationEnabled = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        activerLocalisation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db= db.getInstance(this);
        stages=db.getTousLesStages();

        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        client= LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(this);

        if(ActivityCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        };

        ((ImageView)findViewById(R.id.iv_priorite_basse)).setImageResource(R.drawable.ic_green_flag_24);
        ((ImageView)findViewById(R.id.iv_priorite_moyenne)).setImageResource(R.drawable.ic_yellow_flag_24);
        ((ImageView)findViewById(R.id.iv_priorite_haute)).setImageResource(R.drawable.ic_red_flag_24);

        for (int i = 0; i < stages.size(); i++) {
            addmarkers(stages.get(i));
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initData();
    }

    private void getCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
                }
            }
        });
    }

    private void addmarkers(Stage stage) {
        position = getLocationFromAddress(stage.getEntreprise().getAdresseComplete());
        mMap.addMarker(new MarkerOptions().position(position).title(stage.getEtudiant().toString())
        .snippet("Stage"));//.icon(BitmapDescriptorFactory.defaultMarker()));
    }

    /**
     * Code pris de: https://stackoverflow.com/questions/42626735/geocoding-converting-an-address-in-string-form-into-latlng-in-googlemaps-jav
     * @param inputtedAddress
     * @return
     */
    public LatLng getLocationFromAddress(String inputtedAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(inputtedAddress, 5);
            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return resLatLng;
    }

    private void initData() {
        db = StageDB.getInstance(getApplicationContext());
        stages = db.getTousLesStagesSansPhoto();
        reperes = new ArrayList<>();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ActivityCompat.requestPermissions(this, permissions, LOCATION_REQUEST_CODE);

        placerLesReperes();

    }

    private void placerLesReperes() {
        for (Stage s : stages) {
            reperes.add(mMap.addMarker(
                    new MarkerOptions()
                            .position(getPositionFromAddress(s.getEntreprise().getAdresseComplete()))
                            .title(s.getEtudiant().toString())
                            .snippet(s.getEntreprise().toString())
                            .icon(BitmapDescriptorFactory.defaultMarker(StageUtils.getHueFromPriorite(s.getPriorite())))
            ));
        }
        filtrerLesReperes();
    }


    private LatLng getPositionFromAddress(String adresse) {
        LatLng positionVal = null;
        if (Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(adresse, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address monAdresse = addresses.get(0);
                    positionVal = new LatLng(monAdresse.getLatitude(), monAdresse.getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return positionVal;
    }


    private void filtrerLesReperes() {
        for (int i = 0; i < reperes.size(); i++) {
            reperes.get(i).setVisible((stages.get(i).getPriorite().getFlag() & StageUtils.filtre) != 0);
        }
    }

    @SuppressLint("MissingPermission")
    private void activerLocalisation() {
        if (isLocationEnabled && mMap != null) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location currLocation) {
                    if (currLocation != null) {
                        Log.i("TAG", "Ma localisation: " + currLocation);

                        LatLng maLocation = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maLocation, 12));
                    }
                }
            });
        }
    }

    public void filtrerPriorite(View view) {
        StageUtils.setFlagFromId(view.getId(), ((CheckBox) view).isChecked());
        filtrerLesReperes();
    }
}