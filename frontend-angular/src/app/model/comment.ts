import {User} from "./user";
import {TripMember} from "./TripMember";

export class Comment {
    tripId: number;
    user: User;
    comment: string;
    createdOn: string;
}
