package ar.edu.itba.paw.model;

import java.util.List;

public class TripPaginatedResult {

    private List<Trip> trips;

    private int totalTrips;


    public TripPaginatedResult(List<Trip> trips, int totalTrips) {
        this.trips = trips;
        this.totalTrips = totalTrips;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }
}
