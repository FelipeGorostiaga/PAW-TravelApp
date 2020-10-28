import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DateUtilService {

  constructor() { }

  convertToDateString(date): string {
    let dateString = "";
    let day = date.getDate()
    let month = date.getMonth() + 1
    let year = date.getFullYear()
    if(month < 10){
      dateString = `${day}/0${month}/${year}`;
    }else{
      dateString = `${day}/${month}/${year}`;
    }
    return dateString;
  }

}
