import { Component, OnInit } from '@angular/core';
import {Trip} from "../model/trip";
import {ActivatedRoute, Router} from "@angular/router";
import {ApiUserService} from "../services/api-user.service";
import {User} from "../model/user";
import {AuthService} from "../services/auth/auth.service";
import {Activity} from "../model/activity";
import {Comment} from "../model/comment";
import {Place} from "../model/place";
import {ApiTripService} from "../services/api-trip.service";

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

    startPlace: Place;
    admin: {
        firstName: "admin firstName",
        lastName: "admin lastName"
    };

    constructor(private router: Router, private ts: ApiTripService, private route: ActivatedRoute,
                private authService: AuthService) { }

    ngOnInit() {

        const tripId = Number(this.route.snapshot.paramMap.get("id"));
        this.loggedUser = this.authService.getLoggedUser();
        this.hasImage = false;
        this.ts.getTrip(tripId).subscribe(
            res => {
                this.trip = res;
            },
            error => {
                console.log("Error getting trip");
                this.router.navigate(["/404"]);
            });
        this.ts.getTripImage(tripId).subscribe(
            res => {
                // TODO
                this.tripImage = res.image; },
            error => {
                console.log("Error, trip has no image or server error");
            });
        this.ts.getTripActivities(tripId);
        this.ts.getTripComments(tripId);

        // TODO

        // this.ts.getTripAdmins();
        // this.ts.getTripUsers();
    }

}
