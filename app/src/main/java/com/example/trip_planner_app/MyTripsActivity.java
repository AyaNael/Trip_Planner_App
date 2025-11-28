package com.example.trip_planner_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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

    private ArrayList<Trip> filteredList = new ArrayList<>();// List used for displaying results from search

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
        loadTrips();

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
        adapter = new TripsAdapter(filteredList);
        rcyTrips.setAdapter(adapter);
    }

    // Setup search box to filter trips while user types
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
    private void deleteTrip(Trip trip) {
        tripsList.remove(trip);

        filteredList.remove(trip);


        Gson gson = new Gson();
        String json = gson.toJson(tripsList);
        editor.putString(DATA, json);
        editor.apply();
    }

    // filter trips by title, destination, or type based on user input
    private void filterTrips(String str) {
        filteredList.clear();
        if (str == null || str.trim().isEmpty()) {
            filteredList.addAll(tripsList);
        } else {
            String searchedStr = str.toLowerCase().trim();
            for (Trip trip : tripsList) {
                if (trip.getTitle().toLowerCase().contains(searchedStr) ||
                        trip.getDestination().toLowerCase().contains(searchedStr) ||
                        trip.getType().toLowerCase().contains(searchedStr)) {
                    filteredList.add(trip);
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

    private void loadTrips() {// load trips from shared preference
        Gson gson = new Gson();
        tripsList.clear();

        String jsonStr = prefs.getString(DATA, "");
        if (!jsonStr.equals("")) {
            Trip[] arr = gson.fromJson(jsonStr, Trip[].class);
            tripsList.addAll(Arrays.asList(arr));
        }
        // Copy all trips to filtered list before any search
        filteredList.clear();
        filteredList.addAll(tripsList);

        adapter.updateList(filteredList);
    }

    // When returning to this screen, reload the trips in case user added/edited/deleted a trip
    @Override
    protected void onResume() {
        super.onResume();
        loadTrips();
    }

}