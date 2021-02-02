import {Place} from "./place";
import {Activity} from "./activity";
import {User} from "./user";
import {Comment} from "./comment";
import {TripMember} from "./TripMember";

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
    status: number;
}

export class FullTrip extends Trip {

    activities: Activity[];
    comments: Comment[];
    members: TripMember[];
    admins: User[];

}
