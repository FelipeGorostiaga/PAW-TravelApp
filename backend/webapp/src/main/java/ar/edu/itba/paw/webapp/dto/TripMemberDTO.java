package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripMember;

import java.util.Objects;

public class TripMemberDTO {

    private String role;

    private UserDTO user;

    private long tripId;

    public TripMemberDTO() {
        // needed by JAX-RS
    }

    public TripMemberDTO(TripMember member) {
        this.role = member.getRole().name();
        this.user = new UserDTO(member.getUser());
        this.tripId = member.getTrip().getId();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripMemberDTO)) return false;
        TripMemberDTO that = (TripMemberDTO) o;
        return getTripId() == that.getTripId() &&
                getRole().equals(that.getRole()) &&
                getUser().equals(that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRole(), getUser(), getTripId());
    }
}
