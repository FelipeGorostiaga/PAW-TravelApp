package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripListDTO {

    List<TripDTO> trips = new ArrayList<>();

    public TripListDTO() {
        // Empty constructor needed by JAX-RS
    }

    public TripListDTO(List<Trip> list) {
        this.trips = new ArrayList<>();
        for (Trip trip : list) {
            this.trips.add(new TripDTO(trip));
        }
    }


    public List<TripDTO> getTrips() {
        return trips;
    }

    public void setTrips(List<TripDTO> trips) {
        this.trips = trips;
    }
}
