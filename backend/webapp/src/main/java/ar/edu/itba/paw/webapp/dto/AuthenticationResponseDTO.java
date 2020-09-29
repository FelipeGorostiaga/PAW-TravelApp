package ar.edu.itba.paw.webapp.dto;

public class AuthenticationResponseDTO {

    public AuthenticationResponseDTO() {
        // Empty constructor needed by JAX-RS
    }

    private UserDTO user;
    private String accessToken;
    private String refreshToken;

    public AuthenticationResponseDTO(String accessToken, String refreshToken, UserDTO user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
