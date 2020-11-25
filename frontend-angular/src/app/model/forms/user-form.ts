export class UserForm {

    firstname: string;
    lastname: string;
    email: string;
    password: string;
    pswrepeat: string;
    birthday: string;
    nationality: string;
    sex: string;
    verificationURL: string;


    constructor(firstname, lastname, email, password, pswrepeat, nationality, birthday, sex, verificationURL) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.pswrepeat = pswrepeat;
        this.nationality = nationality;
        this.birthday = birthday;
        this.sex = sex;
        this.verificationURL = verificationURL;
    }
}


