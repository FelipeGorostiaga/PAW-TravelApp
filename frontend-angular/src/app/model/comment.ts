import {User} from "./user";
import {TripMember} from "./TripMember";

export class Comment {
    member: TripMember;
    comment: string;
    createdOn: string;
}
