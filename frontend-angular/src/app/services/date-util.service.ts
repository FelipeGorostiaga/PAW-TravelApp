import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class DateUtilService {

    constructor() {

    }

    public convertToDateString(date): string {
        let dateString = "";
        let day = date.getDate();
        let month = date.getMonth() + 1;
        let year = date.getFullYear();
        if (month < 10) {
            if (day < 10) {
                dateString = `0${day}/0${month}/${year}`;
            } else {
                dateString = `${day}/0${month}/${year}`;
            }
        } else {
            if (day < 10) {
                dateString = `0${day}/${month}/${year}`;
            } else {
                dateString = `${day}/${month}/${year}`;
            }
        }
        return dateString;
    }

    public stringToDate(dateString: string): Date {
        let day = Number(dateString.slice(0, 2));
        let month = Number(dateString.slice(3, 5)) - 1;
        let year = Number(dateString.slice(6, 10));
        return new Date(Number(year), Number(month), Number(day));
    }

    public parseDate(str) {
        var m = str.match(/^(\d{1,2})\/(\d{1,2})\/(\d{4})$/);
        return (m) ? new Date(m[3], m[2] - 1, m[1]) : null;
    }

}
