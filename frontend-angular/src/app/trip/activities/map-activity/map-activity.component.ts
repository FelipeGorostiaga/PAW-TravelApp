import {Component, Input, OnInit} from '@angular/core';
import {Activity} from "../../../model/activity";
import {User} from "../../../model/user";

@Component({
  selector: 'app-map-activity',
  templateUrl: './map-activity.component.html',
  styleUrls: ['./map-activity.component.scss']
})
export class MapActivityComponent implements OnInit {

  @Input() activity: Activity;
  zoom: number;

  constructor() { }

  ngOnInit(): void {
    this.zoom = 14;
  }

}
