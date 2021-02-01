import {User} from "./user";

export class Rate {

    ratedBy: User;
    ratedUser: User;
    comment: string;
    rate: number;
    createdOn: string;

    constructor(ratedBy, ratedUser, comment, rate, createdOn) {
        this.ratedBy = ratedBy;
        this.ratedUser = ratedUser;
        this.rate = rate;
        this.comment = comment;
        this.createdOn = createdOn;
    }


}
