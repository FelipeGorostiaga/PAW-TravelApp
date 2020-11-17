export class ActivityForm {

    name: string;
    category: string;
    placeInput: string;
    startDate: string;
    endDate: string;
    latitude: number;
    longitude: number;

    constructor(name: string, category: string, placeInput: string, latitude: number, longitude: number, startDate: string, endDate: string) {
        this.name = name;
        this.category = category;
        this.placeInput = placeInput;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude =longitude;
    }
}
