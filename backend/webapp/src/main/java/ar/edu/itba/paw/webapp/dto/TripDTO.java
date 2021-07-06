package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.utils.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripStatus;

import java.net.URI;
import java.time.LocalDate;

public class TripDTO {

    private long id;
    private boolean isPrivate;
    private int membersAmount;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private PlaceDTO startPlace;

    private URI url;
    private URI imageURL;
    private URI imageCardURL;
    private URI startPlaceURL;

    private URI commentsURL;
    private URI membersURL;
    private URI activitiesURL;

    public TripDTO() {
        // Empty constructor needed by JAX-RS
    }

    public TripDTO(Trip trip, final URI baseUri) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.description = trip.getDescription();
        this.startDate = DateManipulation.changeDateFormat(trip.getStartDate());
        this.endDate = DateManipulation.changeDateFormat(trip.getEndDate());
        this.isPrivate = trip.isPrivate();
        this.membersAmount = trip.getMembers().size();
        this.startPlace = new PlaceDTO(trip.getStartPlace());

        this.url = baseUri.resolve("trips/" + id);
        this.startPlaceURL = baseUri.resolve("places/" + startPlace.getId());
        this.membersURL = baseUri.resolve("trips/" + id + "/members");
        this.activitiesURL = baseUri.resolve("trips/" + id + "/activities");
        this.commentsURL = baseUri.resolve("trips/" + id + "/comments");

        if (trip.getProfilePicture() != null) {
            imageURL = baseUri.resolve("trips/" + id + "/image");
            imageCardURL = baseUri.resolve("trips/" + id + "/image-card");
        }
        this.status = checkStatus(trip);
    }

    private String checkStatus(Trip trip) {
        TripStatus status = trip.getStatus();
        if (!status.equals(TripStatus.COMPLETED)) {
            LocalDate now = LocalDate.now();
            if (now.isAfter(trip.getStartDate()) || now.isEqual(trip.getStartDate())) {
                return TripStatus.IN_PROGRESS.name();
            }
        }
        return trip.getStatus().name();
    }

    public URI getCommentsURL() {
        return commentsURL;
    }

    public void setCommentsURL(URI commentsURL) {
        this.commentsURL = commentsURL;
    }

    public URI getMembersURL() {
        return membersURL;
    }

    public void setMembersURL(URI membersURL) {
        this.membersURL = membersURL;
    }

    public URI getActivitiesURL() {
        return activitiesURL;
    }

    public void setActivitiesURL(URI activitiesURL) {
        this.activitiesURL = activitiesURL;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public URI getImageURL() {
        return imageURL;
    }

    public void setImageURL(URI imageURL) {
        this.imageURL = imageURL;
    }

    public URI getImageCardURL() {
        return imageCardURL;
    }

    public void setImageCardURL(URI imageCardURL) {
        this.imageCardURL = imageCardURL;
    }

    public URI getStartPlaceURL() {
        return startPlaceURL;
    }

    public void setStartPlaceURL(URI startPlaceURL) {
        this.startPlaceURL = startPlaceURL;
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
