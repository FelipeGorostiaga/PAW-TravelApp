export class User {
    id: number;
    firstname: string;
    lastname: string;
    email: string;
    birthday: string;
    biography: string;
    nationality: string;
    sex: string;

    constructor(id: number, firstname: string, lastname: string, email: string, birthday: string, biography: string, nationality: string, sex: string) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthday = birthday;
        this.biography = biography;
        this.nationality = nationality;
        this.sex = sex;
    }
}

