package com.example.trip_planner_app;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityDetailsActivity extends AppCompatActivity {

    private TextView txtActivityName, txtSelectedDate;
    private RadioGroup rgPeriodOfDay;
    private EditText edtNotes;
    private Button btnSaveActivity;

    private ImageView btnBack;
    private long minDate;
    private long maxDate;
    private String selectedTime = null;

    private SharedPreferences prefs ;
    private SharedPreferences.Editor editor ;
    private static final String PREFS_NAME = "TripPrefs";
    private static final String DATA = "DATA";
    private int tripIndex = -1;
    private int activityIndex = -1;
    private boolean isEditing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_details);
        setUpViews();
        setupSharedPrefs();
        setupDatePicker();
        setupRadioButtons();
        setupSaveButton();
        loadActivityIfEditing();
        setUpBackBt();
    }
    public void setUpViews(){
        txtActivityName = findViewById(R.id.txtActivityName);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        rgPeriodOfDay = findViewById(R.id.rgPeriodOfDay);

        edtNotes = findViewById(R.id.edtNotes);
        btnSaveActivity = findViewById(R.id.btnSaveActivity);
        btnBack = findViewById(R.id.btnBack);


        String activityName = getIntent().getStringExtra("activityName");
        txtActivityName.setText(activityName);

        String tripStartDate = getIntent().getStringExtra("tripStartDate");
        int tripDays = getIntent().getIntExtra("tripDays", 1);

        if (tripStartDate != null && !tripStartDate.isEmpty()) {

            String[] parts = tripStartDate.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int year = Integer.parseInt(parts[2]);

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month, day, 0, 0, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);

            minDate = cal.getTimeInMillis();

            cal.add(java.util.Calendar.DAY_OF_MONTH, tripDays - 1);
            maxDate = cal.getTimeInMillis();
        }



    }
    private void loadActivityIfEditing() {
        tripIndex = getIntent().getIntExtra("tripIndex", -1);
        String activityName = getIntent().getStringExtra("activityName");

        if (tripIndex == -1 || activityName == null) {
            return;//new activity
        }

            Gson gson = new Gson();
            String json = prefs.getString(DATA, "");
            Trip[] tripsArray = gson.fromJson(json, Trip[].class);

        if (tripsArray == null || tripIndex >= tripsArray.length) {
            return;
        }

        Trip trip = tripsArray[tripIndex];

        if (trip.getActivities() == null || trip.getActivities().isEmpty()) {
            return;
        }

        for (int i = 0; i < trip.getActivities().size(); i++) {
            TripActivity oldAct = trip.getActivities().get(i);

            if (activityName.equals(oldAct.getActivityName())) {

                isEditing = true;
                activityIndex = i;

                txtActivityName.setText(oldAct.getActivityName());
                txtSelectedDate.setText(oldAct.getDate());
                edtNotes.setText(oldAct.getNotes());

                selectedTime = oldAct.getPeriodOfDay();
                if ("Morning".equals(selectedTime)) {
                    rgPeriodOfDay.check(R.id.rdBtnMorning);
                } else if ("Afternoon".equals(selectedTime)) {
                    rgPeriodOfDay.check(R.id.rdBtnAfternoon);
                } else if ("Evening".equals(selectedTime)) {
                    rgPeriodOfDay.check(R.id.rdBtnEvening);
                }

                break; // matching activity found
            }
        }
    }
    private void setupSharedPrefs() {
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    private void setupRadioButtons() {
        rgPeriodOfDay.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdBtnMorning) {
                selectedTime = "Morning";
            } else if (checkedId == R.id.rdBtnAfternoon) {
                selectedTime = "Afternoon";
            } else if (checkedId == R.id.rdBtnEvening) {
                selectedTime = "Evening";
            }
        });
    }
        private void setupDatePicker() {
        txtSelectedDate.setOnClickListener(v -> {
            java.util.Calendar c = java.util.Calendar.getInstance();
            int year = c.get(java.util.Calendar.YEAR);
            int month = c.get(java.util.Calendar.MONTH);
            int day = c.get(java.util.Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    ActivityDetailsActivity.this,
                    (view, y, m, d) -> {
                        String dateStr = d + "/" + (m + 1) + "/" + y;
                        txtSelectedDate.setText(dateStr);
                    },
                    year, month, day
            );
            if (minDate > 0) {
                dialog.getDatePicker().setMinDate(minDate);
            }
            if (maxDate > 0) {
                dialog.getDatePicker().setMaxDate(maxDate);
            }

            dialog.show();
        });
    }
    private void setupSaveButton() {
       btnSaveActivity.setOnClickListener(v->{

     String dateStr = txtSelectedDate.getText().toString().trim();
     String notes = edtNotes.getText().toString().trim();

     if (dateStr.isEmpty() || dateStr.equals(getString(R.string.select_date))) {
         android.widget.Toast.makeText(
                 this, "Please Select Date for this Activity",android.widget.Toast.LENGTH_SHORT).show();
         return;
     }
           if (selectedTime == null || selectedTime.isEmpty() ) {
               android.widget.Toast.makeText(
                       this,
                       "Please Choose Time of Day (Morning/ Evening/ Afternoon) ",
                       android.widget.Toast.LENGTH_SHORT
               ).show();
               return;
           }
           TripActivity newActivity = new TripActivity(txtActivityName.getText().toString(),
                   dateStr,
                   selectedTime,
                   notes
           );
           Gson gson = new Gson();

           String json = prefs.getString("DATA", "");
           Trip[] tripsArray = gson.fromJson(json, Trip[].class);

           ArrayList<Trip> trips = new ArrayList<>();
           if (tripsArray != null) {
               trips.addAll(Arrays.asList(tripsArray));
           }

           if (tripIndex != -1 && tripIndex < trips.size()) {
               //check that no activities in the same date and time
               Trip currentTrip = trips.get(tripIndex);
               if (currentTrip.getActivities() != null) {
                   for (int i = 0; i < currentTrip.getActivities().size(); i++) {
                       TripActivity existing = currentTrip.getActivities().get(i);

                       //The same Activity we are editing it so skip it
                       if (isEditing && i == activityIndex)
                           continue;

                       if (existing.getDate().equals(dateStr) &&
                               existing.getPeriodOfDay().equals(selectedTime)) {
                           Toast.makeText(this,
                                   "You already have an activity at this date and time.", Toast.LENGTH_SHORT).show();
                           return;
                       }
                   }
               }

               if (isEditing && activityIndex != -1 &&
                       activityIndex < trips.get(tripIndex).getActivities().size()) {
                   trips.get(tripIndex).getActivities().set(activityIndex, newActivity);
               } else {
                   trips.get(tripIndex).addActivity(newActivity);
               }

               String updatedStr = gson.toJson(trips);
               editor.putString(DATA, updatedStr);
               editor.apply();

               Toast.makeText(this,
                       isEditing ? "Activity updated!" : "Activity added to trip!",
                       Toast.LENGTH_SHORT).show();
           } else {
               Toast.makeText(this, "Trip not found!", Toast.LENGTH_SHORT).show();
           }
           finish();
           });
    }
    public void setUpBackBt(){
        btnBack.setOnClickListener(view -> finish());

    }

}