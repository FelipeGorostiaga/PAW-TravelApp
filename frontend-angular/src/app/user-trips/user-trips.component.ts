import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {Trip} from "../model/trip";
import {ApiUserService} from "../services/api-user.service";
import {AuthService} from "../services/auth/auth.service";
import {User} from "../model/user";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

@Component({
  selector: 'app-user-trips',
  templateUrl: './user-trips.component.html',
  styleUrls: ['./user-trips.component.scss']
})
export class UserTripsComponent implements OnInit {

  trips: Trip[][];
  currentPage = 0;
  tripsPerPage = 8;
  numberOfPages: number;
  loggedUser: User;
  loading = true;

  constructor(private userService: ApiUserService, private authService: AuthService, private router: Router,
              private spinner: NgxSpinnerService) { }

  ngOnInit(): void {
    this.spinner.show();
    this.loggedUser = this.authService.getLoggedUser();
    this.userService.getUserTrips(this.loggedUser.id).subscribe(
        res => {
          let totalTrips = res.length;
          this.trips = this.chopList(res);
          this.numberOfPages = Math.ceil(totalTrips / this.tripsPerPage);
          this.loading = false;
          this.spinner.hide();
        },
        error => {
          this.spinner.hide();
          this.loading = false;
        }
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
