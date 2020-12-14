import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../model/user";

@Component({
  selector: 'user-list-item',
  templateUrl: './user-list-item.component.html',
  styleUrls: ['./user-list-item.component.scss']
})
export class UserListItemComponent implements OnInit {

  @Input() user: User;

  constructor() { }

  ngOnInit(): void {
    console.log("user" + JSON.stringify(this.user));
  }

  inviteUser($event: MouseEvent) {
    console.log("clicked on " + this.user.firstname);
  }
}
