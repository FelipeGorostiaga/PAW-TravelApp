package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Trip;

import java.time.LocalDate;

public class TripDTO {

    private long id;
    private long adminId;
    private PlaceDTO startPlace;
    private boolean isPrivate;
    private int membersAmount;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    public TripDTO() {
        // Empty constructor needed by JAX-RS
    }

    public TripDTO(Trip trip, PlaceDTO place) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.description = trip.getDescription();
        this.startDate = trip.getStartDate();
        this.endDate = trip.getEndDate();
        this.adminId = trip.getAdminId();
        this.startPlace = place;
        this.isPrivate = trip.isPrivate();
    }

    public PlaceDTO getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(PlaceDTO startPlace) {
        this.startPlace = startPlace;
    }

    public int getMembersAmount() {
        return membersAmount;
    }

    public void setMembersAmount(int membersAmount) {
        this.membersAmount = membersAmount;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
