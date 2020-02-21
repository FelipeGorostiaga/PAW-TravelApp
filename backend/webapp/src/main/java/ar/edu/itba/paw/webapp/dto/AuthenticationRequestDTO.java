package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AuthenticationRequestDTO {

    /*@NotNull
    @Pattern(regexp =  "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(min = 6, max = 100)*/
    @Email
    private String username;

    @Size(min = 8, max = 100)
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
