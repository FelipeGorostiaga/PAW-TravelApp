import {User} from "./user";
import {Trip} from "./trip";

export class TripMember {

    user: User;
    trip: Trip;
    role: TripRole;


    constructor(user: User, trip: Trip, role: TripRole  ) {
        this.user = user;
        this.trip = trip;
        this.role = role;
    }
}

export enum TripRole {
    CREATOR,
    ADMIN,
    MEMBER
}
