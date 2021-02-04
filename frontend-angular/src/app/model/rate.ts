import {User} from "./user";

export class Rate {

    id: number;
    ratedBy: User;
    ratedUser: User;
    comment: string;
    rate: number;
    createdOn: string;

    constructor(id, ratedBy, ratedUser, comment, rate, createdOn) {
        this.id = id;
        this.ratedBy = ratedBy;
        this.ratedUser = ratedUser;
        this.rate = rate;
        this.comment = comment;
        this.createdOn = createdOn;
    }


}
