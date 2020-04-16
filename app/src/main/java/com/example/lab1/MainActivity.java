package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Geocoder;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

// Tässä on yhdistetty kaikki lab1 tehtävät samaan äppiin. Jokainen nappi suorittaa eri tehtävän annon.
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    TextView text;
    double Lat = 0;
    double Long = 0;
    public List<Address> lista;
    String country_name;
    String city_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.locationBtn).setOnClickListener(this);
        findViewById(R.id.geoBtn).setOnClickListener(this);
        findViewById(R.id.hmtlBtn).setOnClickListener(this);
        text = findViewById(R.id.Teksti);

        // Ohitetaan androidin turvapolicy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Tarkistetaan onko oikeus sijaintidataan ja jos ei ole niin kysytään lupaa
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                }
            }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.locationBtn){
            location();
        }
        if(v.getId() == R.id.geoBtn){
            geocode();
        }
        if(v.getId() == R.id.hmtlBtn){
            simpleHtml();
        }
    }

    //Tehtävä 1, haetaan gps data ja kirjataan se näyttöön. Funktio on try/catchin sisässä koska jos ei ole gps päällä tai jostain syystä ole mapsia käytetty päällä niin sovellus kaatuu muuten.
    private void location(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        try{
                            Lat = location.getLatitude();
                            Long = location.getLongitude();
                            if(Lat != 0 && Long !=0){
                                String cord = Double.toString(Lat) +"\n" + Double.toString(Long) + "\n" + "\n" + "\n";
                                text.setText(cord);
                            }else{

                            }
                        }catch (Exception e){
                            text.setText("Virhe hakiessa koordinaatteja, onko sijainti päällä? Käytä google maps päällä ja koita uudestaan");
                        }
                    }
                });
    }

    //Tehtävä 2 haetaan paikkatiedot annetuilla koordinaateilla, tässä tapauksessa ne jotka saatiin edellisestä funktiosta
    private void geocode() {
        if(Lat != 0 && Long != 0){
            try{
                Geocoder geo = new Geocoder(this);
                lista = geo.getFromLocation(Lat,Long,1);
                country_name = lista.get(0).getCountryName();
                city_name = lista.get(0).getLocality();
                text.append(country_name + "\n" + city_name + "\n");

            }catch (Exception e){
            }

        }
    }

    // Tehtävä 3  Haetaan html string ja kirjataan se näyttöön
    private void simpleHtml() {
            try {
                EditText urlLine = findViewById(R.id.hmtlEdit);
                URL url = new URL(urlLine.getText().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(connection.getInputStream());
                String htmlText = Utilities.fromStream(in);
                TextView hmtlView = findViewById(R.id.hmtlTxt);
                hmtlView.setText(htmlText);

            }catch (Exception e){

            }
    }
}
