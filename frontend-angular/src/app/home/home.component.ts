import {Component, OnInit} from '@angular/core';
import {ApiTripService} from "../services/api-trip.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  trips: any[][];
  currentPage: number;
  numberOfPages: number;
  tripsPerPage = 6;

  constructor(private ts: ApiTripService) { }

  ngOnInit() {
      this.currentPage = 0;
      this.ts.getAllTrips().subscribe(
        res => {
            this.trips = this.chopList(res);
            this.numberOfPages = Math.ceil(this.trips.length / this.tripsPerPage);
        },
        err => {
            console.log("Error: couldn't get trips from server");
        }
    );
  }

  chopList(arr: any) {
      const newarr = new Array();
      for (let i = 0; i < arr.length; i = i + this.tripsPerPage) {
          let tempArray = arr.slice(i, i + this.tripsPerPage)
          newarr.push(tempArray);
      }
      return newarr;
  }

  updatePage(newPage) {
      this.currentPage = newPage;
  }

}
