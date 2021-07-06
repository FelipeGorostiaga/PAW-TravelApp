package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.annotation.FieldMatch;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotNull
    @Size(min = 6, max = 50)
    private String password;

    @NotNull
    @Size(min = 6, max = 50)
    private String pswrepeat;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String birthday;

    @NotNull
    private String nationality;

    @NotNull
    private String sex;


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

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

    @Override
    public String toString() {
        return "UserCreateForm{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", pswrepeat='" + pswrepeat + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nationality='" + nationality + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
