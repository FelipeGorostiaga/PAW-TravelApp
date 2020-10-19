import {Component, Input, OnInit} from '@angular/core';
import {FullTrip, Trip} from "../../model/trip";
import {ApiTripService} from "../../services/api-trip.service";

@Component({
  selector: 'app-information',
  templateUrl: './information.component.html',
  styleUrls: ['./information.component.scss']
})
export class InformationComponent implements OnInit {

  @Input() trip: FullTrip;
  @Input() isAdmin: boolean;
  hasImage: boolean;
  tripImage: any;

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
    }
  }

  leaveTrip() {

  }
  joinTrip() {

  }
}
