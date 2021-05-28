import {Component, Input, OnInit} from '@angular/core';
import {ApiUserService} from "../../services/api-user.service";
import {User} from "../../model/user";

@Component({
    selector: 'app-invitations',
    templateUrl: './invitations.component.html',
    styleUrls: ['./invitations.component.scss']
})
export class InvitationsComponent implements OnInit {

    @Input() user: User;

    constructor(private userService: ApiUserService) {
    }

    ngOnInit(): void {
        this.userService.getUserInvitations(this.user.invitationsURL).subscribe(
            data => {
                console.log('invitations:');
                console.log(data);
                this.user.invitations = data;
            }
        )
    }

}
