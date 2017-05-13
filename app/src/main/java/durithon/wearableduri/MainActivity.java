package durithon.wearableduri;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //gps 정보 받기
    private LocationManager mLocationManager;
    Timer timer;
    public static final int ACCURACY_DECAYS_TIME = 3; // Metres per second
    private KalmanFilter kalmanLatLong = new KalmanFilter(ACCURACY_DECAYS_TIME);
    double Lat;
    double Lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!gpsIsOpen())
            return;

        get_gps();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //GPS상태
    private boolean gpsIsOpen() {
        boolean bRet = true;

        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (!alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please, turn on GPS!", Toast.LENGTH_SHORT).show();
            bRet = false;
        } else {
            Toast.makeText(this, "GPS has turned on!", Toast.LENGTH_SHORT).show();
        }

        return bRet;
    }

    public void get_gps() {

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        String provider = mLocationManager.getBestProvider(new Criteria(), true); //GPS 정보
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //mLocationManager.requestLocationUpdates(provider, 3 * 1000, 0, locationListener);
    }



    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location != null) {

                kalmanLatLong.Process(
                        location.getLatitude(),
                        location.getLongitude(),
                        location.getAccuracy(),
                        location.getTime());

                location.setLatitude(kalmanLatLong.get_lat());
                location.setLongitude(kalmanLatLong.get_lng());
                location.setAccuracy(kalmanLatLong.get_accuracy());

                Lat=kalmanLatLong.get_lat();
                Lon=kalmanLatLong.get_lng();


      //Toast.makeText(MainActivity.this, "lat : " + location.getLatitude() + "long"+ location.getLongitude(), Toast.LENGTH_SHORT).show();
        Log.d("gps","lat : " + Lat + "long"+ Lon);
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };









}