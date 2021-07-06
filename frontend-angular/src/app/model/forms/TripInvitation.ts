import {Trip} from "../trip";
import {User} from "../user";

export interface TripInvitation {

    id: number;
    trip: Trip;
    inviter: User;
    invitee: User;
    token: string;
    responded: boolean;

}
