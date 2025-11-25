package com.example.trip_planner_app;

public class Trip {
    private String title;
    private String destination;
    private String date;
    private int days;
    private String type;

    public Trip(String title, String destination, String date, int days, String type) {
        this.title = title;
        this.destination = destination;
        this.date = date;
        this.days = days;
        this.type = type;
    }

    public String getTitle() { return title; }
    public String getDestination() { return destination; }
    public String getDate() { return date; }
    public int getDays() { return days; }
    public String getType() { return type; }



}
