export class ActivityForm {

    name: string;
    category: string;
    placeInput: string;
    startDate: string;
    endDate: string;

    constructor(name: string, category: string, placeInput: string, startDate: string, endDate: string) {
        this.name = name;
        this.category = category;
        this.placeInput = placeInput;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
