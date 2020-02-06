package center.claims.mirascon.mirascon.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import center.claims.mirascon.mirascon.Adapter.SupportAdapter;
import center.claims.mirascon.mirascon.Models.Support;
import center.claims.mirascon.mirascon.R;

public class MainActivity extends AppCompatActivity {

    List<Support> supportSelection;
    private Toolbar toolbar;
    private ImageView back;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TransitionInflater tf = TransitionInflater.from(this);
        Transition t =
                tf.inflateTransition(R.transition.fade_transition);
        getWindow().setExitTransition(t);

        back = findViewById(R.id.navigation_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnTouchListener(new View.OnTouchListener() {

            float startX;
            float startRawX;
            float distanceX;
            int lastAction;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = view.getX() - event.getRawX();
                        startRawX = event.getRawX();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.setX(event.getRawX() + startX);
                        view.setY(event.getRawY() + startX);

                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        distanceX = event.getRawX()-startRawX;
                        if (Math.abs(distanceX)< 20){
                            //Toast.makeText(MainActivity.this, "FAB CLICKED", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Chat.class);
                            // start te activity
                            startActivity(intent);
                        }
                        break;
                    case MotionEvent.ACTION_BUTTON_PRESS:

                    default:
                        return false;
                }
                return true;
            }
        });

        supportSelection = new ArrayList<>();
        supportSelection.add(new Support("Emergency Call", R.drawable.icon_emergency_weiss));
        supportSelection.add(new Support("Roadside\nAssistance", R.drawable.icon_roadsideassistance_weiss));
        supportSelection.add(new Support("Claims Center", R.drawable.icon_claims_weiss));
        supportSelection.add(new Support("Products", R.drawable.icon_products_weiss));

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        SupportAdapter myAdapter = new SupportAdapter(this, supportSelection);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setAdapter(myAdapter);
    }

    public void transitionActivity(final Class<? extends Activity> ActivityToOpen) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, null);
        Intent intent = new Intent(this, ClaimsCenter.class); //set a new Activity
        // start the activity
        startActivity(new Intent(MainActivity.this, ActivityToOpen), compat.toBundle());
    }

}
