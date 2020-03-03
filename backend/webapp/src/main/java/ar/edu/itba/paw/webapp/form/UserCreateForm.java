package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.annotation.FieldMatch;
import ar.edu.itba.paw.webapp.form.annotation.ValidBirthday;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ValidBirthday(message = "Invalid birthday")
@FieldMatch(first = "password", second = "pswrepeat", message = "Passwords do not match")
public class UserCreateForm {

    public UserCreateForm() {
        // Empty constructor needed by JAX-RS
    }

    @NotNull
    @Pattern(regexp = "[a-zA-ZñáéíóúüÑÁÉÍÓÚÜ]+[ ]?([a-zA-ZÑÁÉÍÓÚÜñáéíóúü])*$")
    @Size(min = 2, max = 100)
    private String firstname;

    @NotNull
    @Pattern(regexp = "[a-zA-ZñáéíóúüÑÁÉÍÓÚÜ]+[ ]?([a-zA-ZÑÁÉÍÓÚÜñáéíóúü])*$")
    @Size(min = 2, max = 100)
    private String lastname;

    @NotNull
    @Pattern(regexp =  "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotNull
    @Size(min = 8, max = 100)
    private String password;

    @NotNull
    @Size(min = 8, max = 100)
    private String pswrepeat;

    @NotNull
    @Pattern(regexp =  "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$")
    private String birthday;

    @NotNull
    @Size(min = 2, max = 5)
    private String nationality;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPswrepeat() {
        return pswrepeat;
    }

    public void setPswrepeat(String pswrepeat) {
        this.pswrepeat = pswrepeat;
    }

    public boolean checkPswRepeat() {
        return password.equals(pswrepeat);
    }

}
