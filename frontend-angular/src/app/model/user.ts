import {Rate} from "./rate";
import {UserTripsData} from "./UserTripsData";
import {TripInvitation} from "./forms/TripInvitation";

export class User {

    id: number;
    firstname: string;
    lastname: string;
    email: string;
    birthday: string;
    biography: string;
    nationality: string;
    sex: string;
    rating: number;
    rates: Rate[];
    pendingRates: Rate[];
    invitations: TripInvitation[];
    tripsData: UserTripsData;
    url: string;
    imageURL: string;
    tripsURL: string;
    ratesURL: string;
    pendingRatesURL: string;
    invitationsURL: string;
    tripsDataURL: string;

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

