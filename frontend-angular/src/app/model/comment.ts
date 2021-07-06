import {User} from "./user";

export class Comment {
    tripId: number;
    user: User;
    comment: string;
    createdOn: Date;
}
