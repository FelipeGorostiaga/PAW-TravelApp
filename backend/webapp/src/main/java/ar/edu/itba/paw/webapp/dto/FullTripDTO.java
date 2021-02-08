package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FullTripDTO {

    private Long id;
    private Boolean isPrivate;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private PlaceDTO startPlace;
    private List<TripCommentDTO> comments;
    private Set<TripMemberDTO> members;
    private List<ActivityDTO> activities;

    public FullTripDTO() {
        // Empty constructor needed by JAX-RS
    }

    public FullTripDTO(Trip trip, List<TripComment> comments) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.description = trip.getDescription();
        this.startDate = DateManipulation.changeDateFormat(trip.getStartDate());
        this.endDate = DateManipulation.changeDateFormat(trip.getEndDate());
        this.startPlace = new PlaceDTO(trip.getStartPlace());
        this.isPrivate = trip.isPrivate();
        this.comments = comments.stream().map(TripCommentDTO::new).collect(Collectors.toList());
        this.activities = trip.getActivities().stream().map(ActivityDTO::new).collect(Collectors.toList());
        this.members = trip.getMembers().stream().map(TripMemberDTO::new).collect(Collectors.toSet());
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<TripMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<TripMemberDTO> members) {
        this.members = members;
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

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
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
