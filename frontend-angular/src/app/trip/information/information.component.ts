import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {Trip} from "../../model/trip";
import {Place} from "../../model/place";
import {ApiTripService} from "../../services/api-trip.service";

@Component({
  selector: 'app-information',
  templateUrl: './information.component.html',
  styleUrls: ['./information.component.scss']
})
export class InformationComponent implements OnInit {

  @Input() loggedUser: User;
  @Input() trip: Trip;
  @Input() users: User[];
  @Input() admins: User[];
  @Input() startPlace: Place;
  hasImage: boolean;
  tripImage: any;
  isAdmin: boolean;

  constructor(private ts: ApiTripService) { }

  ngOnInit() {
    if(this.trip != null) {
      const tripId = this.trip.id;
      this.ts.getTripImage(tripId).subscribe(
          data => {
            this.tripImage = data.image;
            this.hasImage = true;
          },
          error => {
            this.hasImage = false;
          }
      );
      this.isAdmin = this.admins.includes(this.loggedUser);
    }
  }

  leaveTrip() {

  }
  joinTrip() {

  }
}
