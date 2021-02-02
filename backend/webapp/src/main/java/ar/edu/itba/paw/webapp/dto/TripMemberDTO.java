package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripMember;

public class TripMemberDTO {

    private String role;

    private UserDTO user;

    public TripMemberDTO() {
        // needed by JAX-RS
    }

    public TripMemberDTO(TripMember member) {
        this.role = member.getRole().name();
        this.user = new UserDTO(member.getUser());

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
}
