package center.claims.mirascon.mirascon.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import center.claims.mirascon.mirascon.Models.User;
import center.claims.mirascon.mirascon.Interface.OnTaskCompletedCallback;
import center.claims.mirascon.mirascon.R;
import center.claims.mirascon.mirascon.SQL.DatabaseHelper;

public class ClaimsForm extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private Toolbar toolbar;
    private ImageView back;

    private EditText first_name;
    private EditText last_name;
    private EditText lp;
    private EditText custome_id;
    private EditText phone;
    private EditText email;

    private String f_name_text;
    private String l_name_text;
    private String lp_text;
    private String customer_id_text;
    private String phone_text;
    private String email_text;
    SharedPreferences prefs;

    List<User> listUsers;
    DatabaseHelper databaseHelper;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claimsform);
        initView();
        initListeners();
        setSupportActionBar(toolbar);
        ZoomSplashScreen zoomSplashScreen = new ZoomSplashScreen();
        prefs = zoomSplashScreen.getPrefs();
        Slide s = new Slide();
        s.setDuration(400);
        getWindow().setEnterTransition(s);
        databaseHelper = new DatabaseHelper(this);
        listUsers = new ArrayList<>();
        prefs = getSharedPreferences("center.claims.mirascon.mirascon", MODE_PRIVATE);
        if (!prefs.getBoolean("firstrun", true)) {
            Log.v("data", "start get all data!");
            getAllDataFromDatabase();
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        back = findViewById(R.id.navigation_back);
        button = findViewById(R.id.btnContinues);
        first_name = findViewById(R.id.first_name_edittext);
        last_name = findViewById(R.id.last_name_edittext);
        lp = findViewById(R.id.lp_edittext);
        custome_id = findViewById(R.id.customer_id_edittext);
        phone = findViewById(R.id.mobile_edittext);
        email = findViewById(R.id.email_edittext);

    }

    private void initListeners() {
        back.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation_back:
                finish();
                break;
            case R.id.btnContinues:
                if (checkRequieredFields() == true) {
                    if (prefs.getBoolean("firstrun", true)) {
                        // Do first run stuff here then set 'firstrun' as false
                        // using the following line to edit/commit prefs
                        saveAllDataToDatabase();
                        prefs.edit().putBoolean("firstrun", false).apply();
                        intentMain();
                    } else {
                        intentGoogleMaps(f_name_text, l_name_text, lp_text, customer_id_text, phone_text, email_text);
                    }
                }
        }
    }

    private boolean checkRequieredFields() {
        if (first_name.getText().toString().length() == 0) {
            first_name.setError("First name is required!");

            if (last_name.getText().toString().length() == 0) {
                last_name.setError("Last Name is requiered!");

                if (lp.getText().toString().length() == 0) {
                    lp.setError("License Plate is requiered!");

                    if (custome_id.getText().toString().length() == 0 && custome_id.getText().length() < 4) {
                        custome_id.setError("Customer ID is requiered!");

                        if (phone.getText().toString().length() == 0) {
                            phone.setError("Phone Number is requiered!");

                            if (email.getText().toString().length() == 0) {
                                email.setError("E-Mail is requiered!");

                                if (isEmailValid(email.getText().toString()) == false) {
                                    email.setError("Invalid Email Address");
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    private void saveAllDataToDatabase() {
        f_name_text = first_name.getText().toString();
        user.setFirst_name(f_name_text);

        l_name_text = last_name.getText().toString();
        user.setLast_name(l_name_text);

        lp_text = lp.getText().toString();
        user.setLp(lp_text);

        customer_id_text = custome_id.getText().toString();
        user.setCustome_id(customer_id_text);

        phone_text = phone.getText().toString();
        user.setPhone(phone_text);

        email_text = email.getText().toString();
        user.setEmail(email_text);

        databaseHelper.addUser(user);
        /*if (!isEmailValid(email_text)) {
            email.setError("Your Email is Invalid.");
        }*/
    }

    private void getAllDataFromDatabase() {
        Log.v("data", "I am in the method!");
        getDataFromSQLite(new OnTaskCompletedCallback() {
            @Override
            public void onTaskCompleted() {
                Log.v("data", "On Task Completed. I am there!");
                User foundUser = findUser();
                if (foundUser != null) {
                    f_name_text = foundUser.getFirst_name();
                    Log.v("data", "" + f_name_text);
                    first_name.setText(f_name_text);
                    l_name_text = foundUser.getLast_name();
                    last_name.setText(l_name_text);
                    lp_text = foundUser.getLp();
                    lp.setText(lp_text);
                    customer_id_text = foundUser.getCustome_id();
                    custome_id.setText(customer_id_text);
                    phone_text = foundUser.getPhone();
                    phone.setText(phone_text);
                    email_text = foundUser.getEmail();
                    email.setText(email_text);
                }
            }
        });
    }

    private void intentMain() {
        Intent intent = new Intent(ClaimsForm.this, MainActivity.class);
        startActivity(intent);
    }

    private void intentGoogleMaps(String f_name, String l_name, String lp, String customer_id, String phone, String email) {
        Intent intent = new Intent(ClaimsForm.this, GoogleMaps.class);
        intent.putExtra("fName", f_name);
        intent.putExtra("lName", l_name);
        intent.putExtra("lp", lp);
        intent.putExtra("customer", customer_id);
        intent.putExtra("phone", phone);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validCellPhone(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
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
