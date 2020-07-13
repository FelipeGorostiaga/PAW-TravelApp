export class UserForm {

    firstname: string;
    lastname: string;
    email: string;
    password: string;
    pswrepeat: string;
    birthday: string;
    nationality: string;

    constructor(firstname, lastname, email, password, pswrepeat, nationality, birthday) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.pswrepeat = pswrepeat;
        this.nationality = nationality;
        this.birthday = birthday;
    }
}


