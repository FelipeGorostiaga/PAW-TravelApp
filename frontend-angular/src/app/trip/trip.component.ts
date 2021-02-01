import {Component, OnInit} from '@angular/core';
import {FullTrip} from "../model/trip";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../model/user";
import {AuthService} from "../services/auth/auth.service";
import {ApiTripService} from "../services/api-trip.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";

@Component({
    selector: 'app-trip',
    templateUrl: './trip.component.html',
    styleUrls: ['./trip.component.scss']
})
export class TripComponent implements OnInit {

    loggedUser: User;
    trip: FullTrip;

    tripId: number;
    isAdmin: boolean;
    selectedIndex: number;
    loading = true;
    isMember = false;

    constructor(private router: Router,
                private ts: ApiTripService,
                private route: ActivatedRoute,
                private authService: AuthService,
                private spinner: NgxSpinnerService) {
    }

    ngOnInit() {
        this.spinner.show();
        this.selectedIndex = 0;
        this.tripId = Number(this.route.snapshot.paramMap.get("id"));
        this.loggedUser = this.authService.getLoggedUser();
        this.ts.getTrip(this.tripId).subscribe(
            res => {
                this.trip = res;
                this.isAdmin =  !!this.trip.admins.find(admin => admin.id === this.loggedUser.id);
                this.isMember = !!(this.isAdmin || this.trip.users.find(user => user.id === this.loggedUser.id));
                this.spinner.hide();
                this.loading = false;
                console.log("Is admin: " + this.isAdmin);
                console.log("Is member: " + this.isMember);
                console.log(this.trip);
            },
            err => {
                this.loading = false;
                this.spinner.hide();
            });
    }


    switchTab(index: number) {
        this.selectedIndex = index;
    }

}
