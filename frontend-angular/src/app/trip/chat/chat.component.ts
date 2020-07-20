import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../model/user";
import {Comment} from "../../model/comment";


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  @Input() loggedUser: User;
  @Input() users: User[];
  @Input() admins: User[];
  @Input() comments: Comment[];

  constructor() { }

  ngOnInit() {
  }

}
