package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class TripListDTO {


    private List<TripDTO> trips;
    private int totalAmount;
    private int maxPage;

    public TripListDTO() {

    }

    public TripListDTO(List<TripDTO> tripDTOS, int totalAmount, int maxPage) {
        this.trips = tripDTOS;
        this.totalAmount = totalAmount;
        this.maxPage = maxPage;
    }

    public List<TripDTO> getTrips() {
        return trips;
    }

    public void setTrips(List<TripDTO> trips) {
        this.trips = trips;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
}
