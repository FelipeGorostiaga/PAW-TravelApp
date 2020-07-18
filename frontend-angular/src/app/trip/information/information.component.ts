import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {Trip} from "../../model/trip";
import {Place} from "../../model/place";

@Component({
  selector: 'app-information',
  templateUrl: './information.component.html',
  styleUrls: ['./information.component.scss']
})
export class InformationComponent implements OnInit {

  @Input() trip: Trip;
  @Input() users: User[];
  @Input() admin: any;
  @Input() loggedUser: User;
  @Input() startPlace: Place;

  constructor() { }

  ngOnInit() {
  }

}
