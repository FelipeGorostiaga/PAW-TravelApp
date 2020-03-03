package ar.edu.itba.paw.webapp.dto;

public class AuthenticationResponseDTO {

    public AuthenticationResponseDTO() {
        // Empty constructor needed by JAX-RS
    }

    private String jwt;

    public AuthenticationResponseDTO(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}
