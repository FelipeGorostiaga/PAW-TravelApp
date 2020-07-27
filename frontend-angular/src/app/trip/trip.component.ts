import {AfterContentInit, Component, OnInit} from '@angular/core';
import {Trip} from "../model/trip";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../model/user";
import {AuthService} from "../services/auth/auth.service";
import {Activity} from "../model/activity";
import {Comment} from "../model/comment";
import {Place} from "../model/place";
import {ApiTripService} from "../services/api-trip.service";
import {ApiPlaceService} from "../services/api-place.service";

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

    tripId: number;
    isAdmin: boolean;

    startPlace: Place;
    selectedIndex: number;

    constructor(private router: Router, private ts: ApiTripService, private route: ActivatedRoute,
                private authService: AuthService, private ps: ApiPlaceService) { }

    ngOnInit() {
        this.selectedIndex = 0;
        let serverError = false;
        this.tripId = Number(this.route.snapshot.paramMap.get("id"));
        this.loggedUser = this.authService.getLoggedUser();
        this.hasImage = false;
        this.ts.getTrip(this.tripId).subscribe(
            res => {
                this.trip = res;
                this.ps.getPlaceById(this.trip.startPlaceId).subscribe(
                    data => {
                        this.startPlace = data;
                    },
                    error => {
                        console.log("error getting trip from server or not found");
                       /* if (error.status === "404") {

                        }*/
                        this.router.navigate(['not-found']);
                    }
                );
                console.log(res);
            },
            err => {
                alert("Error getting trip from server...");
                serverError = true;
            });
        this.ts.getTripImage(this.tripId).subscribe(
            res => {
                // TODO
                this.tripImage = res.image;
                console.log(res);
            },
            err => {
                console.log("Trip has no image!");
            });

        this.ts.getTripActivities(this.tripId).subscribe(
            res => {
                this.activities = res.activities;
            },
            err => {
                console.log("Error getting trip activities");
                this.activities = [];
                serverError = true;
            }
        );

        this.ts.getTripComments(this.tripId).subscribe(
            res => {
                this.comments = res.comments;
            },
            err => {
                this.comments = [];
                console.log("Error getting trip comments");
                serverError = true;
            }
        );

        this.ts.getTripAdmins(this.tripId).subscribe(
            res => {
                this.admins = res.admins;
                console.log(this.admins);
            },
            err => {
                console.log("Error getting trip admins");
                serverError = true;
            }
        );
        this.ts.getTripUsers(this.tripId).subscribe(
            res => {
                this.users = res.users;
                console.log(this.users);
            },
            err => {
                console.log("Error getting trip users");
                serverError = true;
            }
        );
    }

    ngAfterContentInit() {
        if (this.admins) {
            this.isAdmin = this.admins.includes(this.loggedUser);
        } else {
            this.isAdmin = false;
        }
    }

    switchTab(index: number) {
        this.selectedIndex = index;
        console.log(index);
    }
}
