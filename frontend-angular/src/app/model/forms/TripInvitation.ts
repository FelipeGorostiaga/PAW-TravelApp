export class TripInvitation {

    userId: number;
    tripId: number;

    constructor(tripId: number, userId: number) {
        this.tripId = tripId;
        this.userId = userId;
    }
}
