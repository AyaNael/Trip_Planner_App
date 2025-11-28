package com.example.trip_planner_app;

public class TripActivity {

    private String activityName;
    private String date;
    private String periodOfDay;
    private String notes;

    public TripActivity(String activityName, String date, String periodOfDay, String notes) {
        this.activityName = activityName;
        this.date = date;
        this.periodOfDay = periodOfDay;
        this.notes = notes;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getDate() {
        return date;
    }

    public String getPeriodOfDay() {
        return periodOfDay;
    }

    public String getNotes() {
        return notes;
    }
}
