import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {User} from "../../../model/user";

@Component({
    selector: 'user-list-item',
    templateUrl: './user-list-item.component.html',
    styleUrls: ['./user-list-item.component.scss']
})
export class UserListItemComponent implements OnInit {

    @Input() user: User;
    @Output() userClickedEvent = new EventEmitter();
    @Input() userLang: string;

    sendInviteTitle: string;
    sendInviteMessage: string;

    constructor() {
    }

    ngOnInit(): void {
        if (this.userLang == 'es') {
            this.sendInviteTitle = "Enviar invitacion";
            this.sendInviteMessage = "Estas seguro que deseas enviar una invitacion";
        } else {
            this.sendInviteTitle = "Send trip invite";
            this.sendInviteMessage = "Are you sure you want to send a trip invitation?";
        }
    }
}
