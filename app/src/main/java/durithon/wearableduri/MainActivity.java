package durithon.wearableduri;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    static Netty_DuriClient netty;
    static MediaPlayer mediaPlayer;
    public static char ascii = (char)0x2593;

    //gps 정보 받기
    private LocationManager mLocationManager;
    Timer timer;
    public static final int ACCURACY_DECAYS_TIME = 3; // Metres per second
    private KalmanFilter kalmanLatLong = new KalmanFilter(ACCURACY_DECAYS_TIME);
    double Lat;
    double Lon;

//    MyReceiver myReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        netty = new Netty_DuriClient(this);
        netty.start();


        if (!gpsIsOpen())
            return;

        get_gps();


        Intent serviceIntent = new Intent(this , ShakeService.class);
        this.startService(serviceIntent);
        new MusicListUtil();

        MusicListUtil.current_check=true;
        mediaPlayer = MediaPlayer.create(this , MusicListUtil.신나는음악리스트.get(0).sound);
        mediaPlayer.start(); // no need to call prepare() ; create() does that for you

//        registerReceiver();

    }


    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(myReceiver);
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

//        netty = new Netty_DuriClient(this);
//        netty.start();
        Intent serviceIntent = new Intent(this , ShakeService.class);
        this.startService(serviceIntent);
        new MusicListUtil();

        MusicListUtil.current_check=true;
        mediaPlayer = MediaPlayer.create(this , MusicListUtil.신나는음악리스트.get(0).sound);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you


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

//                MainActivity.netty.sendmessage("latlon"+MainActivity.ascii+Lat+MainActivity.ascii+Lon);

      //Toast.makeText(MainActivity.this, "lat : " + location.getLatitude() + "long"+ location.getLongitude(), Toast.LENGTH_SHORT).show();
                  Log.d("gps","lat : " + Lat + "long"+ Lon);
                MainActivity.netty.sendmessage("latlon"+MainActivity.ascii+String.valueOf(Lat)+MainActivity.ascii+String.valueOf(Lon));
            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };


//    private void registerReceiver(){
//        //Register BroadcastReceiver
//        //to receive event from our service
//        myReceiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Netty_DuriHandler.SENDMESAGGE);
//        registerReceiver(myReceiver, intentFilter);
//    }

//    private class MyReceiver extends BroadcastReceiver {

//        @Override
//        public void onReceive(Context arg0, Intent arg1) {
//            //verify if the extra var exist
//            System.out.println(arg1.hasExtra("message")); // true or false
//            //another example...
//            System.out.println(arg1.getExtras().containsKey("message")); // true or false
//            //if var exist only print or do some stuff
//            if (arg1.hasExtra("message")) {
//                //do what you want to
//                if (arg1.getStringExtra("message").equals("start")) {
//                    Toast.makeText(MainActivity.this, "시작", Toast.LENGTH_SHORT).show();
//                }else if (arg1.getStringExtra("message").equals("resume")){
//                   Toast.makeText(MainActivity.this, "재시작", Toast.LENGTH_SHORT).show();
//                }else if(arg1.getStringExtra("message").equals("stop")){
//                    Toast.makeText(MainActivity.this, "일시정지", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        }

    }




