import { Component, OnInit } from '@angular/core';
import {ApiUserService} from "../services/api-user.service";
import {Router} from "@angular/router";
import {Trip} from "../model/trip";
import {ApiTripService} from "../services/api-trip.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  trips: Trip[][];
  currentPage: number;

  constructor(private ts: ApiTripService, private router: Router) { }

  ngOnInit() {
      this.currentPage = 0;
      this.ts.getAllTrips().subscribe(
        res => {
            this.trips = res;
        },
        err => {
            console.log("ERROR: couldn't get trips from server");
        }
    );
  }

  updatePage(newPage) {
      this.currentPage = newPage;
  }

}
