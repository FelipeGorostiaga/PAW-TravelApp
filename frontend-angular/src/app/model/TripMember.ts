import {User} from "./user";
import {Trip} from "./trip";

export class TripMember {

    user: User;
    tripId: number;
    role: TripRole;


    constructor(user: User, tripId: number, role: TripRole) {
        this.user = user;
        this.tripId = tripId;
        this.role = role;
    }
}

export enum TripRole {
    CREATOR = "CREATOR",
    ADMIN = "ADMIN",
    MEMBER = "MEMBER"
}
