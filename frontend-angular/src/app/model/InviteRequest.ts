import { Trip } from "./trip";
import { User } from "./user";

export interface InviteRequest {
    trip: Trip;
    user: User;
    accepted: boolean;
    edited: boolean;

}

