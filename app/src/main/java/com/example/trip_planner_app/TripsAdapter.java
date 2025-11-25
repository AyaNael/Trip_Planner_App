package com.example.trip_planner_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder>{

    private ArrayList<Trip> tripList = new ArrayList<>();
    private Context context;

    public TripsAdapter(Context context, ArrayList<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_trip, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        Trip trip = tripList.get(position);

        TextView txtTitle = cardView.findViewById(R.id.tripTitle);
        TextView txtTo    = cardView.findViewById(R.id.tripTo);
        TextView txtDate  = cardView.findViewById(R.id.tripDate);
        TextView txtType  = cardView.findViewById(R.id.tripType);

        txtTitle.setText(trip.getTitle());
        txtTo.setText(trip.getDestination());
        txtDate.setText(trip.getDate());
        txtType.setText(trip.getType());

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddTripActivity.class);
            intent.putExtra("title", trip.getTitle());
            intent.putExtra("dest", trip.getDestination());
            intent.putExtra("date", trip.getDate());
            intent.putExtra("days", trip.getDays());
            intent.putExtra("type", trip.getType());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Trip> newList){
        this.tripList = newList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }
    }
}
