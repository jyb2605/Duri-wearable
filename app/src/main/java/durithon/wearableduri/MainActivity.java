package durithon.wearableduri;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    

    public static char ascii = (char)0x2593;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this , ShakeService.class);
        this.startService(serviceIntent);
        new MusicListUtil();












































    }
}
