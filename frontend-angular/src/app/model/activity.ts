import {Place} from "./place";

export class Activity {

    id: number;
    name: string;
    description: string;
    place: Place;
    category: string;
    startDate: string;
    endDate: string;

    constructor(id: number, name: string, place: Place, category: string, startDate: string, endDate: string, description: string) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }
}
