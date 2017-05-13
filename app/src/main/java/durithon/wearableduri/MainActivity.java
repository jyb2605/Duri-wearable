package durithon.wearableduri;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static Netty_DuriClient netty;
    static MediaPlayer mediaPlayer;
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

<<<<<<< HEAD
        MediaPlayer mediaPlayer = MediaPlayer.create(this , MusicListUtil.신나는음악리스트.get(0).sound);
=======
        MusicListUtil.current_check=true;
        mediaPlayer = MediaPlayer.create(this , MusicListUtil.신나는음악리스트.get(0).sound);
>>>>>>> 84bc546a9fb0f79579e8c1b424219dd1457aacca
        mediaPlayer.start(); // no need to call prepare(); create() does that for you






    }
}
