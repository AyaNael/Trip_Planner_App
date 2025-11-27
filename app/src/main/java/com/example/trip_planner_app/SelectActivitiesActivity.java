package com.example.trip_planner_app;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectActivitiesActivity extends AppCompatActivity {

    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_activities);
        setUpBackBt();

    }
    private void setupViews() {
        btnBack = findViewById(R.id.btnBack);
    }
    public void setUpBackBt(){
        btnBack.setOnClickListener(v -> finish());

    }


}