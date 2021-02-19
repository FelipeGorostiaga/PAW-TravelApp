import {User} from "./user";
import {Rate} from "./rate";

export interface UserProfile {

    user: User;
    dueTripsAmount: number;
    activeTripsAmount: number;
    completedTripsAmount: number;
    rates: Rate[];

}
