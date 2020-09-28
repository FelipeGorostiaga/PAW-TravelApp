package ar.edu.itba.paw.webapp.dto;

public class AuthenticationResponseDTO {

    public AuthenticationResponseDTO() {
        // Empty constructor needed by JAX-RS
    }

    private String accessToken;
    private String refreshToken;

    public AuthenticationResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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
