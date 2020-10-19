import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {Comment} from "../../model/comment";
import {FullTrip, Trip} from "../../model/trip";


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  @Input() trip: FullTrip;
  @Input() isAdmin: boolean;


  constructor() { }

  ngOnInit() {
  }

}
