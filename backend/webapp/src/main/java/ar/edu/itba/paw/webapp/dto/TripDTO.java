package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Trip;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TripDTO {

    private long id;
    private UserDTO admin;
    private PlaceDTO startPlace;
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

    public TripDTO(Trip trip, UserDTO admin, PlaceDTO startPlace) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.description = trip.getDescription();
        this.tripPicture = new ImageDTO(trip.getProfilePicture());
        this.startDate = trip.getStartDate();
        this.endDate = trip.getEndDate();
        this.admin = admin;
        this.startPlace = startPlace;
        this.users = trip.getUsers().stream().map(UserDTO::new).collect(Collectors.toList());
        this.activities = trip.getActivities().stream().map(ActivityDTO::new).collect(Collectors.toList());
        this.comments = trip.getComments().stream().map(TripCommentDTO::new).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDTO getAdmin() {
        return admin;
    }

    public void setAdmin(UserDTO admin) {
        this.admin = admin;
    }

    public PlaceDTO getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(PlaceDTO startPlace) {
        this.startPlace = startPlace;
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
