import {User} from "./user";
import {Trip} from "./trip";
import {Rate} from "./rate";

export class UserProfile {

    user: User;
    dueTrips: Trip[];
    activeTrips: Trip[];
    completedTrips: Trip[];
    rates: Rate[];

    constructor(user: User, dueTrips: Trip[], activeTrips: Trip[], completedTrips: Trip[], rates: Rate[]) {
        this.user = user;
        this.dueTrips = dueTrips;
        this.activeTrips = activeTrips;
        this.completedTrips = completedTrips;
        this.rates = rates;
    }


}
