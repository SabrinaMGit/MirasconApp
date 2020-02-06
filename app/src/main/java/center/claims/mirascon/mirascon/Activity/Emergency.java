package center.claims.mirascon.mirascon.Activity;

import android.Manifest;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.transition.Slide;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import center.claims.mirascon.mirascon.Adapter.EmergencyAdapter;
import center.claims.mirascon.mirascon.Models.Support;
import center.claims.mirascon.mirascon.R;

public class Emergency extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView back;
    List<Support> supportSelection;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back = findViewById(R.id.navigation_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Slide s = new Slide();
        s.setDuration(400);
        getWindow().setEnterTransition(s);

        checkSelfPermission();

        supportSelection = new ArrayList<>();
        supportSelection.add(new Support("Police 110", R.drawable.icon_police_weiss));
        supportSelection.add(new Support("Fire Department 112", R.drawable.icon_firedepartment_weiss));
        supportSelection.add(new Support("0800 Mirascon", R.drawable.icon_mirascon_weiss));

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        EmergencyAdapter myAdapter = new EmergencyAdapter(this, supportSelection);
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(myAdapter);
    }

    public void checkSelfPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }



}
