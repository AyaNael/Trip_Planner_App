package com.example.trip_planner_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

public class SelectActivitiesActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout itemSwimming, itemSnowSport, itemWork, itemCamping, itemGym, itemPhotography,
            itemInternational, itemBeach, itemBaby, itemHiking,
            itemBicycle, itemRunning, itemDinner, itemMotorcycle;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "TripPrefs";
    private static final String DATA = "DATA";

    private int tripIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_activities);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        tripIndex = getIntent().getIntExtra("tripIndex", -1);

        setupViews();
        setUpBackBt();
        highlightSelectedActivities();


    }
    private void setupViews() {
        btnBack = findViewById(R.id.btnBack);
        itemSwimming      = findViewById(R.id.itemSwimming);
        itemSnowSport     = findViewById(R.id.itemSnowSport);
        itemWork          = findViewById(R.id.itemWork);
        itemCamping       = findViewById(R.id.itemCamping);
        itemGym           = findViewById(R.id.itemGym);
        itemPhotography   = findViewById(R.id.itemPhotography);
        itemInternational = findViewById(R.id.itemInternational);
        itemBeach         = findViewById(R.id.itemBeach);
        itemBaby          = findViewById(R.id.itemBaby);
        itemHiking        = findViewById(R.id.itemHiking);
        itemBicycle       = findViewById(R.id.itemBicycle);
        itemRunning       = findViewById(R.id.itemRunning);
        itemDinner        = findViewById(R.id.itemDinner);
        itemMotorcycle    = findViewById(R.id.itemMotorcycle);

        setupClickOnLayout(itemSwimming, "Swimming");
        setupClickOnLayout(itemSnowSport, "Snow Sports");
        setupClickOnLayout(itemWork, "Work");
        setupClickOnLayout(itemCamping, "Camping");
        setupClickOnLayout(itemGym, "Gym");
        setupClickOnLayout(itemPhotography, "Photography");
        setupClickOnLayout(itemInternational, "International");
        setupClickOnLayout(itemBeach, "Beach");
        setupClickOnLayout(itemBaby, "Baby");
        setupClickOnLayout(itemHiking, "Hiking");
        setupClickOnLayout(itemBicycle, "Bicycling");
        setupClickOnLayout(itemRunning, "Running");
        setupClickOnLayout(itemDinner, "Dinner");
        setupClickOnLayout(itemMotorcycle, "Motorcycle");


    }
    private void setupClickOnLayout(LinearLayout layout, String activityName) {
        layout.setOnClickListener(v -> {

            v.setBackgroundResource(R.drawable.activity_selected);
            openActivityDetails(activityName);
        });
    }
    public void setUpBackBt(){
        btnBack.setOnClickListener(v -> finish());

    }
    private void openActivityDetails(String activityName) {
        int index = getIntent().getIntExtra("tripIndex", -1);

        Intent intent = new Intent(this, ActivityDetailsActivity.class);
        intent.putExtra("activityName", activityName);
        intent.putExtra("tripIndex", index);

        intent.putExtra("tripStartDate", getIntent().getStringExtra("tripStartDate"));
        intent.putExtra("tripDays", getIntent().getIntExtra("tripDays", 1));


        startActivity(intent);
    }
    private void highlightSelectedActivities() {
        if (tripIndex == -1) return;

        String json = prefs.getString(DATA, "");
        if (json.isEmpty())
            return;

        Gson gson = new Gson();
        Trip[] tripsArray = gson.fromJson(json, Trip[].class);
        if (tripsArray == null || tripIndex >= tripsArray.length)
            return;


        Trip currentTrip = tripsArray[tripIndex];
        if (currentTrip.getActivities() == null)
            return;

        // go through al activities for te trip and highlight them
        for (TripActivity act : currentTrip.getActivities()) {
            markLayoutSelected(act.getActivityName());
        }
    }

    private void markLayoutSelected(String activityName) {
        if ("Swimming".equals(activityName)) {
            itemSwimming.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Snow Sports".equals(activityName)) {
            itemSnowSport.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Work".equals(activityName)) {
            itemWork.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Camping".equals(activityName)) {
            itemCamping.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Gym".equals(activityName)) {
            itemGym.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Photography".equals(activityName)) {
            itemPhotography.setBackgroundResource(R.drawable.activity_selected);
        } else if ("International".equals(activityName)) {
            itemInternational.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Beach".equals(activityName)) {
            itemBeach.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Baby".equals(activityName)) {
            itemBaby.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Hiking".equals(activityName)) {
            itemHiking.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Bicycling".equals(activityName)) {
            itemBicycle.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Running".equals(activityName)) {
            itemRunning.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Dinner".equals(activityName)) {
            itemDinner.setBackgroundResource(R.drawable.activity_selected);
        } else if ("Motorcycle".equals(activityName)) {
            itemMotorcycle.setBackgroundResource(R.drawable.activity_selected);
        }
    }


}