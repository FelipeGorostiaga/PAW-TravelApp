package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Trip;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FullTripDTO {

    private Long id;
    private Long adminId;
    private Boolean isPrivate;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private PlaceDTO startPlace;
    private List<TripCommentDTO> comments;
    private List<UserDTO> users;
    private List<UserDTO> admins;
    private List<ActivityDTO> activities;

    public FullTripDTO() {
        // Empty constructor needed by JAX-RS
    }

    public FullTripDTO(Trip trip, PlaceDTO startPlace) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.description = trip.getDescription();
        this.startDate = trip.getStartDate();
        this.endDate = trip.getEndDate();
        this.adminId = trip.getAdminId();
        this.startPlace = startPlace;
        this.isPrivate = trip.isPrivate();
        this.users = trip.getUsers().stream().map(UserDTO::new).collect(Collectors.toList()).;
        this.admins = trip.getAdmins().stream().map(UserDTO::new).collect(Collectors.toList());
        this.comments = trip.getComments().stream().map(TripCommentDTO::new).collect(Collectors.toList());
        this.activities = trip.getActivities().stream().map(ActivityDTO::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
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

    public PlaceDTO getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(PlaceDTO startPlace) {
        this.startPlace = startPlace;
    }

    public List<TripCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<TripCommentDTO> comments) {
        this.comments = comments;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public List<UserDTO> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserDTO> admins) {
        this.admins = admins;
    }

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }
}
