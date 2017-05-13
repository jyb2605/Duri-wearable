package durithon.wearableduri;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static Netty_DuriClient netty;

    public static char ascii = (char)0x2593;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        netty = new Netty_DuriClient(this);
        netty.start();
        Intent serviceIntent = new Intent(this , ShakeService.class);
        this.startService(serviceIntent);
        new MusicListUtil();

        MediaPlayer mediaPlayer = MediaPlayer.create(this , MusicListUtil.신나는음악리스트.get(0).sound);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you






    }
}
