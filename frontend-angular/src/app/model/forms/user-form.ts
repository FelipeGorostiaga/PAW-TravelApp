export class UserForm {

    firstaname: string;
    lastname: string;
    email: string;
    password: string;
    passwordRepeat: string;
    nationality: string;
    birthday: string;

    constructor(firstname, lastname, email, password, passwordRepeat, nationality, birthday) {
        this.firstaname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
        this.nationality = nationality;
        this.birthday = birthday;
    }
}
