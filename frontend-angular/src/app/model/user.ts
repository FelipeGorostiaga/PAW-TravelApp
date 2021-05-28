import {Rate} from "./rate";

export class User {

    id: number;
    firstname: string;
    lastname: string;
    email: string;
    birthday: string;
    biography: string;
    nationality: string;
    sex: string;

    url: string;
    imageURL: string;
    tripsURL: string;
    ratesURL: string;
    pendingRatesURL: string;
    invitationsURL: string;

    rates: Rate[];
    pendingRates: Rate[];
    rating: number;

    // todo
    invitations: any[];

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

