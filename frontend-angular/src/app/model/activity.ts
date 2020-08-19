import {Place} from "./place";

export class Activity {

    id: string;
    name: string;
    place: Place;
    category: string;
    startDate: string;
    endDate: string;

    constructor(id: string, name: string, place: Place, category: string, startDate: string, endDate: string) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
