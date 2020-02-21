package ar.edu.itba.paw.webapp.dto;

public class AuthenticationRequestDTO {

    private String username;
    private String password;

    public AuthenticationRequestDTO() {
        // Empty constructor needed by JAX-RS
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
