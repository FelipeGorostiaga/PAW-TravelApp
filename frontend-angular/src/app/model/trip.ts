import {Place} from "./place";
import {Activity} from "./activity";
import {User} from "./user";
import {Comment} from "./comment";

export class Trip {

    id: number;
    name: string;
    description: string;
    startDate: string;
    endDate: string;
    adminId: number;
    private: boolean;
    startPlace: Place;
    membersAmount: number;
}

export class FullTrip extends Trip {

    activities: Activity[];
    comments: Comment[];
    users: User[];
    admins: User[];

}
