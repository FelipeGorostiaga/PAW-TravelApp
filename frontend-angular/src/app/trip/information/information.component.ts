import {Component, Input, OnInit} from '@angular/core';
import {FullTrip, Trip} from "../../model/trip";
import {ApiTripService} from "../../services/api-trip.service";
import {AuthService} from "../../services/auth/auth.service";

@Component({
    selector: 'app-information',
    templateUrl: './information.component.html',
    styleUrls: ['./information.component.scss']
})
export class InformationComponent implements OnInit {

    @Input() trip: FullTrip;
    @Input() isAdmin: boolean;
    @Input() isMember: boolean;
    hasImage: boolean;
    tripImage: any;

    constructor(private tripService: ApiTripService, private authService: AuthService) {
    }

    ngOnInit() {
        if (this.trip != null) {
            const tripId = this.trip.id;
            this.tripService.getTripImage(tripId).subscribe(
                data => {
                    this.tripImage = data.image;
                    this.hasImage = true;
                },
                error => {
                    this.hasImage = false;
                }
            );
        }
    }

    leaveTrip() {
        if(confirm("Are you sure you want to leave this trip?")) {
            // TODO
        }

    }

    requestJoinTrip() {
        if (confirm("are you sure you want to request to join this trip?")) {
            this.tripService.sendJoinRequest(this.trip.id, this.authService.getLoggedUser().id).subscribe(
                data => {
                    console.log("request to join trip sent!");
                },
                error => {
                    console.log("error sending request");
                }
            );
        }
        return;
    }
}
