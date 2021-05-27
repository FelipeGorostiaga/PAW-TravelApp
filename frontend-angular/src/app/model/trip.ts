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
    private: boolean;
    membersAmount: number;
    status: TripStatus;
    startPlace: Place;

    url: string;
    imageURL: string;
    imageCardURL: string;
    startPlaceURL: string;
    activitiesURL: string;
    commentsURL: string;
    membersURL: string;

    activities: Activity[];
    comments: Comment[];
    members: TripMember[];
    admins: User[];
}


export enum TripStatus {
    DUE = "DUE",
    IN_PROGRESS = "IN PROGRESS",
    COMPLETED = "COMPLETED"
}

