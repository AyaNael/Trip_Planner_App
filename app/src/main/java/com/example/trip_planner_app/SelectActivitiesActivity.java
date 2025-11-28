package com.example.trip_planner_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectActivitiesActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout itemSwimming, itemSnowSport, itemWork, itemCamping, itemGym, itemPhotography,
            itemInternational, itemBeach, itemBaby, itemHiking,
            itemBicycle, itemRunning, itemDinner, itemMotorcycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_activities);
        setupViews();
        setUpBackBt();

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


}