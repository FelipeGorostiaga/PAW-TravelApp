package ar.edu.itba.paw.webapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthenticationRequestDTO {

    public AuthenticationRequestDTO() {
        // Empty constructor needed by JAX-RS
    }

    @NotNull
    @Size(min = 6, max = 100)
    private String username;

    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    public AuthenticationRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
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
