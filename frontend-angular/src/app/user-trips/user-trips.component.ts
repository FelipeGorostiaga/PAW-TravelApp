import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {Trip} from "../model/trip";
import {ApiUserService} from "../services/api-user.service";
import {AuthService} from "../services/auth/auth.service";
import {User} from "../model/user";

@Component({
  selector: 'app-user-trips',
  templateUrl: './user-trips.component.html',
  styleUrls: ['./user-trips.component.scss']
})
export class UserTripsComponent implements OnInit {

  trips: Trip[][];
  currentPage = 0;
  tripsPerPage = 6;
  numberOfPages: number;
  loggedUser: User;

  constructor(private userService: ApiUserService, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.loggedUser = this.authService.getLoggedUser();
    this.userService.getUserTrips(this.loggedUser.id).subscribe(
        res => {
          this.trips = this.chopList(res);
          this.numberOfPages = Math.ceil(this.trips.length / this.tripsPerPage);
        },
        error => {}
    );
  }

  chopList(arr: any) {
    const newarr = [];
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
