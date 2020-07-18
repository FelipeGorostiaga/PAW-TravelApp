package ar.edu.itba.paw.webapp.dto;

import java.util.ArrayList;
import java.util.List;

public class TripListListDTO {

    List<TripListDTO> trips = new ArrayList<>();

    public TripListListDTO() {

    }

    public TripListListDTO(TripListDTO list) {
        this.trips.add(list);
    }

    public void addList(TripListDTO tripPage) {
        this.trips.add(tripPage);
    }

    public List<TripListDTO> getTrips() {
        return trips;
    }

    public void setTrips(List<TripListDTO> trips) {
        this.trips = trips;
    }
}
