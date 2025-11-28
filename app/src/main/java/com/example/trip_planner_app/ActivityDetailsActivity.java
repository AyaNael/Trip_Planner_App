package com.example.trip_planner_app;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
    private long minDate;
    private long maxDate;
    private String selectedTime = null;

    private SharedPreferences prefs ;
    private SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_details);
        setUpViews();
        setupDatePicker();
        setupRadioButtons();
        setupSaveButton();
    }
    public void setUpViews(){
        txtActivityName = findViewById(R.id.txtActivityName);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        rgPeriodOfDay = findViewById(R.id.rgPeriodOfDay);

        edtNotes = findViewById(R.id.edtNotes);
        btnSaveActivity = findViewById(R.id.btnSaveActivity);

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
           if (selectedTime.isEmpty()) {
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
            prefs = getSharedPreferences("TripPrefs", MODE_PRIVATE);
           Gson gson = new Gson();

           String json = prefs.getString("DATA", "");
           Trip[] tripsArray = gson.fromJson(json, Trip[].class);

           ArrayList<Trip> trips = new ArrayList<>();
           if (tripsArray != null) {
               trips.addAll(Arrays.asList(tripsArray));
           }

           int index = getIntent().getIntExtra("tripIndex", -1);
           if (index != -1 && index < trips.size()) {
               trips.get(index).addActivity(newActivity);
               String updatedStr = gson.toJson(trips);
               editor.putString("DATA", updatedStr).apply();

               Toast.makeText(this, "Activity added to trip!", Toast.LENGTH_SHORT).show();
           }
           finish();
           });
    }
}