package com.example.trip_planner_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddTripActivity extends AppCompatActivity {

    private EditText edtTripTitle, edtTripTo;
    private TextView txtSelectedDate, txtDaysBubble;
    private SeekBar seekDays;
    private LinearLayout layoutBusiness, layoutLeisure;
    private Button btnSelectActivities, btnSaveTrip;

    private String selectedType = "Leisure";
    private boolean isEditing = false;

    private String originalTitle;
    private String originalDest;
    private String originalDate;
    private int originalDays;
    private String originalType;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson;
    public static final String DATA = "DATA";
    private static final String PREFS_NAME = "TripPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_trip_activity);
        setupViews();
        setupSharedPrefs();
        setupSeekBar();
        setupTypeSelection();
        loadTripIfEditing();
        setupSaveButton();

    }

    private void setupViews() {
        edtTripTitle = findViewById(R.id.edtTripTitle);
        edtTripTo = findViewById(R.id.edtTripTo);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtDaysBubble = findViewById(R.id.txtDaysBubble);
        seekDays = findViewById(R.id.seekDays);
        layoutBusiness = findViewById(R.id.layoutBusiness);
        layoutLeisure = findViewById(R.id.layoutLeisure);
        btnSelectActivities = findViewById(R.id.btnSelectActivities);
        btnSaveTrip = findViewById(R.id.btnSaveTrip);
    }
    private void setupSharedPrefs() {
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
        gson = new Gson();
    }

    private void setupSeekBar() {
        txtDaysBubble.setText(String.valueOf(seekDays.getProgress()));

        seekDays.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) progress = 1;
                txtDaysBubble.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setupTypeSelection() {

        int selectedColor = Color.parseColor("#D0E8FF");
        int defaultColor = Color.parseColor("#FFFFFF");

        layoutBusiness.setOnClickListener(v -> {
            selectedType = "Business";
            layoutBusiness.setBackgroundColor(selectedColor);
            layoutLeisure.setBackgroundColor(defaultColor);
        });

        layoutLeisure.setOnClickListener(v -> {
            selectedType = "Leisure";
            layoutBusiness.setBackgroundColor(defaultColor);
            layoutLeisure.setBackgroundColor(selectedColor);
        });
    }

    private void loadTripIfEditing() {
        String title = getIntent().getStringExtra("title");
        String dest = getIntent().getStringExtra("dest");
        String date = getIntent().getStringExtra("date");
        int days = getIntent().getIntExtra("days", 5);
        String type = getIntent().getStringExtra("type");

        if (title != null) {
                isEditing = true;

                originalTitle = title;
                originalDest  = dest;
                originalDate  = date;
                originalDays  = days;
                originalType  = type;

                edtTripTitle.setText(title);
                edtTripTo.setText(dest);
                txtSelectedDate.setText(date);
                seekDays.setProgress(days);
                txtDaysBubble.setText(String.valueOf(days));

            if (type != null) {
                selectedType = type;
                int selectedColor = Color.parseColor("#D0E8FF");
                int defaultColor  = Color.parseColor("#FFFFFF");

                if (type.equals("Business")) {
                    layoutBusiness.setBackgroundColor(selectedColor);
                    layoutLeisure.setBackgroundColor(defaultColor);
                } else {
                    layoutBusiness.setBackgroundColor(defaultColor);
                    layoutLeisure.setBackgroundColor(selectedColor);
                }
            }
            }
        }
    private void setupSaveButton() {
        btnSaveTrip.setOnClickListener(v -> {
            saveTripToPrefs();
        });
    }

    private void saveTripToPrefs() {
        String title = edtTripTitle.getText().toString().trim();
        String dest  = edtTripTo.getText().toString().trim();
        String date  = txtSelectedDate.getText().toString().trim();
        int days     = seekDays.getProgress();
        if (days == 0) days = 1;

        if (title.isEmpty() || dest.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();

            return;
        }

        Trip newTrip = new Trip(title, dest, date, days, selectedType);

        String json = prefs.getString(DATA, "");
        List<Trip> list = new ArrayList<>();

        if (!json.equals("")) {
            Trip[] arr = gson.fromJson(json, Trip[].class);
            list.addAll(Arrays.asList(arr));
        }

        if (isEditing) {
            boolean updated = false;
            for (int i = 0; i < list.size(); i++) {
                Trip t = list.get(i);
                if (t.getTitle().equals(originalTitle) &&
                        t.getDestination().equals(originalDest) &&
                        t.getDate().equals(originalDate) &&
                        t.getDays() == originalDays &&
                        t.getType().equals(originalType)) {

                    list.set(i, newTrip);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                list.add(newTrip);
            }
        } else {
            list.add(newTrip);
        }

        String strUpdated = gson.toJson(list);
        editor.putString(DATA, strUpdated);
        editor.commit();
    }

    public void setUpSelectActivitiesButton(){
        btnSelectActivities.setOnClickListener(v -> {
            saveTripToPrefs(); // ensure the last update saved even if the user dont pres son save btn

            Intent i = new Intent(AddTripActivity.this, SelectActivitiesActivity.class);
            startActivity(i);
        });

    }



}


