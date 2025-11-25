package com.example.trip_planner_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class MyTripsActivity extends AppCompatActivity {


    private EditText edtSearch;
    private ImageButton btnAddTrip;
    private RecyclerView rcyTrips;

    private ArrayList<Trip> tripsList = new ArrayList<>();
    private ArrayList<Trip> filteredList = new ArrayList<>();

    private TripsAdapter adapter;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static final String PREFS_NAME = "TripPrefs";
    private static final String DATA = "DATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trips_activity);
        setupViews();
        setupSharedPrefs();
        setupAddButton();
        setupRecyclerView();
        setupSearch();
        loadTripsFromPrefs();
    }

    private void setupViews() {
        edtSearch = findViewById(R.id.edtSearch);
        btnAddTrip = findViewById(R.id.btnAddTrip);
        rcyTrips = findViewById(R.id.rcyTrips);
    }

    private void setupSharedPrefs() {
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    private void setupRecyclerView() {
        rcyTrips.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TripsAdapter(MyTripsActivity.this, filteredList);
        rcyTrips.setAdapter(adapter);
    }

    private void setupSearch() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                filterTrips(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


        });
    }

    private void filterTrips(String query) {
        filteredList.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(tripsList);
        } else {
            String q = query.toLowerCase().trim();
            for (Trip t : tripsList) {
                if (t.getTitle().toLowerCase().contains(q) ||
                        t.getDestination().toLowerCase().contains(q) ||
                        t.getType().toLowerCase().contains(q)) {
                    filteredList.add(t);
                }
            }
        }
        adapter.updateList(filteredList);
    }

    private void setupAddButton() {
        btnAddTrip.setOnClickListener(v -> {
            Intent intent = new Intent(MyTripsActivity.this, AddTripActivity.class);
            startActivity(intent);
        });
    }

    private void loadTripsFromPrefs() {
        Gson gson = new Gson();
        tripsList.clear();

        String json = prefs.getString(DATA, "");
        if (!json.equals("")) {
            Trip[] arr = gson.fromJson(json, Trip[].class);
            tripsList.addAll(Arrays.asList(arr));
        }

        filteredList.clear();
        filteredList.addAll(tripsList);
        adapter.updateList(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTripsFromPrefs();
    }

}