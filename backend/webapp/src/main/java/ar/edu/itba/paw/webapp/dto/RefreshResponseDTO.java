package ar.edu.itba.paw.webapp.dto;

public class RefreshResponseDTO {

    private String accessToken;

    public RefreshResponseDTO() {

    }

    public RefreshResponseDTO(String token) {
        this.accessToken = token;

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
