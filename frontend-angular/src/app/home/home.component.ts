import {Component, OnInit} from '@angular/core';
import {ApiTripService} from "../services/api-trip.service";
import {Trip} from "../model/trip";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  trips: Trip[][];
  currentPage: number;
  numberOfPages: number;
  tripsPerPage = 6;

  constructor(private ts: ApiTripService) { }

  ngOnInit() {
      this.ts.getAllTrips().subscribe(
        res => {
            console.log(res);
            this.currentPage = 0;
            this.trips = this.chopList(res);
            this.numberOfPages = Math.ceil(this.trips.length / this.tripsPerPage);
        },
        err => {
            console.log("ERROR: couldn't get trips from server");
        }
    );
  }

  chopList(arr: Trip[]) {
      const newarr = [];
      for (let i = 0; i < arr.length; i = i + this.tripsPerPage) {
          newarr.push(arr.slice(i, i + this.tripsPerPage));
      }
      return newarr;
  }

  updatePage(newPage) {
      this.currentPage = newPage;
  }

    activateHttpInterceptor() {
        this.ts.getTrip(4).subscribe(
            res => console.log(res),
            error => console.log(error)

        )
    }
}
