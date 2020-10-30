export class TripForm {

    name: string;
    description: string;
    startDate: string;
    endDate: string;
    isPrivate: boolean;
    placeInput: string;
    latitude: number;
    longitude: number;

    constructor(name: string, description: string, startDate: string, endDate: string, place: string, isPrivate: boolean, latitude: number, longitude: number) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.placeInput = place;
        this.isPrivate = isPrivate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
