package ar.edu.itba.paw.model;

import java.util.List;

public class PaginatedResult<T> {

    private List<T> trips;

    private int totalTrips;


    public PaginatedResult(List<T> trips, int totalTrips) {
        this.trips = trips;
        this.totalTrips = totalTrips;
    }

    public List<T> getTrips() {
        return trips;
    }

    public void setTrips(List<T> trips) {
        this.trips = trips;
    }

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }
}
