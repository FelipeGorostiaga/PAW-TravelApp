import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Activity} from "../../../model/activity";
import {ApiTripService} from "../../../services/api-trip.service";

@Component({
  selector: 'app-map-activity',
  templateUrl: './map-activity.component.html',
  styleUrls: ['./map-activity.component.scss']
})
export class MapActivityComponent implements OnInit {

  @Input() activity: Activity;
  @Output()
  deleteActivityEvent: EventEmitter<number> = new EventEmitter<number>();
  zoom: number;
  showMap: boolean;

  constructor(tripService: ApiTripService) { }

  ngOnInit(): void {
    this.zoom = 14;
  }

  toggle() {
    this.showMap = !this.showMap;
  }

    deleteActivity() {
      if (confirm("Are you sure you want to delete activity " + this.activity.name + " ?")) {
        // @ts-ignore
        this.deleteActivityEvent.emit(this.activity);
      }
    }
}
