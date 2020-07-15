export class TripForm {

    placeInput: string;
    name: string;
    description: string;
    startDate: string;
    endDate: string;
    isPrivate: boolean;

    constructor(name: string, description: string, startDate: string, endDate: string, place: string, isPrivate: boolean) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.placeInput = place;
        this.isPrivate = isPrivate;
    }

}
