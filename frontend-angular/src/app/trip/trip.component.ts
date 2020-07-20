import {AfterContentInit, Component, OnInit} from '@angular/core';
import {Trip} from "../model/trip";
import {ActivatedRoute, Router} from "@angular/router";
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
export class TripComponent implements OnInit, AfterContentInit {

    loggedUser: User;

    trip: Trip;

    tripImage: any;
    hasImage: boolean;

    comments: Comment[];
    activities: Activity[];
    users: User[];
    admins: User[];

    isAdmin: boolean;

    startPlace: Place;

    constructor(private router: Router, private ts: ApiTripService, private route: ActivatedRoute,
                private authService: AuthService) { }

    ngOnInit() {
        let serverError = false;
        const tripId = Number(this.route.snapshot.paramMap.get("id"));
        this.loggedUser = this.authService.getLoggedUser();
        this.hasImage = false;
        this.ts.getTrip(tripId).subscribe(
            res => {
                this.trip = res;
                console.log(res);
            },
            err => {
                alert("Error getting trip from server...");
                serverError = true;
            });
        this.ts.getTripImage(tripId).subscribe(
            res => {
                // TODO
                this.tripImage = res.image;
                console.log(res);
            },
            err => {
                console.log("Trip has no image!");
            });

        this.ts.getTripActivities(tripId).subscribe(
            res => {
                this.activities = res;
                console.log(res);
            },
            err => {
                console.log("Error getting trip activities");
                serverError = true;
            }
        );

        this.ts.getTripComments(tripId).subscribe(
            res => {
                this.comments = res;
                console.log(res);
            },
            err => {
                console.log("Error getting trip comments");
                serverError = true;
            }
        );

        this.ts.getTripAdmins(tripId).subscribe(
            res => {
                this.admins = res;
                console.log(res);
            },
            err => {
                console.log("Error getting trip admins");
                serverError = true;
            }
        );
        this.ts.getTripUsers(tripId).subscribe(
            res => {
                this.users = res;
                console.log(res);
            },
            err => {
                console.log("Error getting trip users");
                serverError = true;
            }
        );

    }

    ngAfterContentInit() {
        this.isAdmin = this.admins.includes(this.loggedUser);
    }


}
