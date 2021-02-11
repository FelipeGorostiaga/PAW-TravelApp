import {User} from "./user";
import {Trip} from "./trip";

export class Rate {

    id: number;
    ratedBy: User;
    ratedUser: User;
    comment: string;
    rate: number;
    createdOn: string;
    trip: Trip;

    constructor(id, ratedBy, ratedUser, comment, rate, createdOn, trip) {
        this.id = id;
        this.ratedBy = ratedBy;
        this.ratedUser = ratedUser;
        this.rate = rate;
        this.comment = comment;
        this.createdOn = createdOn;
        this.trip = trip;
    }


}
