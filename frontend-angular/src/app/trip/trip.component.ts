import {Component, OnInit} from '@angular/core';
import {FullTrip} from "../model/trip";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../model/user";
import {AuthService} from "../services/auth/auth.service";
import {ApiTripService} from "../services/api-trip.service";
import {NgxSpinnerService} from "ngx-bootstrap-spinner";
import {TripRole} from "../model/TripMember";

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
                let members = this.trip.members;
                this.isAdmin = !!members.find(member => (member.role === TripRole.CREATOR || member.role === TripRole.ADMIN) &&
                    member.user.id === this.loggedUser.id);
                this.isMember = !!(this.isAdmin || members.find(member => member.user.id === this.loggedUser.id));
                this.spinner.hide();
                this.loading = false;
                console.log("Is admin: " + this.isAdmin);
                console.log("Is member: " + this.isMember);
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
