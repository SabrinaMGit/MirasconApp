package center.claims.mirascon.mirascon.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import center.claims.mirascon.mirascon.Adapter.ClaimsCenterAdapter;
import center.claims.mirascon.mirascon.Adapter.EmergencyAdapter;
import center.claims.mirascon.mirascon.Adapter.SupportAdapter;
import center.claims.mirascon.mirascon.Interface.OnTaskCompletedCallback;
import center.claims.mirascon.mirascon.Models.Support;
import center.claims.mirascon.mirascon.Models.User;
import center.claims.mirascon.mirascon.R;
import center.claims.mirascon.mirascon.SQL.DatabaseHelper;

public class ClaimsCenter extends AppCompatActivity {

    List<Support> supportSelection;
    private Toolbar toolbar;
    private ImageView back;
    List<User> listUsers;
    DatabaseHelper databaseHelper;
    User user = new User();

    private String l_name_text;
    private String lp_text;
    private String customer_id_text;

    private TextView customer_id;
    private TextView lp;
    private TextView l_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claimscenter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);
        listUsers = new ArrayList<>();

        customer_id = findViewById(R.id.customer_id);
        lp = findViewById(R.id.lp);
        l_name = findViewById(R.id.l_name_id);

        Slide s = new Slide();
        s.setDuration(400);
        getWindow().setEnterTransition(s);

        TransitionInflater tf = TransitionInflater.from(this);
        Transition t =
                tf.inflateTransition(R.transition.fade_transition);
        getWindow().setExitTransition(t);

        back = findViewById(R.id.navigation_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getDataFromSQLite(new OnTaskCompletedCallback() {
            @Override
            public void onTaskCompleted() {
                Log.v("data", "On Task Completed. I am there!");
                User foundUser = findUser();
                if (foundUser != null) {
                    l_name_text = foundUser.getLast_name();
                    l_name.setText("Family Name: "+l_name_text);
                    lp_text = foundUser.getLp();
                    lp.setText("License Plate: "+lp_text);
                    customer_id_text = foundUser.getCustome_id();
                    customer_id.setText("Customer Identification: "+customer_id_text);
                }
            }
        });

        supportSelection = new ArrayList<>();
        supportSelection.add(new Support("Fill out the Claims Form", R.drawable.icon_fillclaimsform_weiss));
        supportSelection.add(new Support("Photos camera /\nUpload", R.drawable.ic_photo_camera));
        supportSelection.add(new Support("Other Info\nMP/ Police Report etc.)", R.drawable.ic_info));
        supportSelection.add(new Support("Glass Damage", R.drawable.icon_glassdamage_weiss));

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        ClaimsCenterAdapter myAdapter = new ClaimsCenterAdapter(this, supportSelection);
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(myAdapter);
    }

    public void transitionActivity(final Class<? extends Activity> ActivityToOpen) {
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(ClaimsCenter.this, null);
        // start the activity
        startActivity(new Intent(ClaimsCenter.this, ActivityToOpen), compat.toBundle());
    }

    /**
     * @return User
     */
    public User findUser() {
        for (User user : listUsers) {
            return user;
        }

        return null;
    }

    /**
     * This method is to fetch all user records from SQLite
     *
     * @param callback
     */
    public void getDataFromSQLite(final OnTaskCompletedCallback callback) {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            private OnTaskCompletedCallback listener = callback;

            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(databaseHelper.getAllUser());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //adapter.notifyDataSetChanged();
                if (listUsers != null && !listUsers.isEmpty()) {
                    listener.onTaskCompleted();
                } else {
                    Log.v("ERROR", "No user available!");
                }
            }
        }.execute();
    }
}