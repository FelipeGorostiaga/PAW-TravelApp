package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Trip;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TripDTO {

    private long id;
    private long adminId;
    private long startPlaceId;
    private boolean isPrivate;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private ImageDTO tripPicture;
    private List<TripCommentDTO> comments;
    private List<ActivityDTO> activities;
    private List<UserDTO> users;

    public TripDTO() {
        // Empty constructor needed by JAX-RS
    }

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.description = trip.getDescription();
        this.tripPicture = new ImageDTO(trip.getProfilePicture());
        this.startDate = trip.getStartDate();
        this.endDate = trip.getEndDate();
        this.adminId = trip.getAdminId();
        this.startPlaceId = trip.getStartPlaceId();
        this.users = trip.getUsers().stream().map(UserDTO::new).collect(Collectors.toList());
        this.activities = trip.getActivities().stream().map(ActivityDTO::new).collect(Collectors.toList());
        this.comments = trip.getComments().stream().map(TripCommentDTO::new).collect(Collectors.toList());
        this.isPrivate = trip.isPrivate();
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

    public long getStartPlaceId() {
        return startPlaceId;
    }

    public void setStartPlaceId(long startPlaceId) {
        this.startPlaceId = startPlaceId;
    }

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
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

    public List<TripCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<TripCommentDTO> comments) {
        this.comments = comments;
    }

    public ImageDTO getTripPicture() {
        return tripPicture;
    }

    public void setTripPicture(ImageDTO tripPicture) {
        this.tripPicture = tripPicture;
    }
}
