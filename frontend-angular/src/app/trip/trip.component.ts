import { Component, OnInit } from '@angular/core';
import {Trip} from "../model/trip";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiUserService} from "../services/api-user.service";
import {User} from "../model/user";
import {AuthService} from "../services/auth/auth.service";
import {Activity} from "../model/activity";
import {Comment} from "../model/comment";

@Component({
  selector: 'app-trip',
  templateUrl: './trip.component.html',
  styleUrls: ['./trip.component.scss']
})
export class TripComponent implements OnInit {

    loggedUser: User;

    trip: Trip;

    tripImage: any;
    hasImage: boolean;

    comments: Comment[];
    activities: Activity[];
    users: User[];

    place: string;
    admin: {
        firstName: "admin firstName",
        lastName: "admin lastName"
    };

    constructor(private router: Router, private apiService: ApiUserService, private route: ActivatedRoute,
                private authService: AuthService) { }

    ngOnInit() {
          this.place = "MOCK PLACE";
          this.loggedUser = this.authService.getLoggedUser();
          this.hasImage = false;
          const tripId = Number(this.route.snapshot.paramMap.get("id"));
          this.apiService.getTrip(tripId).subscribe(
            res => {
                this.trip = res;
            },
            error => {
                console.log("Error getting trip");
                this.router.navigate(["/404"]);
            }
            );
          this.apiService.getTripImage(tripId).subscribe(
              res => {
                  // TODO
                  this.tripImage = res.image;
              },
              error => {
                  console.log("Error, trip has no image or server error");
              }
          );


    }

}
