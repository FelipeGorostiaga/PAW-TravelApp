package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripStatus;

import java.time.LocalDate;

public class TripDTO {

    private long id;
    private PlaceDTO startPlace;
    private boolean isPrivate;
    private int membersAmount;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private boolean hasImage;

    public TripDTO() {
        // Empty constructor needed by JAX-RS
    }

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.description = trip.getDescription();
        this.startDate = DateManipulation.changeDateFormat(trip.getStartDate());
        this.endDate = DateManipulation.changeDateFormat(trip.getEndDate());
        this.startPlace = new PlaceDTO(trip.getStartPlace());
        this.isPrivate = trip.isPrivate();
        this.membersAmount = trip.getMembers().size();
        this.hasImage = trip.getProfilePicture() != null;
        TripStatus status = trip.getStatus();
        if (!status.equals(TripStatus.COMPLETED)) {
            LocalDate now = LocalDate.now();
            if (now.isAfter(trip.getStartDate()) || now.isEqual(trip.getStartDate())) {
                this.status = TripStatus.IN_PROGRESS.name();
                return;
            }
        }
        this.status = trip.getStatus().name();
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
