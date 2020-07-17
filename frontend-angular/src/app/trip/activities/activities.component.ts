import {Component, Input, OnInit} from '@angular/core';
import {Activity} from "../../model/activity";
import {User} from "../../model/user";

@Component({
  selector: 'app-activities',
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.scss']
})
export class ActivitiesComponent implements OnInit {

  @Input() activities: Activity[];
  @Input() users: User[];
  @Input() admin: any;
  @Input() loggedUser: User;

  isEmpty: boolean;

  constructor() { }

  ngOnInit() {
    if (this.activities.length === 0) {
      this.isEmpty = true;
    }
  }

}
