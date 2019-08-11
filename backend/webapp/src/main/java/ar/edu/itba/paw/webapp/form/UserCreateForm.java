package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.DateManipulation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class UserCreateForm {

    @Pattern(regexp = "[a-zA-ZñáéíóúüÑÁÉÍÓÚÜ]+[ ]?([a-zA-ZÑÁÉÍÓÚÜñáéíóúü])*$")
    @Size(min = 2, max = 100)
    private String firstname;

    @Pattern(regexp = "[a-zA-ZñáéíóúüÑÁÉÍÓÚÜ]+[ ]?([a-zA-ZÑÁÉÍÓÚÜñáéíóúü])*$")
    @Size(min = 2, max = 100)
    private String lastname;

    @Pattern(regexp =  "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(min = 6, max = 100)
    private String email;

    @Size(min = 8, max = 100)
    private String password;

    @Size(min = 8, max = 100)
    private String pswrepeat;

    @NotNull
    @Size(min = 8, max = 10)
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

    public boolean checkBirthday() {
        LocalDate birth = DateManipulation.stringToLocalDate(birthday);
        LocalDate now = LocalDate.now();
        return birth.isBefore(now);
    }
}
