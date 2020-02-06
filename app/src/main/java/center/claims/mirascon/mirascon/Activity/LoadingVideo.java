package center.claims.mirascon.mirascon.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import center.claims.mirascon.mirascon.Animation.RevealAnimation;
import center.claims.mirascon.mirascon.R;

public class LoadingVideo extends AppCompatActivity {

    //create class reference
    private VideoView vid;
    private Intent intent;
    private String firstStart = "First";
    SharedPreferences prefs = null;
    private String path;
    private Uri u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingview);
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Width", "" + width);
        Log.e("height", "" + height);

        vid = findViewById(R.id.videoView);
        path = "android.resource://center.claims.mirascon.mirascon/" + R.raw.logoflieger1280;
        u = Uri.parse(path);
        vid.setVideoURI(u);
        prefs = getSharedPreferences("center.claims.mirascon.mirascon", MODE_PRIVATE);
        Log.v("PREFS", "" + prefs);

        vid.start();
        vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startRevealActivity(vid);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            vid.start();
            vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    intent = new Intent(LoadingVideo.this, IntroductionUI.class);
                    startActivity(intent);
                }
            });

        }
    }

    /*public SharedPreferences getPrefs() {
        return prefs;
    }*/

    private void startRevealActivity(View v) {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        intent.putExtra("ID", firstStart);
        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }
}
