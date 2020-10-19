import {Component, OnInit} from '@angular/core';
import {FullTrip} from "../model/trip";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../model/user";
import {AuthService} from "../services/auth/auth.service";
import {ApiTripService} from "../services/api-trip.service";

@Component({
  selector: 'app-trip',
  templateUrl: './trip.component.html',
  styleUrls: ['./trip.component.scss']
})
export class TripComponent implements OnInit {

    loggedUser: User;

    trip: FullTrip;

    tripImage: any;
    hasImage: boolean;

    tripId: number;
    isAdmin: boolean;
    selectedIndex: number;

    constructor(private router: Router, private ts: ApiTripService, private route: ActivatedRoute,
                private authService: AuthService) { }

    ngOnInit() {
        this.selectedIndex = 0;
        let serverError = false;
        this.tripId = Number(this.route.snapshot.paramMap.get("id"));
        this.loggedUser = this.authService.getLoggedUser();
        this.hasImage = false;
        this.ts.getTrip(this.tripId).subscribe(
            res => {
                this.trip = res;
                this.isAdmin = this.trip.admins.includes(this.loggedUser);
                console.log(res);
            },
            err => {
                console.log("Error getting trip from server");
                serverError = true;
            });
    }


    switchTab(index: number) {
        this.selectedIndex = index;
        console.log(index);
    }
}
